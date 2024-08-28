package com.ericsson.nms.security.taf.test.operators;

import com.ericsson.cifwk.taf.data.Host;

public interface AIWebOperator {

    void downloadConfigFile(Host host, String uri, String filename, String configFileLocationOnServer, String privateKeyLocation,
            String privateCertLocation);

    String checkFor404Response(String configFileLocationOnServer);
}
