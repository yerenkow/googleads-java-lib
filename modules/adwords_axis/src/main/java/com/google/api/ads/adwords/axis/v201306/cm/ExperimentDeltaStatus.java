/**
 * ExperimentDeltaStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Mar 02, 2009 (07:08:06 PST) WSDL2Java emitter.
 */

package com.google.api.ads.adwords.axis.v201306.cm;

public class ExperimentDeltaStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ExperimentDeltaStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _EXPERIMENT_ONLY = "EXPERIMENT_ONLY";
    public static final java.lang.String _MODIFIED = "MODIFIED";
    public static final java.lang.String _CONTROL_ONLY = "CONTROL_ONLY";
    public static final java.lang.String _UNKNOWN = "UNKNOWN";
    public static final ExperimentDeltaStatus EXPERIMENT_ONLY = new ExperimentDeltaStatus(_EXPERIMENT_ONLY);
    public static final ExperimentDeltaStatus MODIFIED = new ExperimentDeltaStatus(_MODIFIED);
    public static final ExperimentDeltaStatus CONTROL_ONLY = new ExperimentDeltaStatus(_CONTROL_ONLY);
    public static final ExperimentDeltaStatus UNKNOWN = new ExperimentDeltaStatus(_UNKNOWN);
    public java.lang.String getValue() { return _value_;}
    public static ExperimentDeltaStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ExperimentDeltaStatus enumeration = (ExperimentDeltaStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ExperimentDeltaStatus fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExperimentDeltaStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201306", "ExperimentDeltaStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
