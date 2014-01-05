package bzh.terrevirtuelle.navisu.app.drivers.grib;

import bzh.terrevirtuelle.navisu.app.drivers.Driver;
import bzh.terrevirtuelle.navisu.app.drivers.grib.impl.view.GribLayer;
import org.capcaval.c3.component.ComponentService;

/**
 * User: jordan
 * Date: 23/11/2013
 */
public interface GribServices extends ComponentService {

    Driver getDriver();

    void loadFile(String path);

    double[] getVelocityInMPSAtPoint(double latitude, double longitude);

    double getPressionAtPoint(double latitude, double longitude);

    double getLatitudeDimension();

    double getLongitudeDimension();

    double getTimeDimension();

    GribLayer getGribLayer();
}
