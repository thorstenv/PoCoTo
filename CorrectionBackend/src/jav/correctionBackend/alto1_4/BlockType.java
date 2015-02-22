//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.16 at 08:45:13 AM CET 
//


package jav.correctionBackend.alto1_4;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Base type for any kind of block on the page.
 * 
 * <p>Java class for BlockType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BlockType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="Shape" type="{}ShapeType"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}simpleLink"/>
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="STYLEREFS" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
 *       &lt;attribute name="HEIGHT" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="WIDTH" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="HPOS" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="VPOS" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="ROTATION" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="IDNEXT" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BlockType", propOrder = {
    "shape"
})
@XmlSeeAlso({
    TextBlockType.class,
    GraphicalElementType.class,
    ComposedBlockType.class,
    IllustrationType.class
})
public class BlockType {

    @XmlElement(name = "Shape")
    protected ShapeType shape;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "STYLEREFS")
    @XmlIDREF
    @XmlSchemaType(name = "IDREFS")
    protected List<Object> stylerefs;
    @XmlAttribute(name = "HEIGHT", required = true)
    protected int height;
    @XmlAttribute(name = "WIDTH", required = true)
    protected int width;
    @XmlAttribute(name = "HPOS", required = true)
    protected int hpos;
    @XmlAttribute(name = "VPOS", required = true)
    protected int vpos;
    @XmlAttribute(name = "ROTATION")
    protected Float rotation;
    @XmlAttribute(name = "IDNEXT")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object idnext;
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/1999/xlink")
    protected String type;
    @XmlAttribute(name = "href", namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(name = "role", namespace = "http://www.w3.org/1999/xlink")
    protected String role;
    @XmlAttribute(name = "arcrole", namespace = "http://www.w3.org/1999/xlink")
    protected String arcrole;
    @XmlAttribute(name = "title", namespace = "http://www.w3.org/1999/xlink")
    protected String title;
    @XmlAttribute(name = "show", namespace = "http://www.w3.org/1999/xlink")
    protected String show;
    @XmlAttribute(name = "actuate", namespace = "http://www.w3.org/1999/xlink")
    protected String actuate;

    /**
     * Gets the value of the shape property.
     * 
     * @return
     *     possible object is
     *     {@link ShapeType }
     *     
     */
    public ShapeType getShape() {
        return shape;
    }

    /**
     * Sets the value of the shape property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShapeType }
     *     
     */
    public void setShape(ShapeType value) {
        this.shape = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the stylerefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stylerefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSTYLEREFS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getSTYLEREFS() {
        if (stylerefs == null) {
            stylerefs = new ArrayList<Object>();
        }
        return this.stylerefs;
    }

    /**
     * Gets the value of the height property.
     * 
     */
    public int getHEIGHT() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     */
    public void setHEIGHT(int value) {
        this.height = value;
    }

    /**
     * Gets the value of the width property.
     * 
     */
    public int getWIDTH() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     */
    public void setWIDTH(int value) {
        this.width = value;
    }

    /**
     * Gets the value of the hpos property.
     * 
     */
    public int getHPOS() {
        return hpos;
    }

    /**
     * Sets the value of the hpos property.
     * 
     */
    public void setHPOS(int value) {
        this.hpos = value;
    }

    /**
     * Gets the value of the vpos property.
     * 
     */
    public int getVPOS() {
        return vpos;
    }

    /**
     * Sets the value of the vpos property.
     * 
     */
    public void setVPOS(int value) {
        this.vpos = value;
    }

    /**
     * Gets the value of the rotation property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getROTATION() {
        return rotation;
    }

    /**
     * Sets the value of the rotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setROTATION(Float value) {
        this.rotation = value;
    }

    /**
     * Gets the value of the idnext property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getIDNEXT() {
        return idnext;
    }

    /**
     * Sets the value of the idnext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setIDNEXT(Object value) {
        this.idnext = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "simple";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the arcrole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * Sets the value of the arcrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArcrole(String value) {
        this.arcrole = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the show property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShow() {
        return show;
    }

    /**
     * Sets the value of the show property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShow(String value) {
        this.show = value;
    }

    /**
     * Gets the value of the actuate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActuate() {
        return actuate;
    }

    /**
     * Sets the value of the actuate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActuate(String value) {
        this.actuate = value;
    }

}
