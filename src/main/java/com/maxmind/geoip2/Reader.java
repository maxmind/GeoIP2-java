package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.math.*;
import java.net.*;

class Reader {
  private Decoder d;
  private static byte MetadataStartMarker[] = {(byte) 0xab,(byte) 0xcd,
  (byte) 0xef,'M','a','x','M','i','n','d','.','c','o','m'};
  private int node_count;
  private int record_size;
  private int ip_version;
  private int node_byte_size;
  private long search_tree_size;
  private RandomAccessFile in;
  private int major_version;
  private int minor_version;
  Reader(String Filename) {
    try {
      in = new RandomAccessFile(Filename,"r");
      long offset = find_MetadataStartMarker(in);
      Decoder d = new Decoder(in,0);
      Decoder.Result dr = d.decode(offset);
      HashMap<String,Object> h = (HashMap<String,Object>) dr.get_object();
      Number n = (Number) h.get("node_count");
      node_count = n.intValue();
      n = (Number) h.get("record_size");
      record_size = n.intValue();
      n = (Number) h.get("ip_version");
      ip_version = n.intValue();
      n = (Number) h.get("binary_format_major_version");
      major_version = n.intValue();
      n = (Number) h.get("binary_format_minor_version");
      minor_version = n.intValue();
      System.out.println("node_count: " + node_count);
      System.out.println("record_size: " + record_size);
      System.out.println("ip_version: " + ip_version);
      System.out.println("major_version: " + major_version);
      System.out.println("minor_version: " + minor_version);
      node_byte_size = (record_size * 2) / 8;
      search_tree_size = node_count * node_byte_size;
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }

  private long find_address_in_tree(String address) {
    int node_num = 0;
    try {
      InetAddress addr = InetAddress.getByName(address);
      byte b[] = addr.getAddress(); 
      for (int i = 0;i < b.length;i++) {
        //System.out.println(b[i]);
        for (int j = 7;j >= 0;j--) {
          int bit = (b[i] >> j) & 1;
          int n[] = read_node(node_num);
          int record = n[bit];
          //System.out.println("record: " + record + " " + bit);
          if (record == node_count) {
            System.out.println("record is emtpy");
            return 0;
          }
          if (record >= node_count) {
            System.out.println("record is a data pointer");
            return record;
          }
          node_num = record;
        }
      }
    } catch (UnknownHostException e) {
      System.out.println("unknown host");
    }    
    return node_num;
  }
  private int[] read_node(int node_num) {
    byte b[] = new byte[node_byte_size];
    try {
      in.seek(node_num * node_byte_size);
      in.read(b);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return split_node_into_records(b);
  }
  private int[] split_node_into_records(byte b2[]) {
    int i[] = new int[2];
    short b[] = new short[node_byte_size];
    for (int j = 0;j < node_byte_size;j++) {
      b[j] = (short) (b2[j] & 255);
    } 
    if (node_byte_size == 6) {
      i[0] = (b[0] << 16) | (b[1] << 8) | b[2];
      i[1] = (b[3] << 16) | (b[4] << 8) | b[5];
    }
    if (node_byte_size == 7) {
      i[0] = (b[0] << 16) | (b[1] << 8) | b[2];
      i[0] = i[0] | (((b[3] & 0xf0)>>4)<<24);
      i[1] = (b[4] << 16) | (b[5] << 8) | b[6];
      i[1] = i[1] | ((b[3] & 0x0f)<<24);      
    }
    if (node_byte_size == 8) {
      i[0] = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
      i[1] = (b[4] << 24) | (b[5] << 16) | (b[6] << 8) | b[7];
    }
    return i;
  }
  private long find_MetadataStartMarker(RandomAccessFile in) throws IOException {
      byte buffer[] = new byte[40];
      long o = 0;
      long offset = 0;
      in.read(buffer,0,20);
      while (in.read(buffer,20,20) == 20) {        
        for (int i = 0;i < 20;i++) {
          int f = 1;
          for (int j = 0;j < MetadataStartMarker.length;j++) {
            if (buffer[i+j] != MetadataStartMarker[j]) {
              f = 0;
              break;
            }
          }
          if (f == 1) {
            offset = o + MetadataStartMarker.length;
            //System.out.println("offset:" + offset);
          }
          o = o + 1;
        }
        for (int i = 0;i < 20;i++) {
          buffer[i] = buffer[i+20];
        }
      }
      return offset;
  }
}

