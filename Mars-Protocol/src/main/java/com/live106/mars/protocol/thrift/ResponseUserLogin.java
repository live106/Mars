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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-10-23")
public class ResponseUserLogin implements org.apache.thrift.TBase<ResponseUserLogin, ResponseUserLogin._Fields>, java.io.Serializable, Cloneable, Comparable<ResponseUserLogin> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ResponseUserLogin");

  private static final org.apache.thrift.protocol.TField CODE_FIELD_DESC = new org.apache.thrift.protocol.TField("code", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("msg", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField UID_FIELD_DESC = new org.apache.thrift.protocol.TField("uid", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField PASSPORT_FIELD_DESC = new org.apache.thrift.protocol.TField("passport", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField SECURE_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("secureKey", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField GAMESERVER_FIELD_DESC = new org.apache.thrift.protocol.TField("gameserver", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ResponseUserLoginStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ResponseUserLoginTupleSchemeFactory());
  }

  private LoginCode code; // required
  private String msg; // required
  private int uid; // required
  private String passport; // required
  private String secureKey; // required
  private String gameserver; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see LoginCode
     */
    CODE((short)1, "code"),
    MSG((short)2, "msg"),
    UID((short)3, "uid"),
    PASSPORT((short)4, "passport"),
    SECURE_KEY((short)5, "secureKey"),
    GAMESERVER((short)6, "gameserver");

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
        case 1: // CODE
          return CODE;
        case 2: // MSG
          return MSG;
        case 3: // UID
          return UID;
        case 4: // PASSPORT
          return PASSPORT;
        case 5: // SECURE_KEY
          return SECURE_KEY;
        case 6: // GAMESERVER
          return GAMESERVER;
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
    tmpMap.put(_Fields.CODE, new org.apache.thrift.meta_data.FieldMetaData("code", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, LoginCode.class)));
    tmpMap.put(_Fields.MSG, new org.apache.thrift.meta_data.FieldMetaData("msg", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.UID, new org.apache.thrift.meta_data.FieldMetaData("uid", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.PASSPORT, new org.apache.thrift.meta_data.FieldMetaData("passport", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SECURE_KEY, new org.apache.thrift.meta_data.FieldMetaData("secureKey", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GAMESERVER, new org.apache.thrift.meta_data.FieldMetaData("gameserver", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ResponseUserLogin.class, metaDataMap);
  }

  public ResponseUserLogin() {
  }

  public ResponseUserLogin(
    LoginCode code,
    String msg,
    int uid,
    String passport,
    String secureKey,
    String gameserver)
  {
    this();
    this.code = code;
    this.msg = msg;
    this.uid = uid;
    setUidIsSet(true);
    this.passport = passport;
    this.secureKey = secureKey;
    this.gameserver = gameserver;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ResponseUserLogin(ResponseUserLogin other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetCode()) {
      this.code = other.code;
    }
    if (other.isSetMsg()) {
      this.msg = other.msg;
    }
    this.uid = other.uid;
    if (other.isSetPassport()) {
      this.passport = other.passport;
    }
    if (other.isSetSecureKey()) {
      this.secureKey = other.secureKey;
    }
    if (other.isSetGameserver()) {
      this.gameserver = other.gameserver;
    }
  }

  public ResponseUserLogin deepCopy() {
    return new ResponseUserLogin(this);
  }

  @Override
  public void clear() {
    this.code = null;
    this.msg = null;
    setUidIsSet(false);
    this.uid = 0;
    this.passport = null;
    this.secureKey = null;
    this.gameserver = null;
  }

  /**
   * 
   * @see LoginCode
   */
  public LoginCode getCode() {
    return this.code;
  }

  /**
   * 
   * @see LoginCode
   */
  public void setCode(LoginCode code) {
    this.code = code;
  }

  public void unsetCode() {
    this.code = null;
  }

  /** Returns true if field code is set (has been assigned a value) and false otherwise */
  public boolean isSetCode() {
    return this.code != null;
  }

  public void setCodeIsSet(boolean value) {
    if (!value) {
      this.code = null;
    }
  }

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public void unsetMsg() {
    this.msg = null;
  }

  /** Returns true if field msg is set (has been assigned a value) and false otherwise */
  public boolean isSetMsg() {
    return this.msg != null;
  }

  public void setMsgIsSet(boolean value) {
    if (!value) {
      this.msg = null;
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

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CODE:
      if (value == null) {
        unsetCode();
      } else {
        setCode((LoginCode)value);
      }
      break;

    case MSG:
      if (value == null) {
        unsetMsg();
      } else {
        setMsg((String)value);
      }
      break;

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

    case GAMESERVER:
      if (value == null) {
        unsetGameserver();
      } else {
        setGameserver((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CODE:
      return getCode();

    case MSG:
      return getMsg();

    case UID:
      return Integer.valueOf(getUid());

    case PASSPORT:
      return getPassport();

    case SECURE_KEY:
      return getSecureKey();

    case GAMESERVER:
      return getGameserver();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CODE:
      return isSetCode();
    case MSG:
      return isSetMsg();
    case UID:
      return isSetUid();
    case PASSPORT:
      return isSetPassport();
    case SECURE_KEY:
      return isSetSecureKey();
    case GAMESERVER:
      return isSetGameserver();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ResponseUserLogin)
      return this.equals((ResponseUserLogin)that);
    return false;
  }

  public boolean equals(ResponseUserLogin that) {
    if (that == null)
      return false;

    boolean this_present_code = true && this.isSetCode();
    boolean that_present_code = true && that.isSetCode();
    if (this_present_code || that_present_code) {
      if (!(this_present_code && that_present_code))
        return false;
      if (!this.code.equals(that.code))
        return false;
    }

    boolean this_present_msg = true && this.isSetMsg();
    boolean that_present_msg = true && that.isSetMsg();
    if (this_present_msg || that_present_msg) {
      if (!(this_present_msg && that_present_msg))
        return false;
      if (!this.msg.equals(that.msg))
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

    boolean this_present_gameserver = true && this.isSetGameserver();
    boolean that_present_gameserver = true && that.isSetGameserver();
    if (this_present_gameserver || that_present_gameserver) {
      if (!(this_present_gameserver && that_present_gameserver))
        return false;
      if (!this.gameserver.equals(that.gameserver))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_code = true && (isSetCode());
    list.add(present_code);
    if (present_code)
      list.add(code.getValue());

    boolean present_msg = true && (isSetMsg());
    list.add(present_msg);
    if (present_msg)
      list.add(msg);

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

    boolean present_gameserver = true && (isSetGameserver());
    list.add(present_gameserver);
    if (present_gameserver)
      list.add(gameserver);

    return list.hashCode();
  }

  @Override
  public int compareTo(ResponseUserLogin other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCode()).compareTo(other.isSetCode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.code, other.code);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMsg()).compareTo(other.isSetMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.msg, other.msg);
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
    StringBuilder sb = new StringBuilder("ResponseUserLogin(");
    boolean first = true;

    sb.append("code:");
    if (this.code == null) {
      sb.append("null");
    } else {
      sb.append(this.code);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("msg:");
    if (this.msg == null) {
      sb.append("null");
    } else {
      sb.append(this.msg);
    }
    first = false;
    if (!first) sb.append(", ");
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
    sb.append("gameserver:");
    if (this.gameserver == null) {
      sb.append("null");
    } else {
      sb.append(this.gameserver);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetCode()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'code' is unset! Struct:" + toString());
    }

    if (!isSetMsg()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'msg' is unset! Struct:" + toString());
    }

    if (!isSetUid()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'uid' is unset! Struct:" + toString());
    }

    if (!isSetPassport()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'passport' is unset! Struct:" + toString());
    }

    if (!isSetSecureKey()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'secureKey' is unset! Struct:" + toString());
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

  private static class ResponseUserLoginStandardSchemeFactory implements SchemeFactory {
    public ResponseUserLoginStandardScheme getScheme() {
      return new ResponseUserLoginStandardScheme();
    }
  }

  private static class ResponseUserLoginStandardScheme extends StandardScheme<ResponseUserLogin> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ResponseUserLogin struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CODE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.code = com.live106.mars.protocol.thrift.LoginCode.findByValue(iprot.readI32());
              struct.setCodeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.msg = iprot.readString();
              struct.setMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // UID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.uid = iprot.readI32();
              struct.setUidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PASSPORT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.passport = iprot.readString();
              struct.setPassportIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // SECURE_KEY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.secureKey = iprot.readString();
              struct.setSecureKeyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // GAMESERVER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gameserver = iprot.readString();
              struct.setGameserverIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ResponseUserLogin struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.code != null) {
        oprot.writeFieldBegin(CODE_FIELD_DESC);
        oprot.writeI32(struct.code.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.msg != null) {
        oprot.writeFieldBegin(MSG_FIELD_DESC);
        oprot.writeString(struct.msg);
        oprot.writeFieldEnd();
      }
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
      if (struct.gameserver != null) {
        oprot.writeFieldBegin(GAMESERVER_FIELD_DESC);
        oprot.writeString(struct.gameserver);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ResponseUserLoginTupleSchemeFactory implements SchemeFactory {
    public ResponseUserLoginTupleScheme getScheme() {
      return new ResponseUserLoginTupleScheme();
    }
  }

  private static class ResponseUserLoginTupleScheme extends TupleScheme<ResponseUserLogin> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ResponseUserLogin struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI32(struct.code.getValue());
      oprot.writeString(struct.msg);
      oprot.writeI32(struct.uid);
      oprot.writeString(struct.passport);
      oprot.writeString(struct.secureKey);
      BitSet optionals = new BitSet();
      if (struct.isSetGameserver()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetGameserver()) {
        oprot.writeString(struct.gameserver);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ResponseUserLogin struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.code = com.live106.mars.protocol.thrift.LoginCode.findByValue(iprot.readI32());
      struct.setCodeIsSet(true);
      struct.msg = iprot.readString();
      struct.setMsgIsSet(true);
      struct.uid = iprot.readI32();
      struct.setUidIsSet(true);
      struct.passport = iprot.readString();
      struct.setPassportIsSet(true);
      struct.secureKey = iprot.readString();
      struct.setSecureKeyIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.gameserver = iprot.readString();
        struct.setGameserverIsSet(true);
      }
    }
  }

}

