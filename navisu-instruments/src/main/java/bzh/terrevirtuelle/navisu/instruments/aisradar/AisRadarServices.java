/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.aisradar;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import org.capcaval.c3.component.ComponentService;

/**
 *
 * @author Serge
 */
public interface AisRadarServices
        extends ComponentService {

    void on();

    default void off() {
    }

    boolean canOpen(String category);

    InstrumentDriver getDriver();
}
