package com.dsgly.bixin.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Hashtable;

import android.content.Context;

/**
 * 该类提供与数据缓存相关的方法
 */
public class CacheUtils {
	
	public static final String TAG = "CacheUtils";
	
	private Context context;
	/** 保存在内存中的缓存对象 */
	private Hashtable<String, Object> cachesInMemery = new Hashtable<>();
	/** 缓存存在的时间，默认缓存一小时，单位为毫秒 */
	private long cacheTime = 60 * 60000;

	private CacheUtils(){}

    public CacheUtils(Context context){
        this.context = context;
    }
	
	/**
	 * 构造器
	 * @param cacheTime	缓存的有效时间
	 * @param context
	 */
	public CacheUtils(long cacheTime, Context context){
		this.cacheTime = cacheTime;
		this.context = context;
	}
	
	/**
	 * 将缓存信息保存到内存中
	 * @param cacheName		缓存文件的文件名
	 * @param cacheContent	缓存文件的内容
	 */
	public void saveCacheToMemery(String cacheName, String cacheContent){
		cachesInMemery.put(cacheName, cacheContent);
	}
	
	/**
	 * 从内存中读取缓存文件的信息
	 * @param cacheName	缓存文件的文件名
	 * @return			返回缓存文件的对象
	 */
	public Object readCacheFromMemery(String cacheName){
		return cachesInMemery.get(cacheName);
	}
	
	/**
	 * 保存缓存到磁盘中
	 * @param cacheName		缓存文件的名称
	 * @param cacheContent	缓存的文件的内容
	 */
	public void saveCacheToDisk(String cacheName, String cacheContent){
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(cacheName,	Context.MODE_PRIVATE);
			fos.write(cacheContent.getBytes());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos != null){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 从磁盘中读取缓存文件
	 * @param cacheName	缓存文件的文件名
	 * @return			返回缓存文件的内容
	 */
	public String readCacheFromDisk(String cacheName){
		FileInputStream fin = null;
		try {
			fin = context.openFileInput(cacheName);
			byte[] datas = new byte[fin.available()];
			fin.read(datas);
			return new String(datas);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fin != null){
					fin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 保存对象
	 * @param serializable	需要保存的对象
	 * @param cacheName		缓存文件的文件名
	 */
	public void saveObject(Serializable serializable, String cacheName){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File cacheDir = context.getCacheDir();
		File cacheFile = new File(cacheDir, cacheName);
		try {
//			fos = context.openFileOutput(cacheName, Context.MODE_PRIVATE);
			fos = new FileOutputStream(cacheFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(serializable);
			oos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(oos != null){
					oos.close();
				}
				if(fos != null){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 加载缓存的对象
	 * @param cacheName	缓存对象的文件名
	 * @return			返回缓存的对象
	 */
	public Serializable readObject(String cacheName){
		if(!isExistDataCache(cacheName)) return null;
		FileInputStream fin = null;
		ObjectInputStream oin = null;
		File cacheDir = context.getCacheDir();
		File cacheFile = new File(cacheDir, cacheName);
		try {
//			fin = context.openFileInput(cacheName);
			fin = new FileInputStream(cacheFile);
			oin = new ObjectInputStream(fin);
			return (Serializable)oin.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(oin != null){
					oin.close();
				}
				if(fin != null){
					fin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 删除指定的文件夹下的所有缓存文件
	 * @param dir			需要进行删除的根文件夹
	 * @param currentTime	当前的时间，单位为毫秒/ms
	 * @return				返回被删除的文件的个数
	 */
	public int clearCacheFolder(File dir, long currentTime){
		int deletedFiles = 0;
		if(dir != null && dir.isDirectory()){
			for(File child : dir.listFiles()){
				if(child.isDirectory()){
					deletedFiles += clearCacheFolder(child, currentTime);
				} else if(child.lastModified() < currentTime){
					if(child.delete()){
						deletedFiles++;
					}
				}
			}
		}
		return deletedFiles;
	}

    /**
     * 删除指定名称的缓存文件
     * @param cacheName 缓存文件的名称
     */
    public void removeCache(String cacheName){
        File file = new File(context.getCacheDir(), cacheName);
        if(file.exists()){
            file.delete();
        }
    }
	
	/**
	 * 判断缓存文件是否存在
	 * @param fileName	缓存文件的保存文件名
	 * @return			如果缓存文件存在则返回true，否则返回false
	 */
	public boolean isExistDataCache(String fileName){
//		File cacheFile = context.getFileStreamPath(fileName);
		File cacheFile = new File(context.getCacheDir(), fileName);
		if(cacheFile != null && cacheFile.exists()) return true;
		return false;
	}
	
	/**
	 * 判断缓存是否失效
	 * @param cacheName	缓存的文件的文件名
	 * @return			如果缓存的文件超过了缓存的时间限制则返回true，否则返回false
	 */
	public boolean isCacheDataFailure(String cacheName){
		boolean isFailure = false;
//		File cacheFile = context.getFileStreamPath(cacheName);
		File cacheFile = new File(context.getCacheDir(), cacheName);
		if(cacheFile.exists() && (System.currentTimeMillis() - cacheFile.lastModified()) > cacheTime){
			isFailure = true;
		} else if(!cacheFile.exists()){
			isFailure = true;
		}
		return isFailure;
	}

	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
