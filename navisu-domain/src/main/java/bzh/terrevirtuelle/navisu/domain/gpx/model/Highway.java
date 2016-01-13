//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.03 at 09:49:42 AM CET 
//
package bzh.terrevirtuelle.navisu.domain.gpx.model;

import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Location;
import bzh.terrevirtuelle.navisu.domain.navigation.NavigationData;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "highway", propOrder = {"id", "name", "description", "latitude", "longitude", "geometry"})
public class Highway
        extends Location
        implements NavigationData {

    protected String name;

    private String description;

    public Highway() {
    }

    public Highway(long id) {
        super(id);
    }

    public Highway(long id, String geometry) {
        super(id, geometry);
    }

    public Highway(String name, long id, String geometry) {
        super(id, geometry);
        this.name = name;
    }

    public Highway(String name, String description, long id) {
        super(id);
        this.name = name;
        this.description = description;
    }

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Highway{" + "name=" + name + super.toString() + '}';
    }

}