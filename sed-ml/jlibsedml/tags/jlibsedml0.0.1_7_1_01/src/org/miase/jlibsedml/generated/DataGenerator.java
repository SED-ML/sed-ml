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
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.biomodels.net/sed-ml}listOfVariables" minOccurs="0"/>
 *         &lt;element ref="{http://www.biomodels.net/sed-ml}listOfParameters" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/1998/Math/MathML}math"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "listOfVariables",
    "listOfParameters",
    "math"
})
@XmlRootElement(name = "dataGenerator")
public class DataGenerator {

    protected ListOfVariables listOfVariables;
    protected ListOfParameters listOfParameters;
    @XmlElement(namespace = "http://www.w3.org/1998/Math/MathML", required = true)
    protected Math math;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected String id;

    /**
     * Gets the value of the listOfVariables property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfVariables }
     *     
     */
    public ListOfVariables getListOfVariables() {
        return listOfVariables;
    }

    /**
     * Sets the value of the listOfVariables property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfVariables }
     *     
     */
    public void setListOfVariables(ListOfVariables value) {
        this.listOfVariables = value;
    }

    /**
     * Gets the value of the listOfParameters property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfParameters }
     *     
     */
    public ListOfParameters getListOfParameters() {
        return listOfParameters;
    }

    /**
     * Sets the value of the listOfParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfParameters }
     *     
     */
    public void setListOfParameters(ListOfParameters value) {
        this.listOfParameters = value;
    }

    /**
     * Gets the value of the math property.
     * 
     * @return
     *     possible object is
     *     {@link Math }
     *     
     */
    public Math getMath() {
        return math;
    }

    /**
     * Sets the value of the math property.
     * 
     * @param value
     *     allowed object is
     *     {@link Math }
     *     
     */
    public void setMath(Math value) {
        this.math = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
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
    public void setId(String value) {
        this.id = value;
    }

}
