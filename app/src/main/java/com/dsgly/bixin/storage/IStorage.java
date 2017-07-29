package com.dsgly.bixin.storage;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * Created by bjdengxuan1 on 2017/6/22.
 */

public abstract interface IStorage
{
    public abstract boolean putSerializable(String paramString, Serializable paramSerializable);

    public abstract boolean putBytes(String paramString, byte[] paramArrayOfByte);

    public abstract boolean putInt(String paramString, int paramInt);

    public abstract boolean putShort(String paramString, short paramShort);

    public abstract boolean putLong(String paramString, long paramLong);

    public abstract boolean putFloat(String paramString, float paramFloat);

    public abstract boolean putDouble(String paramString, double paramDouble);

    public abstract boolean putString(String paramString1, String paramString2);

    public abstract boolean putBoolean(String paramString, boolean paramBoolean);

    public abstract <T extends Serializable> T getSerializable(String paramString, Class<T> paramClass, T paramT);

    public abstract int getInt(String paramString, int paramInt);

    public abstract double getDouble(String paramString, double paramDouble);

    public abstract float getFloat(String paramString, float paramFloat);

    public abstract short getShort(String paramString, short paramShort);

    public abstract long getLong(String paramString, long paramLong);

    public abstract String getString(String paramString1, String paramString2);

    public abstract boolean getBoolean(String paramString, boolean paramBoolean);

    public abstract byte[] getBytes(String paramString, byte[] paramArrayOfByte);

    public abstract boolean remove(String paramString);

    public abstract boolean contains(String paramString);

    public abstract Map<String, Object> getAll();

    public abstract List<String> getKeys();

    public abstract boolean cleanAllStorage();
}

