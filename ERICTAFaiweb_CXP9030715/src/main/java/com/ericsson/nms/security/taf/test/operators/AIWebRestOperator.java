package com.ericsson.nms.security.taf.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;

@Operator(context = Context.REST)
public class AIWebRestOperator implements AIWebOperator {

    private SshRemoteCommandExecutor executor;

    @Override
    public void downloadConfigFile(final Host host, final String uri, final String filename, final String configFileLocationOnServer,
            final String privateKeyLocation, final String privateCertLocation) {
        executor = new SshRemoteCommandExecutor(host);
        executor.simplExec("curl -v -k --key " + privateKeyLocation + " --cert " + privateCertLocation + " " + uri + filename + " > "
                + configFileLocationOnServer + "tmp/" + "result");
    }

    @Override
    public String checkFor404Response(final String resultFileFromCurlRequest) {
        return executor.simplExec("grep -c 404 " + resultFileFromCurlRequest);
    }

}
