package com.ericsson.nms.security.taf.test.cases;

import javax.inject.Inject;

import org.testng.annotations.BeforeClass;
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

public class AIWeb_FunctionalTest extends TorTestCaseHelper implements TestCase {

    private static final String SERIALNUMBER123_ERICSSON_COM = "SERIALNUMBER123.ericsson.com";
    public static final String HTTPS = "https://";

    @Inject
    private OperatorRegistry<AIWebOperator> aIWebProvider;

    @BeforeClass
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
    @TestId(id = "TS-9_Func_9", title = "Download the file using the same logical name/serial combinations")
    @Context(context = { Context.REST })
    @DataDriven(name = "aiweb_regression_downloadfile")
    @Test(groups = { "{Acceptance", "aiweb_regression_downloadfileSuccess}" })
    public void downloadTheFileUsingTheSameLogicalNameSerialCombinations(
            @Input("host") final String hostIn,
            @Input("jbossServer") final String jbossServer, //sc1jbi 
            @Input("filename") final String filename, @Input("configFileLocationOnServer") final String configFileLocationOnServer,
            @Input("privateKeyLocation") final String privateKeyLocation, @Input("privateCertLocation") final String privateCertLocation,
            @Output("result") final boolean expected) {

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

    /**
     * @DESCRIPTION Test case for successfull downloading a conf file using http
     *              request
     * @PRE Application Server is running with REST and the files are created on
     *      the file system.
     * @PRIORITY HIGH
     */
    @Context(context = { Context.REST })
    @Test(groups = { "{Acceptance", "aiweb_regression}" })
    @DataDriven(name = "aiweb_regression_downloadfileSuccessDifferentFilename")
    public void downloadTheFileUsingDifferentLogicalNameSerialCombinations(@Input("host") final String hostIn,
            @Input("jbossServer") final String jbossServer, @Input("filename") final String filename,
            @Input("configFileLocationOnServer") final String configFileLocationOnServer,
            @Input("privateKeyLocation") final String privateKeyLocation, @Input("privateCertLocation") final String privateCertLocation,
            @Output("result") final boolean expected) {

        final AIWebOperator aIWebOperator = aIWebProvider.provide(AIWebOperator.class);
        setTestInfo("Send a request to the server with incorrect Serial number of NE");

        final Host host = getHost(hostIn);
        final Host secServ = DataHandler.getHostByName("sc1jbi");

        aIWebOperator.downloadConfigFile(host, HTTPS + secServ.getOriginalIp() + jbossServer, filename, configFileLocationOnServer,
                privateKeyLocation, privateCertLocation);

        final boolean checkFilesEqual = RemoteFileHandler.compareRemoteFiles(host, configFileLocationOnServer + "tmp/result", host,
                configFileLocationOnServer + SERIALNUMBER123_ERICSSON_COM);

        assertSame(expected, checkFilesEqual);
    }

    /**
     * @DESCRIPTION Read a file from the system when none exists
     * @PRE Application Server is running with REST and no files are created on
     *      the file system
     * @PRIORITY HIGH
     */
    @TestId(id = "TS-9_Func_11", title = "Read file when none exists - send request with valid key/cert but no configuration file exists")
    @Context(context = { Context.REST })
    @DataDriven(name = "aiweb_regression_downloadfileWhereNoFileExists")
    @Test(groups = { "{Acceptance", "aiweb_regression}" })
    public void readFileWhenNoneExists(@Input("host") final String hostIn, @Input("jbossServer") final String jbossServer,
            @Input("filename") final String filename, @Input("configFileLocationOnServer") final String configFileLocationOnServer,
            @Input("privateKeyLocation") final String privateKeyLocation, @Input("privateCertLocation") final String privateCertLocation,
            @Output("result") final boolean expected) {

        final AIWebOperator aIWebOperator = aIWebProvider.provide(AIWebOperator.class);
        setTestInfo("Send a request to the server with a Serial number of NE");

        final Host host = getHost(hostIn);
        final Host secServ = DataHandler.getHostByName("sc1jbi");

        aIWebOperator.downloadConfigFile(host, HTTPS + secServ.getOriginalIp() + jbossServer, filename, configFileLocationOnServer,
                privateKeyLocation, privateCertLocation);

        final String response = aIWebOperator.checkFor404Response(configFileLocationOnServer + "tmp/result");

        final boolean result = Integer.parseInt(response) > 0;

        assertSame(expected, result);
    }

    //    /**
    //     * @DESCRIPTION Ai-Web fails on one SC, backup on other SC takes over
    //     * @PRE AIWeb running on SC1 and SC2
    //     * @PRIORITY HIGH
    //     */
    //    @TestId(id = "TS-9_Func_12", title = "TS-9_AIWeb_HighAvailability")
    //    @Context(context = {CAIWontext.REST})
    //    @Test(groups={"{Acceptance","aiweb_regression}"})
    //    public void tS9_AIWeb_HighAvailability() {
    //
    //        AIWebOperator aIWebOperator = aIWebProvider.provide(AIWebOperator.class);
    //        setTestInfo("AIWeb on SC1 fails");
    //        //TODO VERIFY:AIWeb on SC2 sucessfully takes over
    //        throw new TestCaseNotImplementedException();
    //    }

    //    void deleteTestFilesFromServer(Host host) {
    //    	rfh =new RemoteFileHandler(host);
    //    	rfh.deleteRemoteFile(TAF_PRIV_KEY);
    //		rfh.deleteRemoteFile(TAF_DEFAULT_CERT);
    //    	rfh.deleteRemoteFile(INT_FILES_DEF_LOC+SERIALNUMBER123_ERICSSON_COM);
    //    	rfh.deleteRemoteFile(TMP_LOCATION);
    //    }

    private Host getHost(final String hostIn) {
        if (hostIn.equals("sc1")) {
            return HostConfigurator.getSC1();
        } else if (hostIn.equals("sc2")) {
            return HostConfigurator.getSC1();
        } else {
            return null;
        }
    }

}
