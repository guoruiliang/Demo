//package Pigeon.db4oTester;
//
//import java.io.File;
//import java.util.List;
//import android.content.Context;
//import android.os.Environment;
//import android.util.Log;
//import com.db4o.Db4o;
//import com.db4o.ObjectContainer;
//import com.db4o.ObjectSet;
//import com.db4o.config.Configuration;
//import com.db4o.query.Query;
//
//public class db4oHelper {
//	private static ObjectContainer oc = null;
//	private Context context;
//
//	public db4oHelper(Context ctx) {
//		context = ctx;
//	}
//
//	private ObjectContainer db() {
//		try {
//			if (oc == null || oc.ext().isClosed()) {
//				oc = Db4o.openFile(dbConfig(), db4oDBFullPath(context));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e(db4oHelper.class.getName(), e.toString());
//		}
//		return oc;
//	}
//
//	private Configuration dbConfig() {
//		Configuration c = Db4o.newConfiguration();
//		// Index entries by Id
//		c.objectClass(Loger.class).objectField("id").indexed(true);
//		// Configure proper activation + update depth
//		// TODO
//		return c;
//	}
//
//	public String db4oDBFullPath(Context ctx) {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//		return Environment.getExternalStorageDirectory() + "/" + "android.db4o";
//        }
//        else{
//        	return ctx.getDir("data", 0) + "/" + "android.db4o";
//        }
//	}
//
//	public void close() {
//		if (oc != null) {
//			oc.close();
//			oc = null;
//		}
//	}
//
//	public void commit() {
//		db().commit();
//	}
//
//	public void rollback() {
//		db().rollback();
//	}
//
//	public void deleteDatabase() {
//		close();
//		new File(db4oDBFullPath(context)).delete();
//	}
//
//	public void backup(String path) {
//		db().ext().backup(path);
//	}
//
//	public void restore(String path) {
//		deleteDatabase();
//		new File(path).renameTo(new File(db4oDBFullPath(context)));
//		new File(path).delete();
//	}
//
//	// �������м�¼�ļ���
//	public List<Loger> fetchAllRows() {
//		return db().query(Loger.class);
//	}
//
//	// ��ѯ��¼����
//	public int GetLogsCount() {
//		List<Loger> logs = fetchAllRows();
//		return logs == null ? 0 : logs.size();
//	}
//
//	private ObjectSet<Loger> fetchLogsById(long Id) {
//        Query query = db().query();      
//        query.constrain(Loger.class);   
//        query.descend("id").constrain(Id);   
//        ObjectSet<Loger> list = query.execute();   
//        return list;
//	}
//
//	public Loger fetchLogById(long Id) {
//		ObjectSet<Loger> result = fetchLogsById(Id);
//		Log.i("logs",String.valueOf(result.size()));
//		if (result.hasNext())
//			return result.next();
//		else
//			return null;
//	}
//
//	public void saveLog(Loger log) {
//		db().store(log);
//		this.commit();
//	}
//	
//	public void delLog(long id){
//		Loger log=fetchLogById(id); 
//		if(log!=null){
//			db().delete(log);
//		}
//	}
//}
//
