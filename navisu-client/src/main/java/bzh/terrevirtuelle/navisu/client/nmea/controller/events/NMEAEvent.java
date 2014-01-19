/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.client.nmea.controller.events;

import bzh.terrevirtuelle.navisu.nmea.model.NMEA;
import org.capcaval.c3.component.ComponentEvent;

/**
 *
 * @author Serge
 */
public interface NMEAEvent
        extends ComponentEvent {

    /**
     *
     * @param <T>
     * @param data
     */
    public <T extends NMEA> void notifyNmeaMessageChanged(T data);
}
