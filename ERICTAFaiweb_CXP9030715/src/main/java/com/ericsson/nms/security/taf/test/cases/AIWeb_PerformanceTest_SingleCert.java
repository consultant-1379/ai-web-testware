package com.ericsson.nms.security.taf.test.cases;

import javax.inject.Inject;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.nms.host.HostConfigurator;
import com.ericsson.nms.security.taf.test.operators.AIWebOperator;

public class AIWeb_PerformanceTest_SingleCert extends TorTestCaseHelper implements TestCase {

    public static final String HTTPS = "https://";

    @Inject
    private OperatorRegistry<AIWebOperator> aIWebProvider;

    @BeforeSuite
    void setup() {
        WriteFilesToServer.copyRequiredFilesToServer(HostConfigurator.getSC1());
    }

    /**
     * @DESCRIPTION Test case for successfull downloading a conf file using http
     *              request
     * @PRE Application Server is running with REST and the files are created on
     *      the file system.
     * @PRIORITY HIGH
     */
    @TestId(id = "TS-9_Perf_9", title = "Download the file using the same logical name/serial combinations")
    @Context(context = { Context.REST })
    @DataDriven(name = "aiweb_regression_downloadfileSuccessPerformance")
    @Test(groups = { "{Acceptance", "aiweb_regression_downloadfileSuccess}" })
    public void downloadTheFileUsingTheSameLogicalNameSerialCombinations(@Input("host") final String hostIn,
            @Input("jbossServer") final String jbossServer, @Input("filename") final String filename,
            @Input("configFileLocationOnServer") final String configFileLocationOnServer,
            @Input("privateKeyLocation") final String privateKeyLocation, @Input("privateCertLocation") final String privateCertLocation,
            @Output("result") final boolean expected) {

        verifyConfigFileDownload(hostIn, jbossServer, filename, configFileLocationOnServer, privateKeyLocation, privateCertLocation, expected);
    }

    private void verifyConfigFileDownload(final String hostIn, final String jbossServer, final String filename,
            final String configFileLocationOnServer, final String privateKeyLocation, final String privateCertLocation, final boolean expected) {
        final AIWebOperator aiWebOperator = aIWebProvider.provide(AIWebOperator.class);
        setTestInfo("Send a request to the server with Serial number of NE");

        final Host host = getHost(hostIn);
        final Host secServ = DataHandler.getHostByName("sc1jbi");

        aiWebOperator.downloadConfigFile(host, HTTPS + secServ.getOriginalIp() + jbossServer, filename, configFileLocationOnServer,
                privateKeyLocation, privateCertLocation);

        final boolean checkFilesEqual = RemoteFileHandler.compareRemoteFiles(host, configFileLocationOnServer + "tmp/result", host,
                configFileLocationOnServer + filename);

        assertSame(expected, checkFilesEqual);
    }

    private Host getHost(final String hostIn) {
        if (hostIn.equals("sc1")) {
            return HostConfigurator.getSC1();
        } else if (hostIn.equals("sc2")) {
            return HostConfigurator.getSC2();
        } else {
            return null;
        }
    }

}
