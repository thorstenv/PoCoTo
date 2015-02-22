//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.16 at 08:45:13 AM CET 
//


package jav.correctionBackend.alto1_4;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Describes the bounding shape of a block, if it is not rectangular.
 * 
 * <p>Java class for ShapeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShapeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Polygon" type="{}PolygonType"/>
 *         &lt;element name="Ellipse" type="{}EllipseType"/>
 *         &lt;element name="Circle" type="{}CircleType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShapeType", propOrder = {
    "polygon",
    "ellipse",
    "circle"
})
public class ShapeType {

    @XmlElement(name = "Polygon")
    protected PolygonType polygon;
    @XmlElement(name = "Ellipse")
    protected EllipseType ellipse;
    @XmlElement(name = "Circle")
    protected CircleType circle;

    /**
     * Gets the value of the polygon property.
     * 
     * @return
     *     possible object is
     *     {@link PolygonType }
     *     
     */
    public PolygonType getPolygon() {
        return polygon;
    }

    /**
     * Sets the value of the polygon property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolygonType }
     *     
     */
    public void setPolygon(PolygonType value) {
        this.polygon = value;
    }

    /**
     * Gets the value of the ellipse property.
     * 
     * @return
     *     possible object is
     *     {@link EllipseType }
     *     
     */
    public EllipseType getEllipse() {
        return ellipse;
    }

    /**
     * Sets the value of the ellipse property.
     * 
     * @param value
     *     allowed object is
     *     {@link EllipseType }
     *     
     */
    public void setEllipse(EllipseType value) {
        this.ellipse = value;
    }

    /**
     * Gets the value of the circle property.
     * 
     * @return
     *     possible object is
     *     {@link CircleType }
     *     
     */
    public CircleType getCircle() {
        return circle;
    }

    /**
     * Sets the value of the circle property.
     * 
     * @param value
     *     allowed object is
     *     {@link CircleType }
     *     
     */
    public void setCircle(CircleType value) {
        this.circle = value;
    }

}
