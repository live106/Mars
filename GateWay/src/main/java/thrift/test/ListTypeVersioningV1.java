/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.test;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-9-29")
public class ListTypeVersioningV1 implements org.apache.thrift.TBase<ListTypeVersioningV1, ListTypeVersioningV1._Fields>, java.io.Serializable, Cloneable, Comparable<ListTypeVersioningV1> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ListTypeVersioningV1");

  private static final org.apache.thrift.protocol.TField MYINTS_FIELD_DESC = new org.apache.thrift.protocol.TField("myints", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField HELLO_FIELD_DESC = new org.apache.thrift.protocol.TField("hello", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ListTypeVersioningV1StandardSchemeFactory());
    schemes.put(TupleScheme.class, new ListTypeVersioningV1TupleSchemeFactory());
  }

  public List<Integer> myints; // required
  public String hello; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    MYINTS((short)1, "myints"),
    HELLO((short)2, "hello");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // MYINTS
          return MYINTS;
        case 2: // HELLO
          return HELLO;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.MYINTS, new org.apache.thrift.meta_data.FieldMetaData("myints", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    tmpMap.put(_Fields.HELLO, new org.apache.thrift.meta_data.FieldMetaData("hello", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ListTypeVersioningV1.class, metaDataMap);
  }

  public ListTypeVersioningV1() {
  }

  public ListTypeVersioningV1(
    List<Integer> myints,
    String hello)
  {
    this();
    this.myints = myints;
    this.hello = hello;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ListTypeVersioningV1(ListTypeVersioningV1 other) {
    if (other.isSetMyints()) {
      List<Integer> __this__myints = new ArrayList<Integer>(other.myints);
      this.myints = __this__myints;
    }
    if (other.isSetHello()) {
      this.hello = other.hello;
    }
  }

  public ListTypeVersioningV1 deepCopy() {
    return new ListTypeVersioningV1(this);
  }

  @Override
  public void clear() {
    this.myints = null;
    this.hello = null;
  }

  public int getMyintsSize() {
    return (this.myints == null) ? 0 : this.myints.size();
  }

  public java.util.Iterator<Integer> getMyintsIterator() {
    return (this.myints == null) ? null : this.myints.iterator();
  }

  public void addToMyints(int elem) {
    if (this.myints == null) {
      this.myints = new ArrayList<Integer>();
    }
    this.myints.add(elem);
  }

  public List<Integer> getMyints() {
    return this.myints;
  }

  public ListTypeVersioningV1 setMyints(List<Integer> myints) {
    this.myints = myints;
    return this;
  }

  public void unsetMyints() {
    this.myints = null;
  }

  /** Returns true if field myints is set (has been assigned a value) and false otherwise */
  public boolean isSetMyints() {
    return this.myints != null;
  }

  public void setMyintsIsSet(boolean value) {
    if (!value) {
      this.myints = null;
    }
  }

  public String getHello() {
    return this.hello;
  }

  public ListTypeVersioningV1 setHello(String hello) {
    this.hello = hello;
    return this;
  }

  public void unsetHello() {
    this.hello = null;
  }

  /** Returns true if field hello is set (has been assigned a value) and false otherwise */
  public boolean isSetHello() {
    return this.hello != null;
  }

  public void setHelloIsSet(boolean value) {
    if (!value) {
      this.hello = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case MYINTS:
      if (value == null) {
        unsetMyints();
      } else {
        setMyints((List<Integer>)value);
      }
      break;

    case HELLO:
      if (value == null) {
        unsetHello();
      } else {
        setHello((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case MYINTS:
      return getMyints();

    case HELLO:
      return getHello();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case MYINTS:
      return isSetMyints();
    case HELLO:
      return isSetHello();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ListTypeVersioningV1)
      return this.equals((ListTypeVersioningV1)that);
    return false;
  }

  public boolean equals(ListTypeVersioningV1 that) {
    if (that == null)
      return false;

    boolean this_present_myints = true && this.isSetMyints();
    boolean that_present_myints = true && that.isSetMyints();
    if (this_present_myints || that_present_myints) {
      if (!(this_present_myints && that_present_myints))
        return false;
      if (!this.myints.equals(that.myints))
        return false;
    }

    boolean this_present_hello = true && this.isSetHello();
    boolean that_present_hello = true && that.isSetHello();
    if (this_present_hello || that_present_hello) {
      if (!(this_present_hello && that_present_hello))
        return false;
      if (!this.hello.equals(that.hello))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_myints = true && (isSetMyints());
    list.add(present_myints);
    if (present_myints)
      list.add(myints);

    boolean present_hello = true && (isSetHello());
    list.add(present_hello);
    if (present_hello)
      list.add(hello);

    return list.hashCode();
  }

  @Override
  public int compareTo(ListTypeVersioningV1 other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMyints()).compareTo(other.isSetMyints());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMyints()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.myints, other.myints);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetHello()).compareTo(other.isSetHello());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHello()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.hello, other.hello);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ListTypeVersioningV1(");
    boolean first = true;

    sb.append("myints:");
    if (this.myints == null) {
      sb.append("null");
    } else {
      sb.append(this.myints);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("hello:");
    if (this.hello == null) {
      sb.append("null");
    } else {
      sb.append(this.hello);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ListTypeVersioningV1StandardSchemeFactory implements SchemeFactory {
    public ListTypeVersioningV1StandardScheme getScheme() {
      return new ListTypeVersioningV1StandardScheme();
    }
  }

  private static class ListTypeVersioningV1StandardScheme extends StandardScheme<ListTypeVersioningV1> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ListTypeVersioningV1 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // MYINTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list114 = iprot.readListBegin();
                struct.myints = new ArrayList<Integer>(_list114.size);
                int _elem115;
                for (int _i116 = 0; _i116 < _list114.size; ++_i116)
                {
                  _elem115 = iprot.readI32();
                  struct.myints.add(_elem115);
                }
                iprot.readListEnd();
              }
              struct.setMyintsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // HELLO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.hello = iprot.readString();
              struct.setHelloIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ListTypeVersioningV1 struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.myints != null) {
        oprot.writeFieldBegin(MYINTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, struct.myints.size()));
          for (int _iter117 : struct.myints)
          {
            oprot.writeI32(_iter117);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.hello != null) {
        oprot.writeFieldBegin(HELLO_FIELD_DESC);
        oprot.writeString(struct.hello);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ListTypeVersioningV1TupleSchemeFactory implements SchemeFactory {
    public ListTypeVersioningV1TupleScheme getScheme() {
      return new ListTypeVersioningV1TupleScheme();
    }
  }

  private static class ListTypeVersioningV1TupleScheme extends TupleScheme<ListTypeVersioningV1> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ListTypeVersioningV1 struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMyints()) {
        optionals.set(0);
      }
      if (struct.isSetHello()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetMyints()) {
        {
          oprot.writeI32(struct.myints.size());
          for (int _iter118 : struct.myints)
          {
            oprot.writeI32(_iter118);
          }
        }
      }
      if (struct.isSetHello()) {
        oprot.writeString(struct.hello);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ListTypeVersioningV1 struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list119 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, iprot.readI32());
          struct.myints = new ArrayList<Integer>(_list119.size);
          int _elem120;
          for (int _i121 = 0; _i121 < _list119.size; ++_i121)
          {
            _elem120 = iprot.readI32();
            struct.myints.add(_elem120);
          }
        }
        struct.setMyintsIsSet(true);
      }
      if (incoming.get(1)) {
        struct.hello = iprot.readString();
        struct.setHelloIsSet(true);
      }
    }
  }

}

