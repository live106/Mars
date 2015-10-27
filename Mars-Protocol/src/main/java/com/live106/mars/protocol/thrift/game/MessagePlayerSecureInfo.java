/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.live106.mars.protocol.thrift.game;

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
public class MessagePlayerSecureInfo implements org.apache.thrift.TBase<MessagePlayerSecureInfo, MessagePlayerSecureInfo._Fields>, java.io.Serializable, Cloneable, Comparable<MessagePlayerSecureInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MessagePlayerSecureInfo");

  private static final org.apache.thrift.protocol.TField UID_FIELD_DESC = new org.apache.thrift.protocol.TField("uid", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField PASSPORT_FIELD_DESC = new org.apache.thrift.protocol.TField("passport", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField SECURE_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("secureKey", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField CHANNEL_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("channelId", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new MessagePlayerSecureInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new MessagePlayerSecureInfoTupleSchemeFactory());
  }

  private int uid; // required
  private String passport; // required
  private String secureKey; // required
  private String channelId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    UID((short)1, "uid"),
    PASSPORT((short)2, "passport"),
    SECURE_KEY((short)3, "secureKey"),
    CHANNEL_ID((short)4, "channelId");

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
        case 1: // UID
          return UID;
        case 2: // PASSPORT
          return PASSPORT;
        case 3: // SECURE_KEY
          return SECURE_KEY;
        case 4: // CHANNEL_ID
          return CHANNEL_ID;
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
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.UID, new org.apache.thrift.meta_data.FieldMetaData("uid", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.PASSPORT, new org.apache.thrift.meta_data.FieldMetaData("passport", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SECURE_KEY, new org.apache.thrift.meta_data.FieldMetaData("secureKey", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CHANNEL_ID, new org.apache.thrift.meta_data.FieldMetaData("channelId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MessagePlayerSecureInfo.class, metaDataMap);
  }

  public MessagePlayerSecureInfo() {
  }

  public MessagePlayerSecureInfo(
    int uid,
    String passport,
    String secureKey,
    String channelId)
  {
    this();
    this.uid = uid;
    setUidIsSet(true);
    this.passport = passport;
    this.secureKey = secureKey;
    this.channelId = channelId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MessagePlayerSecureInfo(MessagePlayerSecureInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.uid = other.uid;
    if (other.isSetPassport()) {
      this.passport = other.passport;
    }
    if (other.isSetSecureKey()) {
      this.secureKey = other.secureKey;
    }
    if (other.isSetChannelId()) {
      this.channelId = other.channelId;
    }
  }

  public MessagePlayerSecureInfo deepCopy() {
    return new MessagePlayerSecureInfo(this);
  }

  @Override
  public void clear() {
    setUidIsSet(false);
    this.uid = 0;
    this.passport = null;
    this.secureKey = null;
    this.channelId = null;
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

  public String getSecureKey() {
    return this.secureKey;
  }

  public void setSecureKey(String secureKey) {
    this.secureKey = secureKey;
  }

  public void unsetSecureKey() {
    this.secureKey = null;
  }

  /** Returns true if field secureKey is set (has been assigned a value) and false otherwise */
  public boolean isSetSecureKey() {
    return this.secureKey != null;
  }

  public void setSecureKeyIsSet(boolean value) {
    if (!value) {
      this.secureKey = null;
    }
  }

  public String getChannelId() {
    return this.channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public void unsetChannelId() {
    this.channelId = null;
  }

  /** Returns true if field channelId is set (has been assigned a value) and false otherwise */
  public boolean isSetChannelId() {
    return this.channelId != null;
  }

  public void setChannelIdIsSet(boolean value) {
    if (!value) {
      this.channelId = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case UID:
      if (value == null) {
        unsetUid();
      } else {
        setUid((Integer)value);
      }
      break;

    case PASSPORT:
      if (value == null) {
        unsetPassport();
      } else {
        setPassport((String)value);
      }
      break;

    case SECURE_KEY:
      if (value == null) {
        unsetSecureKey();
      } else {
        setSecureKey((String)value);
      }
      break;

    case CHANNEL_ID:
      if (value == null) {
        unsetChannelId();
      } else {
        setChannelId((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case UID:
      return Integer.valueOf(getUid());

    case PASSPORT:
      return getPassport();

    case SECURE_KEY:
      return getSecureKey();

    case CHANNEL_ID:
      return getChannelId();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case UID:
      return isSetUid();
    case PASSPORT:
      return isSetPassport();
    case SECURE_KEY:
      return isSetSecureKey();
    case CHANNEL_ID:
      return isSetChannelId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MessagePlayerSecureInfo)
      return this.equals((MessagePlayerSecureInfo)that);
    return false;
  }

  public boolean equals(MessagePlayerSecureInfo that) {
    if (that == null)
      return false;

    boolean this_present_uid = true;
    boolean that_present_uid = true;
    if (this_present_uid || that_present_uid) {
      if (!(this_present_uid && that_present_uid))
        return false;
      if (this.uid != that.uid)
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

    boolean this_present_secureKey = true && this.isSetSecureKey();
    boolean that_present_secureKey = true && that.isSetSecureKey();
    if (this_present_secureKey || that_present_secureKey) {
      if (!(this_present_secureKey && that_present_secureKey))
        return false;
      if (!this.secureKey.equals(that.secureKey))
        return false;
    }

    boolean this_present_channelId = true && this.isSetChannelId();
    boolean that_present_channelId = true && that.isSetChannelId();
    if (this_present_channelId || that_present_channelId) {
      if (!(this_present_channelId && that_present_channelId))
        return false;
      if (!this.channelId.equals(that.channelId))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_uid = true;
    list.add(present_uid);
    if (present_uid)
      list.add(uid);

    boolean present_passport = true && (isSetPassport());
    list.add(present_passport);
    if (present_passport)
      list.add(passport);

    boolean present_secureKey = true && (isSetSecureKey());
    list.add(present_secureKey);
    if (present_secureKey)
      list.add(secureKey);

    boolean present_channelId = true && (isSetChannelId());
    list.add(present_channelId);
    if (present_channelId)
      list.add(channelId);

    return list.hashCode();
  }

  @Override
  public int compareTo(MessagePlayerSecureInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

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
    lastComparison = Boolean.valueOf(isSetSecureKey()).compareTo(other.isSetSecureKey());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSecureKey()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.secureKey, other.secureKey);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetChannelId()).compareTo(other.isSetChannelId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetChannelId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.channelId, other.channelId);
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
    StringBuilder sb = new StringBuilder("MessagePlayerSecureInfo(");
    boolean first = true;

    sb.append("uid:");
    sb.append(this.uid);
    first = false;
    if (!first) sb.append(", ");
    sb.append("passport:");
    if (this.passport == null) {
      sb.append("null");
    } else {
      sb.append(this.passport);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("secureKey:");
    if (this.secureKey == null) {
      sb.append("null");
    } else {
      sb.append(this.secureKey);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("channelId:");
    if (this.channelId == null) {
      sb.append("null");
    } else {
      sb.append(this.channelId);
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class MessagePlayerSecureInfoStandardSchemeFactory implements SchemeFactory {
    public MessagePlayerSecureInfoStandardScheme getScheme() {
      return new MessagePlayerSecureInfoStandardScheme();
    }
  }

  private static class MessagePlayerSecureInfoStandardScheme extends StandardScheme<MessagePlayerSecureInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, MessagePlayerSecureInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // UID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.uid = iprot.readI32();
              struct.setUidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PASSPORT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.passport = iprot.readString();
              struct.setPassportIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // SECURE_KEY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.secureKey = iprot.readString();
              struct.setSecureKeyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CHANNEL_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.channelId = iprot.readString();
              struct.setChannelIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, MessagePlayerSecureInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(UID_FIELD_DESC);
      oprot.writeI32(struct.uid);
      oprot.writeFieldEnd();
      if (struct.passport != null) {
        oprot.writeFieldBegin(PASSPORT_FIELD_DESC);
        oprot.writeString(struct.passport);
        oprot.writeFieldEnd();
      }
      if (struct.secureKey != null) {
        oprot.writeFieldBegin(SECURE_KEY_FIELD_DESC);
        oprot.writeString(struct.secureKey);
        oprot.writeFieldEnd();
      }
      if (struct.channelId != null) {
        oprot.writeFieldBegin(CHANNEL_ID_FIELD_DESC);
        oprot.writeString(struct.channelId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MessagePlayerSecureInfoTupleSchemeFactory implements SchemeFactory {
    public MessagePlayerSecureInfoTupleScheme getScheme() {
      return new MessagePlayerSecureInfoTupleScheme();
    }
  }

  private static class MessagePlayerSecureInfoTupleScheme extends TupleScheme<MessagePlayerSecureInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MessagePlayerSecureInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetUid()) {
        optionals.set(0);
      }
      if (struct.isSetPassport()) {
        optionals.set(1);
      }
      if (struct.isSetSecureKey()) {
        optionals.set(2);
      }
      if (struct.isSetChannelId()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetUid()) {
        oprot.writeI32(struct.uid);
      }
      if (struct.isSetPassport()) {
        oprot.writeString(struct.passport);
      }
      if (struct.isSetSecureKey()) {
        oprot.writeString(struct.secureKey);
      }
      if (struct.isSetChannelId()) {
        oprot.writeString(struct.channelId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MessagePlayerSecureInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.uid = iprot.readI32();
        struct.setUidIsSet(true);
      }
      if (incoming.get(1)) {
        struct.passport = iprot.readString();
        struct.setPassportIsSet(true);
      }
      if (incoming.get(2)) {
        struct.secureKey = iprot.readString();
        struct.setSecureKeyIsSet(true);
      }
      if (incoming.get(3)) {
        struct.channelId = iprot.readString();
        struct.setChannelIdIsSet(true);
      }
    }
  }

}

