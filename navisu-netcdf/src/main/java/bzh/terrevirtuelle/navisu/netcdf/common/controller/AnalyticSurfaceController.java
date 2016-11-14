/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package bzh.terrevirtuelle.navisu.netcdf.common.controller;

import bzh.terrevirtuelle.navisu.core.util.analytics.AnalyticSurface;
import bzh.terrevirtuelle.navisu.core.util.analytics.AnalyticSurfaceAttributes;
import bzh.terrevirtuelle.navisu.core.util.analytics.AnalyticSurfaceLegend;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.geom.Extent;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.BufferFactory;
import gov.nasa.worldwind.util.BufferWrapper;
import gov.nasa.worldwind.util.WWMath;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.util.ArrayList;

public class AnalyticSurfaceController {

    protected int lonDimension;
    protected int latDimension;
    double minLat;
    double maxLat;
    double minLon;
    double maxLon;
    protected double minValue;
    protected double maxValue;
    protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;
    protected RenderableLayer analyticSurfaceLayer;
    protected RenderableLayer legendLayer;
    protected double[] values;
    protected AnalyticSurfaceAttributes attr;
    protected AnalyticSurface surface;
    private final WorldWindow wwd;
    private static boolean first = true;
    private LegendController legendController;

    public AnalyticSurfaceController(
            RenderableLayer layer,
            RenderableLayer legendLayer,
            final double[] values,
            int latDimension, int lonDimension,
            double minLat, double maxLat,
            double minLon, double maxLon,
            double minValue, double maxValue,
            double opacity,
            String legendTitle, String units) {

        this.analyticSurfaceLayer = layer;
        this.legendLayer = legendLayer;

        this.legendLayer.setPickEnabled(true);
        this.values = values;
        this.lonDimension = lonDimension;
        this.latDimension = latDimension;
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
        this.minValue = minValue;
        this.maxValue = maxValue;
        wwd = GeoWorldWindViewImpl.getWW();
        this.analyticSurfaceLayer.setPickEnabled(true);
        wwd.addSelectListener((SelectEvent event) -> {
            if (event.getEventAction().equals(SelectEvent.DRAG)) {
                Object o = event.getTopObject();
                if (o.getClass().getName().equalsIgnoreCase("gov.nasa.worldwind.render.ScreenImage")) {
                    ((gov.nasa.worldwind.render.ScreenImage) o).setScreenLocation(new Point(event.getMouseEvent().getX(),
                            event.getMouseEvent().getY()));
                    event.consume();
                }
            }
        });
//        legendController = new LegendController(surface, minValue, legend);
        apply(values);
        /*
        createSurface(values, HUE_BLUE, HUE_RED, this.lonDimension, this.latDimension,
                minLat, maxLat, minLon, maxLon,
                this.analyticSurfaceLayer, this.legendLayer,
                minValue, maxValue);
         */
    }

    public void apply(final double[] values) {
        if (surface != null) {
            analyticSurfaceLayer.removeRenderable(surface);
        }
        surface = createSurface(values, HUE_BLUE, HUE_RED, this.lonDimension, this.latDimension,
                minLat, maxLat, minLon, maxLon,
                this.analyticSurfaceLayer, this.legendLayer,
                minValue, maxValue);
        analyticSurfaceLayer.addRenderable(surface);
    }

    protected final AnalyticSurface createSurface(double[] values, double minHue, double maxHue,
            int width, int height,
            double minLat, double maxLat, double minLon, double maxLon,
            RenderableLayer outLayer, RenderableLayer legendLayer,
            double minValue, double maxValue) {
        surface = new AnalyticSurface();
        surface.setSector(Sector.fromDegrees(minLat, maxLat, minLon, maxLon));
        surface.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        surface.setDimensions(width, height);
        surface.setClientLayer(outLayer);
        //outLayer.addRenderable(surface);

        BufferWrapper firstBuffer = createBufferValues(values, width, height, minValue, maxValue, new BufferFactory.DoubleBufferFactory(), outLayer);
        BufferWrapper secondBuffer = createBufferValues(values, width, height, minValue, maxValue, new BufferFactory.DoubleBufferFactory(), outLayer);
        mixValuesOverTime(2000L, firstBuffer, secondBuffer, minValue, 18, minHue, maxHue, surface);

        attr = new AnalyticSurfaceAttributes();
        attr.setDrawShadow(false);
        attr.setInteriorOpacity(1.0);
        attr.setDrawOutline(false);
        surface.setSurfaceAttributes(attr);

        if (first == true) {
            first = false;
            Format legendLabelFormat = new DecimalFormat("# m/s") {
                @Override
                public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition) {
                    return super.format(number, result, fieldPosition);
                }
            };
            /*
            AnalyticSurfaceLegend legend = AnalyticSurfaceLegend.fromColorGradient(minValue, 40, minHue, maxHue,
                    AnalyticSurfaceLegend.createDefaultColorGradientLabels(minValue, 40, legendLabelFormat),
                    AnalyticSurfaceLegend.createDefaultTitle("Speed"));
            legend.setOpacity(0.8);
            legend.setScreenLocation(new Point(900, 300));
            legendLayer.addRenderable(createLegendRenderable(surface, maxValue, legend));
             */
        }
        return surface;
    }

    protected void mixValuesOverTime(
            final long timeToMix,
            final BufferWrapper firstBuffer, final BufferWrapper secondBuffer,
            final double minValue, final double maxValue, final double minHue, final double maxHue,
            final AnalyticSurface surface) {

        surface.setValues(createMixedColorGradientGridValues(
                2L, firstBuffer, secondBuffer, minValue, maxValue, minHue, maxHue));

    }

    public Iterable<? extends AnalyticSurface.GridPointAttributes> createMixedColorGradientGridValues(double a,
            BufferWrapper firstBuffer, BufferWrapper secondBuffer, double minValue, double maxValue,
            double minHue, double maxHue) {
        ArrayList<AnalyticSurface.GridPointAttributes> attributesList = new ArrayList<>();

        long length = Math.min(firstBuffer.length(), secondBuffer.length());
        for (int i = 0; i < length; i++) {
            double value = WWMath.mixSmooth(a, firstBuffer.getDouble(i), secondBuffer.getDouble(i));
            attributesList.add(
                    AnalyticSurface.createColorGradientAttributes(value, minValue, maxValue, minHue, maxHue));
        }
        return attributesList;
    }

    public BufferWrapper createBufferValues(double[] values, int width, int height, double min, double max,
            BufferFactory factory, RenderableLayer outLayer) {

        BufferWrapper wrapper = factory.newBuffer(values.length);
        wrapper.putDouble(0, values, 0, values.length);
        return wrapper;
    }

    /*
    protected static Renderable createLegendRenderable(final AnalyticSurface surface,
            final double surfaceMinScreenSize,
            final AnalyticSurfaceLegend legend) {
        return (DrawContext dc) -> {
            Extent extent = surface.getExtent(dc);
            if (!extent.intersects(dc.getView().getFrustumInModelCoordinates())) {
                return;
            }
            if (WWMath.computeSizeInWindowCoordinates(dc, extent) < surfaceMinScreenSize) {
                return;
            }
            legend.render(dc);
        };
    }
     */
    public AnalyticSurfaceAttributes getAttr() {
        return attr;
    }

    public AnalyticSurface getSurface() {
        return surface;
    }

}
