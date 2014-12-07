 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.loader;

import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.loader.ShapefileLoader;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.parameters.CATCAM;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.geo.Buoyage;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.parameters.BUOYAGE;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.parameters.CATLAM;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.parameters.CATSPM;
import bzh.terrevirtuelle.navisu.util.Pair;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.formats.shapefile.Shapefile;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecordPoint;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Serge Morvan
 * @date 4 juin 2014 NaVisu project
 */
public class BUOYAGE_ShapefileLoader
        extends LayerShapefileLoader {

    private Buoyage object;
    private final List<Buoyage> objects;
    private PointPlacemarkAttributes attrs;
    private Set<Entry<String, Object>> entries;
    private Class claz;
    private final String acronym;
    private final Map<Pair, String> topMarks;
    private final String marsys;

    public BUOYAGE_ShapefileLoader( String path, Map<Pair, String> topMarks, String marsys, String acronym) {
        this.topMarks = topMarks;
        this.marsys = marsys;
        this.acronym = acronym;
        String className = BUOYAGE.ATT.get(acronym);
        try {
            claz = Class.forName(path + "." + className);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BUOYAGE_ShapefileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        objects = new ArrayList<>();

    }

    @Override
    protected void addRenderablesForPoints(Shapefile shp, RenderableLayer layer) {

        while (shp.hasNext()) {
            ShapefileRecord record = shp.nextRecord();

            if (!Shapefile.isPointType(record.getShapeType())) {
                continue;
            }
            attrs = this.createPointAttributes(record);
            double[] point = ((ShapefileRecordPoint) record).getPoint();
            layer.addRenderable(this.createPoint(record, point[1], point[0], attrs));
        }
    }

    @Override
    protected PointPlacemarkAttributes createPointAttributes(ShapefileRecord record) {
        PointPlacemarkAttributes normalAttributes = new PointPlacemarkAttributes();
        return normalAttributes;
    }

    @Override
    protected Renderable createPoint(ShapefileRecord record, double latDegrees, double lonDegrees,
            PointPlacemarkAttributes attrs) {

        try {
            object = (Buoyage) claz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BUOYAGE_ShapefileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        objects.add(object);
        entries = record.getAttributes().getEntries();

        object.setLat(latDegrees);
        object.setLon(lonDegrees);
        //   String mark = null;
        entries.stream().forEach((e) -> {
            if (e.getKey().equals("RCID")) {
                object.setId((Long) e.getValue());
            }
            if (e.getKey().equals("BCNSHP") || e.getKey().equals("BOYSHP")) {
                Object obj = e.getValue();
                String shp = "0";
                if (obj != null) {
                    shp = ((Long) e.getValue()).toString();
                }
                object.setShape(shp);
            }
            if (e.getKey().equals("CATCAM") || e.getKey().equals("CATLAM") || e.getKey().equals("CAT")) {
                if (e.getValue() != null) {
                    object.setCategoryOfMark(((Long) e.getValue()).toString());
                } else {
                    object.setCategoryOfMark("0");
                }
            }
            if (e.getKey().equals("CATSPM")) {
                if (e.getValue() != null) {
                    object.setCategoryOfMark((String) e.getValue());
                } else {
                    object.setCategoryOfMark("0");
                }
            }
            if (e.getKey().equals("OBJNAM")) {
                if (e.getValue() != null) {
                    object.setObjectName((String) e.getValue());
                } else {
                    object.setObjectName(" ");
                }
            }
            if (e.getKey().equals("COLOUR")) {
                if (e.getValue() != null) {
                    object.setColour((String) e.getValue());
                } else {
                    object.setColour("0");
                }
            }
            if (e.getKey().equals("COLPAT")) {
                Object obj = e.getValue();
                String colpat = "0";
                if (obj != null) {
                    colpat = (String) e.getValue();
                }
                object.setColourPattern(colpat);
            }
            if (e.getKey().equals("STATUS")) {
                object.setStatus((String) e.getValue());
            }
            if (e.getKey().equals("PICREP")) {
                object.setPictorialRepresentation((String) e.getValue());
            }
            if (e.getKey().equals("DATEND")) {
                object.setDateEnd((String) e.getValue());
            }
            if (e.getKey().equals("DATSTA")) {
                object.setDateStart((String) e.getValue());
            }
        });

        PointPlacemark placemark = new PointPlacemark(Position.fromDegrees(latDegrees, lonDegrees, 0));
        placemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        //   placemark.setLabelText(object.getObjectName());

        String catMark = "";
        if (acronym.contains("CAR")) {
            catMark = CATCAM.ATT.get(object.getCategoryOfMark());
        } else {
            if (acronym.contains("LAT")) {
                catMark = CATLAM.ATT.get(object.getCategoryOfMark());
            } else {
                if (acronym.contains("SPP")) {
                    catMark = CATSPM.ATT.get(object.getCategoryOfMark());
                } else {
                    if (acronym.contains("ISD")) {
                        catMark = "0";
                    }
                }
            }
        }
        /*
         String label = claz.getSimpleName() + " "
         + catMark + "\n"
         + (object.getObjectName() != null ? object.getObjectName() : "") + "\n"
         + "Lat : " + new Float(object.getLat()).toString() + "\n "
         + "Lon : " + new Float(object.getLon()).toString();

         placemark.setValue(AVKey.DISPLAY_NAME, label);
         */
        String tm = topMarks.get(new Pair(latDegrees, lonDegrees));
        if (tm == null) {
            tm = "0";
        }
        attrs.setImageAddress("img/buoyage_" + marsys + "/" + acronym + "_"
                + object.getShape() + "_"
                + object.getCategoryOfMark() + "_"
                + object.getColour() + "_"
                + object.getColourPattern() + "_"
                + tm
                + "_" + marsys
                + ".png");
        attrs.setImageOffset(Offset.BOTTOM_CENTER);
        attrs.setScale(0.9);
        placemark.setAttributes(attrs);

        String label = acronym + "_"
                + object.getShape() + "_"
                + object.getCategoryOfMark() + "_"
                + object.getColour() + "_"
                + object.getColourPattern() + "_"
                + tm
                + "_" + marsys
                + ".png";
        //System.out.println("label : " + label);
        placemark.setValue(AVKey.DISPLAY_NAME, label);

        return placemark;
    }

    public List<Buoyage> getBeacons() {
        return objects;
    }

}
