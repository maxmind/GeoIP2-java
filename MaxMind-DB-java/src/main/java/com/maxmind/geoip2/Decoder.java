package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.math.*;


class Decoder {  
  private final RandomAccessFile in;
  //private final long Data_Section_Offset;
  private final long pointer_base;
  private final int pointer_value_offset[] = {0,1<<11,(1<<11)
+(1<<19),0};
  private final HashMap<Integer,Type> types;
  class Result {
    private final Object obj;
    private final long new_offset;
    private final Type t;
    Result(Object obj,Type t,long new_offset) {
      this.t = t;
      this.obj = obj;
      this.new_offset = new_offset;
    }
    Object get_object() {
      return obj;
    }
    long get_offset() {
      return new_offset;
    }
    Type get_type() {
      return t;
    }
  }
  enum Type {
    EXTENDED(0),
    POINTER(1),
    UTF8_STRING(2) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_utf8_string(buffer,size);
      }
    },

    DOUBLE(3) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_double(buffer,size);
      }
    },
    BYTES(4) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_bytes(buffer,size);
      }
    },
    UINT16(5) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_uint(buffer,size);
      }
    },
    UINT32(6) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_uint(buffer,size);
      }
    },
    MAP(7),
    INT32(8) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_int32(buffer,size);
      }
    },
    UINT64(9) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_uint(buffer,size);
      }
    },
    UINT128(10) {
      @Override protected Object decode(byte buffer[],int size) {
        return Decoder.decode_uint(buffer,size);
      }
    },
    ARRAY(11),
    CONTAINER(12),
    END_MARKER(13);
    private final int id;
    private Type(int i) {
      this.id = i;
    }
    public int get_id() {
      return id;
    }
    protected Object decode(byte buffer[],int size) {
      return null;
    }
  }
  Decoder(RandomAccessFile in,long o) {
    this.in = in;
    pointer_base = o;
    types = new HashMap<Integer,Type>();
    for (Type t: Type.values()) {
      types.put(t.get_id(),t);
    }
  }
  public Result decode (long offset) throws IOException {
    in.seek(offset);
    int ctrl_byte = in.read();
    offset++;
    Type type = types.get(ctrl_byte >> 5);
    if (type == Type.POINTER) {
      long r[] = decode_pointer(ctrl_byte,offset);
      Result r2 = decode(r[0]);
      return new Result(r2.get_object(),r2.get_type(),r[1]);
    }
    if (type == Type.EXTENDED) {
      int next_byte = in.read();
      type = types.get(next_byte + 7);
      offset++;
    }
    long r[] = size_from_ctrl_byte(ctrl_byte,offset);
    int size = (int) r[0];
    offset = r[1];
    if (type == Type.MAP) {
      return decode_map(size,offset);
    }
    if (type == Type.ARRAY) {
      return decode_array(size,offset);
    }
    byte buffer[] = new byte[size];
    in.read(buffer,0,size);
    Object o = type.decode(buffer,size);
    return new Result(o,type,offset + size);
  }
  private long[] size_from_ctrl_byte(int ctrl_byte,long offset) {
    int size = ctrl_byte & 31;
    if (size < 29) {
      return new long[] {size,offset};
    }
    int bytes_to_read = size - 28;
    byte buffer[] = new byte[3];
    if (size == 29) {
      size = 29 + buffer[0];
    } else if (size == 30) {
      size = 285 + (buffer[0]<<8) + buffer[1];
    } else {
      size = 65821 + (buffer[2]<<16);
      size = size + (buffer[1] << 8) + buffer[0];
    }
    return new long[] {size,offset+bytes_to_read};
  }
  private Result decode_array(int size,long offset) throws IOException {
    Vector<Object> array = new Vector<Object>();    
    for (int i = 0;i < size;i++) {
      Result r = decode(offset);
      Object o = r.get_object();
      offset = r.get_offset();     
      array.add(o);
    }
    return new Result(array,Type.ARRAY,offset);

  }
  private Result decode_map(int size,long offset) throws IOException {
    HashMap<String,Object> map = new HashMap<String,Object>();
    for (int i = 0;i < size;i++) {
      Result r = decode(offset);
      String key = (String) r.get_object();
      offset = r.get_offset();
      r = decode(offset);
      Object value = r.get_object();
      offset = r.get_offset();
      map.put(key,value);
    }
    return new Result(map,Type.MAP,offset);
  }
  private long[] decode_pointer(int ctrl_byte,long offset) throws IOException {
    int pointer_size = ( ( ctrl_byte >> 3 ) & 3 ) + 1;
    byte buffer[] = new byte[4];
    in.read(buffer,0,pointer_size);
    long pointer = 0;
    if (pointer_size < 4) {
      pointer = (ctrl_byte & 7);
    }
    for (int i = 0;i < pointer_size;i++) {
      pointer = pointer << 8;
      pointer = pointer + buffer[i];
    }
    pointer = pointer + pointer_base;
    pointer = pointer + pointer_value_offset[pointer_size-1];
    return new long[] {pointer,offset+pointer_size};
  }

  static private String decode_utf8_string(byte buffer[],int size) {
    String str = null;
    try {
      str = new String(buffer,0,size,"UTF-8");    
    } catch (UnsupportedEncodingException e) {
      System.out.println("unsupported encoding");
    }
    return str;
  }

  static private Double decode_double(byte buffer[],int size) {
    Double d = Double.parseDouble(new String(buffer,0,size)); 
    return d;
  }
  static private ByteBuffer decode_bytes(byte buffer[],int size) {
    ByteBuffer bb = ByteBuffer.allocate(size);
    bb = bb.put(buffer,0,size);
    return bb;
  }
  static private Integer decode_int32(byte buffer[],int size) {
    int n = 0;
    for (int i = 0;i < size;i++) {
      n = n << 8;
      n = n | (buffer[i]);
    }
    return new Integer(n);
  }
  static private BigInteger decode_uint(byte buffer[],int size) {
    byte b[] = new byte[size];

    for (int i = 0;i < size;i++) {
      b[i] = buffer[i];
    }
    return new BigInteger(1,b);
  }

}

