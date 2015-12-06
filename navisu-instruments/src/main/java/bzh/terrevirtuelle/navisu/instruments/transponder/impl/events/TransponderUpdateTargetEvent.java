/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.instruments.transponder.impl.events;

import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import org.capcaval.c3.component.ComponentEvent;

/**
 * @date 2 mars 2015
 * @author Serge Morvan
 */
public interface TransponderUpdateTargetEvent
        extends ComponentEvent {

    public void notifyTransponderMessageChanged(Ship updatedData);

}
