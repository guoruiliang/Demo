package com.tcl.gc.popgrid.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Query;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Db4oHelper {

	private static Context ctx;

	private static ObjectContainer oc;

	public static final String DB_NAME="db4oDemo.data";
	
	private static ExecutorService execService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

	public Db4oHelper(Context context) {
		this.ctx=context;
	}

	
	private Configuration dbConfig() {
		Configuration c = Db4o.newConfiguration();
		// Index entries by Id
		c.objectClass(Kv.class).objectField("k").indexed(true);
		// Configure proper activation + update depth
		// TODO
		return c;
	}
	
	
	private  ObjectContainer db() {
		try {
			if (oc == null || oc.ext().isClosed()) {
				oc = Db4o.openFile(dbConfig(), db4oDBFullPath(ctx));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(Db4oHelper.class.getName(), e.toString());
		}
		return oc;
	}
	
	private String db4oDBFullPath(Context ctx) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		return Environment.getExternalStorageDirectory() + "/" + DB_NAME;
        }
        else{
        	return ctx.getDir("data", 0) + "/" + DB_NAME;
        }
	}

	
	
	

	public  <T> void save(T... ts) {
		if (ts != null && ts.length > 0) {
			for (T t : ts) {
				db().store(t);
			}
			commit();
		}
	}

	public  <T> void save(List<T> list) {
		if (list != null && !list.isEmpty()) {
			for (T t : list) {
				db().store(t);
			}
			commit();
		}
	}

	public  <T> void del(T t) {
		if (t != null) {
			db().delete(t);
			commit();
		}
	}

	public  <T> void delAll(Class<T> clazz) {
		Query q = db().query();
		q.constrain(clazz);
		ObjectSet<T> tmp = q.execute();
		if (!tmp.isEmpty()) {
			for (T u : tmp) {
				db().delete(u);
			}
			db().commit();
		}
	}
	
	public  <T> List<T> getAll(Class<T> clazz) {
		List<T> result=new ArrayList<T>();
		
		Query q = getQuery(clazz);
		ObjectSet<T> tmp = q.execute();
		if (!tmp.isEmpty()) {
			for (T u : tmp) {
				result.add(u);
			}
		}
		return result;
	}
	
	
	public  <T> List<T> getDatasByParam(Class<T> clazz,HashMap<String, Object> param) {
		List<T> result=new ArrayList<T>();
		
		Query q = getQuery(clazz);
		for (String k : param.keySet()) {
			q.descend(k).constrain(param.get(k));
		}
		
		ObjectSet<T> tmp = q.execute();
		if (!tmp.isEmpty()) {
			for (T u : tmp) {
				result.add(u);
			}
		}
		return result;
	}
	
	
	

	public  <T> void delByParam(Class<T> clazz, HashMap<String, Object> param) {
		Query q = getQuery(clazz);
		for (String k : param.keySet()) {
			q.descend(k).constrain(param.get(k));
		}
		ObjectSet<T> tmp = q.execute();
		if (!tmp.isEmpty()) {
			for (T t : tmp) {
				db().delete(t);
			}
			db().commit();
		}
	}

	public  <T> Query getQuery(Class<T> clazz) {
		Query q = db().query();
		q.constrain(clazz);
		return q;
	}

	public  void put(String k, Object v) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("k", k);
		delByParam(Kv.class, param);
		if (v != null) {
			Kv kv = new Kv();
			kv.k = k;
			kv.v = v;
			save(kv);
		}
	}

	public  void putAsync(final String k, final Object v,
			final Callback... cbs) {
		execService.execute(new Runnable() {
			public void run() {
				put(k, v);
				if (cbs != null) {
					for (Callback cb : cbs) {
						cb.done(true);
					}
				}
			}
		});
	}

	public  void getAsync(final String k, final Callback cb) {
		execService.execute(new Runnable() {
			public void run() {
				Object obj = get(k);
				if (cb != null) {
					cb.done(obj);
				}
			}
		});
	}

	public  Object get(String k) {
		Object obj = null;
		Query q = getQuery(Kv.class);
		q.descend("k").constrain(k);
		ObjectSet<Kv> tmp = q.execute();
		if (!tmp.isEmpty()) {
			obj = tmp.get(0).v;
		}
		return obj;
	}

	public  <T> int count(Class<T> clazz) {
		Query q = db().query();
		q.constrain(clazz);
		ObjectSet<T> tmp = q.execute();
		return tmp.size();
	}

	

	public  void close() {
		if (oc != null) {
			oc.close();
			oc = null;
		}
	}

	public  void commit() {
		db().commit();
	}
	
	public void rollback() {
		db().rollback();
	}

	public void deleteDatabase() {
		close();
		new File(db4oDBFullPath(ctx)).delete();
	}

	public void backup(String path) {
		db().ext().backup(path);
	}
	
	

	private static class Kv {

		public String k;
		public Object v;
	}

	public static interface Callback {
		public void done(Object obj);
	}
	
	
	
	
	/********
	 * 新成立一个类，专门用于保存apk处于暂停状态的情况，一般暂停大概需要几秒，然后就会被设置成暂停完毕，为了防止意外，比如没改变回来，导致无法继续，
	 * 需要程序启动的时候清空一下。
	 *********/

	public  static class PausingApk {

		public String packageName;
		public Boolean isPausing;

		PausingApk(String packageName, Boolean isPausing) {
			this.packageName = packageName;
			this.isPausing = isPausing;
		}

		@Override
		public String toString() {
			return "PausingApk [packageName=" + packageName + ", isPausing=" + isPausing + "]";
		}
		
		
	}

	/**
	 * 保存apk暂状态
	 * 
	 * @param packageName
	 * @param isPausing
	 */
	public void saveApkPausingStatus(String packageName, Boolean isPausing) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("packageName", packageName);
		delByParam(PausingApk.class, param);
		save(new PausingApk(packageName, isPausing));
	}

	/**
	 * 获取指定包名apk是否处于暂停
	 * @param packageName
	 * @return
	 */
	public Boolean getIsPausing(String packageName) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("packageName", packageName);
		List<PausingApk> results = getDatasByParam(PausingApk.class, param);
		if (!results.isEmpty()) {
			return results.get(0).isPausing;
		}
		return false;
	}
	
	/**
	 * 清空所有暂停状态
	 */
	public void clearApkPausingStatus() {
		delAll(PausingApk.class);
	}
	
	

}
