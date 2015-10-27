/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.live106.mars.protocol.thrift;

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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-10-26")
public class ReuqestGameConnect implements org.apache.thrift.TBase<ReuqestGameConnect, ReuqestGameConnect._Fields>, java.io.Serializable, Cloneable, Comparable<ReuqestGameConnect> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ReuqestGameConnect");

  private static final org.apache.thrift.protocol.TField GAMESERVER_FIELD_DESC = new org.apache.thrift.protocol.TField("gameserver", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField UID_FIELD_DESC = new org.apache.thrift.protocol.TField("uid", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField SEQUENCE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("sequenceId", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField RANDOM_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("randomKey", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField PASSPORT_FIELD_DESC = new org.apache.thrift.protocol.TField("passport", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ReuqestGameConnectStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ReuqestGameConnectTupleSchemeFactory());
  }

  private String gameserver; // required
  private int uid; // required
  private int sequenceId; // required
  private String randomKey; // required
  private String passport; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    GAMESERVER((short)1, "gameserver"),
    UID((short)2, "uid"),
    SEQUENCE_ID((short)3, "sequenceId"),
    RANDOM_KEY((short)4, "randomKey"),
    PASSPORT((short)5, "passport");

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
        case 1: // GAMESERVER
          return GAMESERVER;
        case 2: // UID
          return UID;
        case 3: // SEQUENCE_ID
          return SEQUENCE_ID;
        case 4: // RANDOM_KEY
          return RANDOM_KEY;
        case 5: // PASSPORT
          return PASSPORT;
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
  private static final int __UID_ISSET_ID = 0;
  private static final int __SEQUENCEID_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.GAMESERVER, new org.apache.thrift.meta_data.FieldMetaData("gameserver", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.UID, new org.apache.thrift.meta_data.FieldMetaData("uid", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.SEQUENCE_ID, new org.apache.thrift.meta_data.FieldMetaData("sequenceId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.RANDOM_KEY, new org.apache.thrift.meta_data.FieldMetaData("randomKey", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PASSPORT, new org.apache.thrift.meta_data.FieldMetaData("passport", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ReuqestGameConnect.class, metaDataMap);
  }

  public ReuqestGameConnect() {
  }

  public ReuqestGameConnect(
    String gameserver,
    int uid,
    int sequenceId,
    String randomKey,
    String passport)
  {
    this();
    this.gameserver = gameserver;
    this.uid = uid;
    setUidIsSet(true);
    this.sequenceId = sequenceId;
    setSequenceIdIsSet(true);
    this.randomKey = randomKey;
    this.passport = passport;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ReuqestGameConnect(ReuqestGameConnect other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetGameserver()) {
      this.gameserver = other.gameserver;
    }
    this.uid = other.uid;
    this.sequenceId = other.sequenceId;
    if (other.isSetRandomKey()) {
      this.randomKey = other.randomKey;
    }
    if (other.isSetPassport()) {
      this.passport = other.passport;
    }
  }

  public ReuqestGameConnect deepCopy() {
    return new ReuqestGameConnect(this);
  }

  @Override
  public void clear() {
    this.gameserver = null;
    setUidIsSet(false);
    this.uid = 0;
    setSequenceIdIsSet(false);
    this.sequenceId = 0;
    this.randomKey = null;
    this.passport = null;
  }

  public String getGameserver() {
    return this.gameserver;
  }

  public void setGameserver(String gameserver) {
    this.gameserver = gameserver;
  }

  public void unsetGameserver() {
    this.gameserver = null;
  }

  /** Returns true if field gameserver is set (has been assigned a value) and false otherwise */
  public boolean isSetGameserver() {
    return this.gameserver != null;
  }

  public void setGameserverIsSet(boolean value) {
    if (!value) {
      this.gameserver = null;
    }
  }

  public int getUid() {
    return this.uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
    setUidIsSet(true);
  }

  public void unsetUid() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __UID_ISSET_ID);
  }

  /** Returns true if field uid is set (has been assigned a value) and false otherwise */
  public boolean isSetUid() {
    return EncodingUtils.testBit(__isset_bitfield, __UID_ISSET_ID);
  }

  public void setUidIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __UID_ISSET_ID, value);
  }

  public int getSequenceId() {
    return this.sequenceId;
  }

  public void setSequenceId(int sequenceId) {
    this.sequenceId = sequenceId;
    setSequenceIdIsSet(true);
  }

  public void unsetSequenceId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SEQUENCEID_ISSET_ID);
  }

  /** Returns true if field sequenceId is set (has been assigned a value) and false otherwise */
  public boolean isSetSequenceId() {
    return EncodingUtils.testBit(__isset_bitfield, __SEQUENCEID_ISSET_ID);
  }

  public void setSequenceIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SEQUENCEID_ISSET_ID, value);
  }

  public String getRandomKey() {
    return this.randomKey;
  }

  public void setRandomKey(String randomKey) {
    this.randomKey = randomKey;
  }

  public void unsetRandomKey() {
    this.randomKey = null;
  }

  /** Returns true if field randomKey is set (has been assigned a value) and false otherwise */
  public boolean isSetRandomKey() {
    return this.randomKey != null;
  }

  public void setRandomKeyIsSet(boolean value) {
    if (!value) {
      this.randomKey = null;
    }
  }

  public String getPassport() {
    return this.passport;
  }

  public void setPassport(String passport) {
    this.passport = passport;
  }

  public void unsetPassport() {
    this.passport = null;
  }

  /** Returns true if field passport is set (has been assigned a value) and false otherwise */
  public boolean isSetPassport() {
    return this.passport != null;
  }

  public void setPassportIsSet(boolean value) {
    if (!value) {
      this.passport = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case GAMESERVER:
      if (value == null) {
        unsetGameserver();
      } else {
        setGameserver((String)value);
      }
      break;

    case UID:
      if (value == null) {
        unsetUid();
      } else {
        setUid((Integer)value);
      }
      break;

    case SEQUENCE_ID:
      if (value == null) {
        unsetSequenceId();
      } else {
        setSequenceId((Integer)value);
      }
      break;

    case RANDOM_KEY:
      if (value == null) {
        unsetRandomKey();
      } else {
        setRandomKey((String)value);
      }
      break;

    case PASSPORT:
      if (value == null) {
        unsetPassport();
      } else {
        setPassport((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case GAMESERVER:
      return getGameserver();

    case UID:
      return Integer.valueOf(getUid());

    case SEQUENCE_ID:
      return Integer.valueOf(getSequenceId());

    case RANDOM_KEY:
      return getRandomKey();

    case PASSPORT:
      return getPassport();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case GAMESERVER:
      return isSetGameserver();
    case UID:
      return isSetUid();
    case SEQUENCE_ID:
      return isSetSequenceId();
    case RANDOM_KEY:
      return isSetRandomKey();
    case PASSPORT:
      return isSetPassport();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ReuqestGameConnect)
      return this.equals((ReuqestGameConnect)that);
    return false;
  }

  public boolean equals(ReuqestGameConnect that) {
    if (that == null)
      return false;

    boolean this_present_gameserver = true && this.isSetGameserver();
    boolean that_present_gameserver = true && that.isSetGameserver();
    if (this_present_gameserver || that_present_gameserver) {
      if (!(this_present_gameserver && that_present_gameserver))
        return false;
      if (!this.gameserver.equals(that.gameserver))
        return false;
    }

    boolean this_present_uid = true;
    boolean that_present_uid = true;
    if (this_present_uid || that_present_uid) {
      if (!(this_present_uid && that_present_uid))
        return false;
      if (this.uid != that.uid)
        return false;
    }

    boolean this_present_sequenceId = true;
    boolean that_present_sequenceId = true;
    if (this_present_sequenceId || that_present_sequenceId) {
      if (!(this_present_sequenceId && that_present_sequenceId))
        return false;
      if (this.sequenceId != that.sequenceId)
        return false;
    }

    boolean this_present_randomKey = true && this.isSetRandomKey();
    boolean that_present_randomKey = true && that.isSetRandomKey();
    if (this_present_randomKey || that_present_randomKey) {
      if (!(this_present_randomKey && that_present_randomKey))
        return false;
      if (!this.randomKey.equals(that.randomKey))
        return false;
    }

    boolean this_present_passport = true && this.isSetPassport();
    boolean that_present_passport = true && that.isSetPassport();
    if (this_present_passport || that_present_passport) {
      if (!(this_present_passport && that_present_passport))
        return false;
      if (!this.passport.equals(that.passport))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_gameserver = true && (isSetGameserver());
    list.add(present_gameserver);
    if (present_gameserver)
      list.add(gameserver);

    boolean present_uid = true;
    list.add(present_uid);
    if (present_uid)
      list.add(uid);

    boolean present_sequenceId = true;
    list.add(present_sequenceId);
    if (present_sequenceId)
      list.add(sequenceId);

    boolean present_randomKey = true && (isSetRandomKey());
    list.add(present_randomKey);
    if (present_randomKey)
      list.add(randomKey);

    boolean present_passport = true && (isSetPassport());
    list.add(present_passport);
    if (present_passport)
      list.add(passport);

    return list.hashCode();
  }

  @Override
  public int compareTo(ReuqestGameConnect other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetGameserver()).compareTo(other.isSetGameserver());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGameserver()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gameserver, other.gameserver);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUid()).compareTo(other.isSetUid());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUid()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uid, other.uid);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSequenceId()).compareTo(other.isSetSequenceId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSequenceId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sequenceId, other.sequenceId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRandomKey()).compareTo(other.isSetRandomKey());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRandomKey()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.randomKey, other.randomKey);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPassport()).compareTo(other.isSetPassport());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPassport()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.passport, other.passport);
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
    StringBuilder sb = new StringBuilder("ReuqestGameConnect(");
    boolean first = true;

    sb.append("gameserver:");
    if (this.gameserver == null) {
      sb.append("null");
    } else {
      sb.append(this.gameserver);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("uid:");
    sb.append(this.uid);
    first = false;
    if (!first) sb.append(", ");
    sb.append("sequenceId:");
    sb.append(this.sequenceId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("randomKey:");
    if (this.randomKey == null) {
      sb.append("null");
    } else {
      sb.append(this.randomKey);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("passport:");
    if (this.passport == null) {
      sb.append("null");
    } else {
      sb.append(this.passport);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetUid()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'uid' is unset! Struct:" + toString());
    }

    if (!isSetPassport()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'passport' is unset! Struct:" + toString());
    }

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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ReuqestGameConnectStandardSchemeFactory implements SchemeFactory {
    public ReuqestGameConnectStandardScheme getScheme() {
      return new ReuqestGameConnectStandardScheme();
    }
  }

  private static class ReuqestGameConnectStandardScheme extends StandardScheme<ReuqestGameConnect> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ReuqestGameConnect struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // GAMESERVER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gameserver = iprot.readString();
              struct.setGameserverIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // UID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.uid = iprot.readI32();
              struct.setUidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // SEQUENCE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.sequenceId = iprot.readI32();
              struct.setSequenceIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // RANDOM_KEY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.randomKey = iprot.readString();
              struct.setRandomKeyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // PASSPORT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.passport = iprot.readString();
              struct.setPassportIsSet(true);
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
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ReuqestGameConnect struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.gameserver != null) {
        oprot.writeFieldBegin(GAMESERVER_FIELD_DESC);
        oprot.writeString(struct.gameserver);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(UID_FIELD_DESC);
      oprot.writeI32(struct.uid);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(SEQUENCE_ID_FIELD_DESC);
      oprot.writeI32(struct.sequenceId);
      oprot.writeFieldEnd();
      if (struct.randomKey != null) {
        oprot.writeFieldBegin(RANDOM_KEY_FIELD_DESC);
        oprot.writeString(struct.randomKey);
        oprot.writeFieldEnd();
      }
      if (struct.passport != null) {
        oprot.writeFieldBegin(PASSPORT_FIELD_DESC);
        oprot.writeString(struct.passport);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ReuqestGameConnectTupleSchemeFactory implements SchemeFactory {
    public ReuqestGameConnectTupleScheme getScheme() {
      return new ReuqestGameConnectTupleScheme();
    }
  }

  private static class ReuqestGameConnectTupleScheme extends TupleScheme<ReuqestGameConnect> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ReuqestGameConnect struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI32(struct.uid);
      oprot.writeString(struct.passport);
      BitSet optionals = new BitSet();
      if (struct.isSetGameserver()) {
        optionals.set(0);
      }
      if (struct.isSetSequenceId()) {
        optionals.set(1);
      }
      if (struct.isSetRandomKey()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetGameserver()) {
        oprot.writeString(struct.gameserver);
      }
      if (struct.isSetSequenceId()) {
        oprot.writeI32(struct.sequenceId);
      }
      if (struct.isSetRandomKey()) {
        oprot.writeString(struct.randomKey);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ReuqestGameConnect struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.uid = iprot.readI32();
      struct.setUidIsSet(true);
      struct.passport = iprot.readString();
      struct.setPassportIsSet(true);
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.gameserver = iprot.readString();
        struct.setGameserverIsSet(true);
      }
      if (incoming.get(1)) {
        struct.sequenceId = iprot.readI32();
        struct.setSequenceIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.randomKey = iprot.readString();
        struct.setRandomKeyIsSet(true);
      }
    }
  }

}

