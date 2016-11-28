package prv.mark.project.soapclient.schemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="head" type="{http://prv.mark.project/stocks}RequestHeader"/>
 *         &lt;element name="order" type="{http://prv.mark.project/stocks}StockOrder" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "head",
        "order"
})
@XmlRootElement(name = "SubmitOrderRequest")
public class SubmitOrderRequest {

    @XmlElement(required = true)
    protected RequestHeader head;
    protected StockOrder order;

    /**
     * Gets the value of the head property.
     *
     * @return
     *     possible object is
     *     {@link RequestHeader }
     *
     */
    public RequestHeader getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     *
     * @param value
     *     allowed object is
     *     {@link RequestHeader }
     *
     */
    public void setHead(RequestHeader value) {
        this.head = value;
    }

    /**
     * Gets the value of the order property.
     *
     * @return
     *     possible object is
     *     {@link StockOrder }
     *
     */
    public StockOrder getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     *
     * @param value
     *     allowed object is
     *     {@link StockOrder }
     *
     */
    public void setOrder(StockOrder value) {
        this.order = value;
    }

}

