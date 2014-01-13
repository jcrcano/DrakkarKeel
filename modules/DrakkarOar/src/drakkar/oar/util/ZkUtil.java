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

import java.io.Serializable;
import java.lang.reflect.Method;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.sys.ComponentsCtrl;

public class ZkUtil implements Serializable{
     private static final long serialVersionUID = 77700000000005L;
    /**
     * Este método es el encargado de mapear la notificación relizada por el servidor
     * para su despacho por el método correspondiente.
     *
     * @param object clase que implemta el listener
     * @param evt    evento
     *
     * @throws Exception
     */
    public static void mappingEvent(Object object, Event evt) throws Exception {
        final Object controller = object;
        final Method mtd = ComponentsCtrl.getEventMethod(controller.getClass(), evt.getName());

        if (mtd != null) {
            if (mtd.getParameterTypes().length == 0) {
                mtd.invoke(controller, null);
            } else if (evt instanceof ForwardEvent) {
                final Class paramcls = (Class) mtd.getParameterTypes()[0];
                if (ForwardEvent.class.isAssignableFrom(paramcls)
                        || Event.class.equals(paramcls)) {
                    mtd.invoke(controller, new Object[]{evt});
                } else {
                    do {
                        evt = ((ForwardEvent) evt).getOrigin();
                    } while (evt instanceof ForwardEvent);
                    mtd.invoke(controller, new Object[]{evt});
                }
            } else {
                mtd.invoke(controller, new Object[]{evt});
            }
        }
    }
}
