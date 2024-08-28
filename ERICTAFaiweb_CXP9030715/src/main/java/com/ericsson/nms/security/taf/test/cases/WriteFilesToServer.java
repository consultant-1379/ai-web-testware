package com.ericsson.nms.security.taf.test.cases;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;

public abstract class WriteFilesToServer {

    private static final String SERIALNUMBER123_ERICSSON_COM = "SERIALNUMBER123.ericsson.com";
    private static final String SERIALNUMBER124_ERICSSON_COM = "SERIALNUMBER124.ericsson.com";
    private static final String INT_FILES_DEF_LOC = "/ericsson/tor/data/autointegration/files/";
    private static final String TMP_LOCATION = INT_FILES_DEF_LOC + "tmp/";
    private static final String TAF_PRIV_KEY = TMP_LOCATION + "aiweb_taf_key.priv";
    private static final String TAF_DEFAULT_CERT = TMP_LOCATION + "aiweb_taf_SERIALNUMBER123.crt";
    private static final String TAF_DEFAULT_CERT_124 = TMP_LOCATION + "aiweb_taf_SERIALNUMBER124.crt";
    private static final String TAF_DEFAULT_CERT_ERICSSON123 = TMP_LOCATION + "aiweb_taf_SERIALNUMBER123.ericsson.com.crt";
    private static final String TAF_DEFAULT_CERT_ERICSSON124 = TMP_LOCATION + "aiweb_taf_SERIALNUMBER124.ericsson.com.crt";
    private static final String TAF_DEFAULT_CERT_ERICSSON50 = TMP_LOCATION + "aiweb_taf_SERIALNUMBER50.crt";

    private static final String SOURCE_KEY_FOLDER = "src/main/resources/keys/";
    private static final String CONFIG_FILES_FOLDER = "src/main/resources/configFiles/";

    private static RemoteFileHandler rfh;

    public static void copyRequiredFilesToServer(final Host host) {
        rfh = new RemoteFileHandler(host);
        rfh.copyLocalFileToRemote(CONFIG_FILES_FOLDER + SERIALNUMBER123_ERICSSON_COM, INT_FILES_DEF_LOC);
        rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_key.priv", TAF_PRIV_KEY);
        rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_SERIALNUMBER123.crt", TAF_DEFAULT_CERT);
        rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_SERIALNUMBER123.ericsson.com.crt", TAF_DEFAULT_CERT_ERICSSON123);
        rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_SERIALNUMBER124.crt", TAF_DEFAULT_CERT_124);
        rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_SERIALNUMBER124.ericsson.com.crt", TAF_DEFAULT_CERT_ERICSSON124);
        rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_SERIALNUMBER50.crt", TAF_DEFAULT_CERT_ERICSSON50);

        rfh.copyLocalFileToRemote(CONFIG_FILES_FOLDER + SERIALNUMBER124_ERICSSON_COM, INT_FILES_DEF_LOC);

        for (int i = 0; i < 11; i++) {
            rfh.copyLocalFileToRemote(SOURCE_KEY_FOLDER + "aiweb_taf_SERIALNUMBER" + i + ".crt", TMP_LOCATION + "aiweb_taf_SERIALNUMBER" + i + ".crt");
            rfh.copyLocalFileToRemote(CONFIG_FILES_FOLDER + "aiweb_taf_SERIALNUMBER" + i + ".ericsson.com", INT_FILES_DEF_LOC);
        }

    }

}
