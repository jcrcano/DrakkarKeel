/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow;

import drakkar.oar.util.OutputMonitor;
import java.io.Serializable;

public class ApplicationContext implements Serializable {

    private static final long serialVersionUID = 80000000000000L;
    public static String cache;

    public static void initContext() {
        OutputMonitor.printLine("Initializing context application");
        cache = "download";

    }

}
