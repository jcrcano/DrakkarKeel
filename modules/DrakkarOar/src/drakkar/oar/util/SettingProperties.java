/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingProperties {

    private static String path = "./config/properties.xml";
    /**
     *
     */
    public static boolean ACTIVE_MONITOR_PROPERTY = true;
    /**
     * 
     */
    public static boolean CONSOLE_OUTPUT_PROPERTY = true;
    /**
     *
     */
    public static boolean LOG_FILE_OUTPUT_PROPERTY = false;

    /**
     * 
     * @throws Exception
     */
    public static void loadSettingProperies() throws Exception {
        File f = new File(path);

        if (f.exists()) {
            XMLParser config = XMLParser.getInstance(f);

            String monitor = config.getValue("ACTIVE_MONITOR");
            ACTIVE_MONITOR_PROPERTY = (monitor != null) ? Boolean.valueOf(monitor) : true;

            if (ACTIVE_MONITOR_PROPERTY) {

                String console = config.getValue("CONSOLE_OUTPUT");
                CONSOLE_OUTPUT_PROPERTY = (console != null) ? Boolean.valueOf(console) : true;

                String log_file = config.getValue("LOG_FILE_OUTPUT");
                LOG_FILE_OUTPUT_PROPERTY = (log_file != null) ? Boolean.valueOf(log_file) : false;

            }
        } else {
            List<Parameter> params = new ArrayList<>(7);
            params.add(new Parameter("ACTIVE_MONITOR", true));
            params.add(new Parameter("CONSOLE_OUTPUT", true));
            params.add(new Parameter("LOG_FILE_OUTPUT", false));


            XMLParser.createXMLConfig(path, params);

            loadSettingProperies();


        }



    }

    /**
     * 
     * @param path
     */
    public static void loadSettingProperies(String path) throws Exception {
        SettingProperties.path = path;
        loadSettingProperies();
    }

    public static void activeDefaultMonitor() {
        ACTIVE_MONITOR_PROPERTY = true;
        CONSOLE_OUTPUT_PROPERTY = true;
    }
}
