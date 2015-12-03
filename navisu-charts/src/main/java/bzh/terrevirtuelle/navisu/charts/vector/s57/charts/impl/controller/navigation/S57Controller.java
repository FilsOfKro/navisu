/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.navigation;

import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationData;
import bzh.terrevirtuelle.navisu.domain.ship.model.Ship;
import bzh.terrevirtuelle.navisu.instruments.transponder.impl.controller.TransponderEventsController;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.SurfaceShape;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * NaVisu
 *
 * @date 29 sept. 2015
 * @author Serge Morvan
 */
public abstract class S57Controller
        extends TransponderEventsController {

    protected S57Behavior s57Behavior;
    protected NavigationData navigationData;
    protected WorldWindow wwd;
    protected RenderableLayer layer;
    protected SurfaceShape surveyZone;
    protected ShapeAttributes surveyZoneNormalAttributes = null;
    protected PointPlacemark pointPlacemark;
    protected double range; // distance of perception

    protected long id;
    protected double lat;
    protected double lon;
    protected boolean first = true;
    protected GeodeticCalculator geoCalc = new GeodeticCalculator();
    protected final Ellipsoid reference = Ellipsoid.WGS84;
    protected final double KM_TO_NAUTICAL = 0.53879310;
    protected final double KM_TO_METER = 1000;
    protected GlobalCoordinates waypointA;
    protected GlobalCoordinates waypointB;
    protected double distance;
    protected double azimuth;

    public S57Controller(S57Behavior s57Behavior, NavigationData navigationData, double range) {
        this.s57Behavior = s57Behavior;
        s57Behavior.setS57Controller(this);
        this.navigationData = navigationData;
        this.id = navigationData.getId();
        this.lat = navigationData.getLocation().getLat();
        this.lon = navigationData.getLocation().getLon();
        this.range = range;
        wwd = GeoWorldWindViewImpl.getWW();

        surveyZoneNormalAttributes = new BasicShapeAttributes();
        surveyZoneNormalAttributes.setDrawInterior(false);
        surveyZoneNormalAttributes.setDrawOutline(false);

        surveyZone = new SurfaceCircle(LatLon.fromDegrees(lat, lon), range);
        surveyZone.setAttributes(surveyZoneNormalAttributes);
    }

    public double getDistanceNm(Position posA, Position posB) {
        waypointA = new GlobalCoordinates(posA.getLatitude().getDegrees(), posA.getLongitude().getDegrees());
        waypointB = new GlobalCoordinates(posB.getLatitude().getDegrees(), posB.getLongitude().getDegrees());
        return geoCalc.calculateGeodeticCurve(reference, waypointA, waypointB).getEllipsoidalDistance() / KM_TO_METER * KM_TO_NAUTICAL;
    }

    public double getAzimuth(Position posA, Position posB) {
        waypointA = new GlobalCoordinates(posA.getLatitude().getDegrees(), posA.getLongitude().getDegrees());
        waypointB = new GlobalCoordinates(posB.getLatitude().getDegrees(), posB.getLongitude().getDegrees());
        return geoCalc.calculateGeodeticCurve(reference, waypointA, waypointB).getAzimuth();
    }

    public double getDistanceNm(double latA, double lonA, double latB, double lonB) {
        waypointA = new GlobalCoordinates(latA, lonA);
        waypointB = new GlobalCoordinates(latB, lonB);
        return geoCalc.calculateGeodeticCurve(reference, waypointA, waypointB).getEllipsoidalDistance() / KM_TO_METER * KM_TO_NAUTICAL;
    }

    public double getAzimuth(double latA, double lonA, double latB, double lonB) {
        waypointA = new GlobalCoordinates(latA, lonA);
        waypointB = new GlobalCoordinates(latB, lonB);
        return geoCalc.calculateGeodeticCurve(reference, waypointA, waypointB).getAzimuth();
    }

    public RenderableLayer getLayer() {
        return layer;
    }

    public void setLayer(RenderableLayer layer) {
        this.layer = layer;
    }

    public void setSurveyZone(SurfaceShape surveyZone) {
        this.surveyZone = surveyZone;
    }

    public SurfaceShape getSurveyZone() {
        return surveyZone;
    }

    public S57Behavior getS57Behavior() {
        return s57Behavior;
    }

    public void setS57Behavior(S57Behavior s57Behavior) {
        this.s57Behavior = s57Behavior;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public final double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public boolean isFirst() {
        return first;
    }

    public GeodeticCalculator getGeoCalc() {
        return geoCalc;
    }

    public void setGeoCalc(GeodeticCalculator geoCalc) {
        this.geoCalc = geoCalc;
    }

    public GlobalCoordinates getWaypointA() {
        return waypointA;
    }

    public void setWaypointA(GlobalCoordinates waypointA) {
        this.waypointA = waypointA;
    }

    public GlobalCoordinates getWaypointB() {
        return waypointB;
    }

    public void setWaypointB(GlobalCoordinates waypointB) {
        this.waypointB = waypointB;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public NavigationData getNavigationData() {
        return navigationData;
    }

    public PointPlacemark getPointPlacemark() {
        return pointPlacemark;
    }

    public void setPointPlacemark(PointPlacemark pointPlacemark) {
        this.pointPlacemark = pointPlacemark;
    }

    public void setNavigationData(NavigationData navigationData) {
        this.navigationData = navigationData;
        this.id = navigationData.getId();
        this.lat = navigationData.getLocation().getLat();
        this.lon = navigationData.getLocation().getLon();
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public abstract void activate();

    public abstract void deactivate();

    @Override
    public abstract void updateTarget(Ship ship);
}
