package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.util.HexString;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * MAC address is a 6-byte, : separated HexString
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class MACAddress extends HexString implements Jsonable
{
    public static final int LENGTH = 6;
    
    public MACAddress(byte[] bytes) {
        super(bytes, ':');
    }
    
    public MACAddress(String string) {
        super(string, LENGTH, ':');
    }
    
    public MACAddress(long value) {
        super(value, LENGTH, ':');
    }
    
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(toString());
    }
    
    public static MACAddress valueOf(String string) {
        return new MACAddress(string);
    }

    public static MACAddress valueOf(byte[] bytes) {
        return new MACAddress(bytes);
    }
    
    public static MACAddress valueOf(long value) {
        return new MACAddress(value);
    }
    
    public int length() {
        return _bytes.length;
    }
    
    public boolean isBroadcast()
    {
        for(byte b : _bytes)
            if(b != -1)
                return false;
        return true;
    }

    public boolean isMulticast()
    {
        if(this.isBroadcast())
            return false;
        return (_bytes[0] & 0x01) != 0;
    }
    
    @Override
    public int hashCode() {
        return (int) toLong();
    }
}