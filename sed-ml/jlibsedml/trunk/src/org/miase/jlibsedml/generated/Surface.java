//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.01.07 at 12:53:51 PM GMT 
//


package org.miase.jlibsedml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="yDataReference" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="xDataReference" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zDataReference" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="logY" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="logX" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="logZ" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "surface")
public class Surface {

    @XmlAttribute(required = true)
    protected String yDataReference;
    @XmlAttribute(required = true)
    protected String xDataReference;
    @XmlAttribute(required = true)
    protected String zDataReference;
    @XmlAttribute(required = true)
    protected boolean logY;
    @XmlAttribute(required = true)
    protected boolean logX;
    @XmlAttribute(required = true)
    protected boolean logZ;

    /**
     * Gets the value of the yDataReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYDataReference() {
        return yDataReference;
    }

    /**
     * Sets the value of the yDataReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYDataReference(String value) {
        this.yDataReference = value;
    }

    /**
     * Gets the value of the xDataReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXDataReference() {
        return xDataReference;
    }

    /**
     * Sets the value of the xDataReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXDataReference(String value) {
        this.xDataReference = value;
    }

    /**
     * Gets the value of the zDataReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZDataReference() {
        return zDataReference;
    }

    /**
     * Sets the value of the zDataReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZDataReference(String value) {
        this.zDataReference = value;
    }

    /**
     * Gets the value of the logY property.
     * 
     */
    public boolean isLogY() {
        return logY;
    }

    /**
     * Sets the value of the logY property.
     * 
     */
    public void setLogY(boolean value) {
        this.logY = value;
    }

    /**
     * Gets the value of the logX property.
     * 
     */
    public boolean isLogX() {
        return logX;
    }

    /**
     * Sets the value of the logX property.
     * 
     */
    public void setLogX(boolean value) {
        this.logX = value;
    }

    /**
     * Gets the value of the logZ property.
     * 
     */
    public boolean isLogZ() {
        return logZ;
    }

    /**
     * Sets the value of the logZ property.
     * 
     */
    public void setLogZ(boolean value) {
        this.logZ = value;
    }

}
