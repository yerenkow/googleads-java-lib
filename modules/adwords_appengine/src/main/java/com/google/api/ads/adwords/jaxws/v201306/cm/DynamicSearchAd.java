
package com.google.api.ads.adwords.jaxws.v201306.cm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Represents a dynamic search ad. This ad will have its headline and
 *             destination URL auto-generated at serving time according to domain name
 *             specific information provided by {@link DomainInfoExtension} linked at the
 *             campaign level.
 *             
 *             <p>Auto-generated fields: headline and destination URL (may contain an optional
 *             tracking URL)</p>
 *             
 *             <p>Required fields: description1, description2, displayUrl</p>
 *             
 *             <p>The URL field must contain at least one of the following placeholder tags
 *             (URL parameters): {unescapedlpurl}, {escapedlpurl}, {lpurlpath}. If no URL is
 *             specified, {unescapedlpurl} will be used as default.</p>
 *             
 *             <p>{unescapedlpurl} can only be used at the beginning of the URL field. It
 *             will be replaced with the full landing page URL of the displayed ad. Extra query
 *             parameters can be added to the end, e.g. "{unescapedlpurl}?lang=en".</p>
 *             
 *             <p>{escapedlpurl} will be replaced with the URL-encoded version of the full
 *             landing page URL. This makes it suitable for use as a query parameter
 *             value (e.g. "http://www.3rdpartytracker.com/?lp={escapedlpurl}") but
 *             not at the beginning of the url field.</p>
 *             
 *             <p>{lpurlpath} will be replaced with the path and query part of the landing
 *             page URL and can be added to a different URL, e.g.
 *             "http://www.mygoodbusiness.com/tracking/{lpurlpath}".</p>
 *             
 *             <p>There are also special rules that come into play depending on whether the
 *             destination URL uses local click tracking or third-party click tracking.<p>
 *             
 *             <p>For more information, see the article <a href="//support.google.com/adwords/bin/answer.py?answer=2549100">Using dynamic tracking URLs</a>.
 *             <span class="constraint AdxEnabled">This is disabled for AdX when it is contained within Operators: ADD, SET.</span>
 *           
 * 
 * <p>Java class for DynamicSearchAd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DynamicSearchAd">
 *   &lt;complexContent>
 *     &lt;extension base="{https://adwords.google.com/api/adwords/cm/v201306}Ad">
 *       &lt;sequence>
 *         &lt;element name="description1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DynamicSearchAd", propOrder = {
    "description1",
    "description2"
})
public class DynamicSearchAd
    extends Ad
{

    protected String description1;
    protected String description2;

    /**
     * Gets the value of the description1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription1() {
        return description1;
    }

    /**
     * Sets the value of the description1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription1(String value) {
        this.description1 = value;
    }

    /**
     * Gets the value of the description2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription2() {
        return description2;
    }

    /**
     * Sets the value of the description2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription2(String value) {
        this.description2 = value;
    }

}
