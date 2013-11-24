package bzh.terrevirtuelle.navisu.app.grib.impl.model;

import bzh.terrevirtuelle.navisu.app.grib.GribConstants;
import ucar.nc2.Dimension;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.dt.grid.GridDataset;

/**
 * User: jordan
 * Date: 24/11/2013
 */
public class GribModel {

    protected GridDataset gridDataset;

    protected GeoGrid pressureGrid;

    protected GeoGrid uGrid;

    protected GeoGrid vGrid;

    protected int timeDimension;

    protected int longitudeDimension;

    protected int latitudeDimension;


    public GribModel(GridDataset gridDataset, GeoGrid pressureGrid, GeoGrid uGrid, GeoGrid vGrid) {

        this.gridDataset = gridDataset;
        this.pressureGrid = pressureGrid;
        this.uGrid = uGrid;
        this.vGrid = vGrid;

        this.initDimensions();
    }

    private void initDimensions() {
        String fullName;
        for(Dimension dimension : this.gridDataset.getNetcdfDataset().getDimensions()) {

            fullName = dimension.getFullName().toLowerCase();
            if(fullName.contains(GribConstants.TIME_DIMENSION_NAME)) {
                this.timeDimension = dimension.getLength();
            }

            else if(fullName.contains(GribConstants.LAT_DIMENSION_NAME)) {
                this.latitudeDimension = dimension.getLength();
            }

            else if(fullName.contains(GribConstants.LON_DIMENSION_NAME)) {
                this.longitudeDimension = dimension.getLength();
            }
        }
    }


    public int getTimeDimension() {
        return timeDimension;
    }

    public int getLongitudeDimension() {
        return longitudeDimension;
    }

    public int getLatitudeDimension() {
        return latitudeDimension;
    }

    @Override
    public String toString() {
        return "TimeDimension : " + this.timeDimension +
                "LatitudeDimension : " + this.latitudeDimension +
                "LongitudeDimension : " + this.longitudeDimension;
    }
}
