package com.ericsson.nms.security.taf.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.Host;

@Operator(context = Context.API)
public class AIWebApiOperator implements AIWebOperator {

    @Override
    public void downloadConfigFile(final Host host, final String uri, final String filename, final String configFileLocationOnServer,
            final String privateKeyLocation, final String privateCertLocatio) {
    }

    @Override
    public String checkFor404Response(final String configFileLocationOnServer) {
        return null;
    }

}
