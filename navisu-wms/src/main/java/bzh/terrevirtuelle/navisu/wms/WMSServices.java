/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.wms;

import bzh.terrevirtuelle.navisu.app.drivers.driver.Driver;
import org.capcaval.c3.component.ComponentService;

/**
 * @date 4 mars 2015
 * @author Serge Morvan
 */
public interface WMSServices 
extends ComponentService {

    Driver getDriver();

    void load(String server);

}
