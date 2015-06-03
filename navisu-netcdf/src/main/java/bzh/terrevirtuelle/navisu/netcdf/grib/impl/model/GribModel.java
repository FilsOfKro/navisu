package bzh.terrevirtuelle.navisu.netcdf.grib.impl.model;

import bzh.terrevirtuelle.navisu.netcdf.grib.GribConstants;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ucar.ma2.Array;
import ucar.nc2.Dimension;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.dt.grid.GridDataset;

/**
 * User: jordan Date: 24/11/2013
 */
public class GribModel {

    protected final static Logger LOGGUER = Logger.getLogger(GribModel.class.getName());

    protected GridDataset gridDataset;

    protected GeoGrid pressureGrid;

    protected GeoGrid uGrid;

    protected GeoGrid vGrid;
    protected double module;
    protected double angle;
    protected int timeDimension;
    protected int longitudeDimension;
    protected int latitudeDimension;

    protected double topLeftLatitude;
    protected double topLeftLongitude;
    protected double bottomRightLatitude;
    protected double bottomRightLongitude;

    protected double longitudeGap;
    protected double latitudeGap;

    public GribModel(GridDataset gridDataset, GeoGrid pressureGrid, GeoGrid uGrid, GeoGrid vGrid) {

        this.gridDataset = gridDataset;
        this.pressureGrid = pressureGrid;
        this.uGrid = uGrid;
        this.vGrid = vGrid;
        this.initDimensions();

        this.initBoundingBox();

        this.computeGridGap();
    }

    //Todo Important : Do Some Tests !!
    private void computeGridGap() {
        this.longitudeGap = (Math.abs(this.topLeftLongitude) + Math.abs(this.bottomRightLongitude)) / (this.longitudeDimension - 1);
        this.latitudeGap = (Math.abs(this.topLeftLatitude) - Math.abs(this.bottomRightLatitude)) / (this.latitudeDimension - 1);
    }

    private void initBoundingBox() {
        this.topLeftLatitude = this.gridDataset.getBoundingBox().getUpperLeftPoint().getLatitude();
        this.topLeftLongitude = this.gridDataset.getBoundingBox().getUpperLeftPoint().getLongitude();
        this.bottomRightLatitude = this.gridDataset.getBoundingBox().getLowerRightPoint().getLatitude();
        this.bottomRightLongitude = this.gridDataset.getBoundingBox().getLowerRightPoint().getLongitude();
    }

    private void initDimensions() {
        String fullName;
        for (Dimension dimension : this.gridDataset.getNetcdfDataset().getDimensions()) {

            fullName = dimension.getFullName().toLowerCase();
            if (fullName.contains(GribConstants.TIME_DIMENSION_NAME)) {
                this.timeDimension = dimension.getLength();
            } else if (fullName.contains(GribConstants.LAT_DIMENSION_NAME)) {
                this.latitudeDimension = dimension.getLength();
            } else if (fullName.contains(GribConstants.LON_DIMENSION_NAME)) {
                this.longitudeDimension = dimension.getLength();
            }
        }
    }

    public double[] getVelocityVectorFromLatLon(double latitude, double longitude, int time) {

        Array uGridArray = null;
        Array vGridArray = null;

        try {
            uGridArray = this.uGrid.readYXData(time, 0);
            vGridArray = this.vGrid.readYXData(time, 0);
        } catch (IOException ex) {
            Logger.getLogger(GribModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        int[] uIndex = this.uGrid.getCoordinateSystem().findXYindexFromLatLon(latitude, longitude, null);
        int[] vIndex = this.vGrid.getCoordinateSystem().findXYindexFromLatLon(latitude, longitude, null);

        //  LOGGUER.info("#####" + uIndex[0] + "#####" + uIndex[1]);
        //  LOGGUER.info("#####" + vIndex[0] + "#####" + vIndex[1]);
        double uValue = uGridArray.getDouble(uIndex[1] * longitudeDimension + uIndex[0]);
        double vValue = vGridArray.getDouble(vIndex[1] * latitudeDimension + vIndex[0]);

        module = Math.sqrt((uValue * uValue) + (vValue * vValue));
        angle = Math.atan(vValue / uValue);

        if ((uValue < 0 && vValue > 0) || (uValue < 0 && vValue < 0)) {
            angle += 180;
        }

        return new double[]{module, angle};
    }

    public double getVelocity(double latitude, double longitude, int time) {
        double[] param = getVelocityVectorFromLatLon(latitude, longitude, time);
        return param[0];
    }

    public double getDirection(double latitude, double longitude, int time) {
        double[] param = getVelocityVectorFromLatLon(latitude, longitude, time);
        return param[1];
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

    public double getTopLeftLatitude() {
        return topLeftLatitude;
    }

    public double getTopLeftLongitude() {
        return topLeftLongitude;
    }

    public double getBottomRightLatitude() {
        return bottomRightLatitude;
    }

    public double getBottomRightLongitude() {
        return bottomRightLongitude;
    }

    public double getLongitudeGap() {
        return longitudeGap;
    }

    public double getLatitudeGap() {
        return latitudeGap;
    }

    @Override
    public String toString() {
        return "TimeDimension : " + this.timeDimension + " "
                + "LatitudeDimension : " + this.latitudeDimension + " "
                + "LongitudeDimension : " + this.longitudeDimension + " "
                + "BoundingBox : [(" + this.topLeftLatitude + "," + this.topLeftLongitude + "),("
                + this.bottomRightLatitude + "," + this.bottomRightLongitude + ")]" + " "
                + "Latitude Gap : " + this.latitudeGap + " "
                + "Longitude Gap : " + this.longitudeGap;
    }
}
