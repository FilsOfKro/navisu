/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.locators;

import bzh.terrevirtuelle.navisu.app.drivers.Driver;
import org.capcaval.c3.component.ComponentService;

/**
 *
 * @author Serge
 */
public interface Widget3DServices
        extends ComponentService {

    public void createGpsLocator();
    public void createAisLocator();

}
