/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.controller.cmd.catalog;

import bzh.terrevirtuelle.navisu.domain.camera.model.Camera;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.S57Chart;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconCardinal;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconIsolatedDanger;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconLateral;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconSafeWater;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BeaconSpecialPurpose;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyCardinal;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyInstallation;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyIsolatedDanger;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoyLateral;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoySafeWater;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.BuoySpecialPurpose;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Landmark;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.MooringWarpingFacility;
import bzh.terrevirtuelle.navisu.domain.gpx.model.Gpx;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationData;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.Avurnav;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.AvurnavSet;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.rss.AvurnavRSS;
import bzh.terrevirtuelle.navisu.domain.navigation.avurnav.rss.Rss;
import bzh.terrevirtuelle.navisu.domain.navigation.sailingDirections.SailingDirections;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author serge
 */
@XmlType(name = "arcommand", propOrder = {
    "cmd",
    "arg"
})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ArCommand {

    private String cmd;
    @XmlElements({
        @XmlElement(name = "bcncar", type = BeaconCardinal.class),
        @XmlElement(name = "bcnisd", type = BeaconIsolatedDanger.class),
        @XmlElement(name = "bcnlat", type = BeaconLateral.class),
        @XmlElement(name = "bcnsaw", type = BeaconSafeWater.class),
        @XmlElement(name = "bcnspp", type = BeaconSpecialPurpose.class),
        @XmlElement(name = "buoycar", type = BuoyCardinal.class),
        @XmlElement(name = "buoyinb", type = BuoyInstallation.class),
        @XmlElement(name = "buoyisd", type = BuoyIsolatedDanger.class),
        @XmlElement(name = "buoylat", type = BuoyLateral.class),
        @XmlElement(name = "buoysaw", type = BuoySafeWater.class),
        @XmlElement(name = "buoyssp", type = BuoySpecialPurpose.class),
        @XmlElement(name = "morfac", type = MooringWarpingFacility.class),
        @XmlElement(name = "lndmrk", type = Landmark.class),
        @XmlElement(name = "ship", type = Ship.class),
        @XmlElement(name = "avurnav", type = Avurnav.class),
        @XmlElement(name = "sailingDirections", type = SailingDirections.class),
        @XmlElement(name = "s57Chart", type = S57Chart.class),
        @XmlElement(name = "gpx", type = Gpx.class),
        @XmlElement(name = "camera", type = Camera.class)
    })
    private NavigationData arg = null;

    public ArCommand() {
    }

    public ArCommand(String cmd, NavigationData navigationData) {
        this.cmd = cmd;
        this.arg = navigationData;

    }

    public void parse() {
        String path = getClass().getPackage().getName();
        try {
            ((NavigationCmd) (Class.forName(path + "." + cmd).newInstance())).doIt(arg);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ArCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NavigationData getArg() {
        return arg;
    }

    public void setArg(NavigationData arg) {
        this.arg = arg;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "ArCommand{" + "cmd=" + cmd + ", arg=" + arg + '}';
    }

}
