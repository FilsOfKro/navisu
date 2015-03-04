package bzh.terrevirtuelle.navisu.charts.raster.kap;

import bzh.terrevirtuelle.navisu.app.drivers.driver.Driver;
import org.capcaval.c3.component.ComponentService;

/**
 * NaVisu
 *
 * @author tibus
 * @date 11/11/2013 12:49
 */
public interface KapChartServices extends ComponentService {

    Driver getDriver();

    void openChart(String file);
}
