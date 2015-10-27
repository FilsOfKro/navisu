//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2015.10.27 à 06:10:03 PM CET 
//


package bzh.terrevirtuelle.navisu.domain.photos.exif;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Classe Java pour sensingMethodType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="sensingMethodType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://xmlns.oracle.com/ord/meta/exif>sensingMethod_t">
 *       &lt;attGroup ref="{http://xmlns.oracle.com/ord/meta/exif}exifAttrs"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sensingMethodType", propOrder = {
    "value"
})
public class SensingMethodType {

    @XmlValue
    protected SensingMethodT value;
    @XmlAttribute(name = "tag", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger tag;

    /**
     * Obtient la valeur de la propriété value.
     * 
     * @return
     *     possible object is
     *     {@link SensingMethodT }
     *     
     */
    public SensingMethodT getValue() {
        return value;
    }

    /**
     * Définit la valeur de la propriété value.
     * 
     * @param value
     *     allowed object is
     *     {@link SensingMethodT }
     *     
     */
    public void setValue(SensingMethodT value) {
        this.value = value;
    }

    /**
     * Obtient la valeur de la propriété tag.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTag() {
        return tag;
    }

    /**
     * Définit la valeur de la propriété tag.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTag(BigInteger value) {
        this.tag = value;
    }

}
