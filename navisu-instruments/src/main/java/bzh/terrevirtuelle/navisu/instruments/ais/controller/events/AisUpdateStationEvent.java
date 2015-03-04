/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.ais.controller.events;

import bzh.terrevirtuelle.navisu.domain.devices.model.BaseStation;
import org.capcaval.c3.component.ComponentEvent;

/**
 * @date 2 mars 2015
 * @author Serge Morvan
 */
public interface AisUpdateStationEvent extends ComponentEvent {

    public void notifyAisMessageChanged(BaseStation updatedData);

}
