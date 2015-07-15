package com.wesoft.movedgridwithdb4o;


import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.wesoft.movedgridwithdb4o.util.Db4oHelper;
import com.wesoft.movedgridwithdb4o.util.SQLHelper;

public class AppApplication extends Application {
	public static final String LOG_TAG="DB4o";
	private static AppApplication mAppApplication;
	public static Db4oHelper mDb4oHelper;
	public static SQLHelper mSQLHelper;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
		mDb4oHelper=new Db4oHelper(this);
		mSQLHelper=new SQLHelper(this);
	}

	/** 获取Application */
	public static AppApplication getApp() {
		return mAppApplication;
	}

	public void clearAppCache() {
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		mDb4oHelper.close();
		mSQLHelper.close();
	}

	/**
	 * 方法用时
	 * @param desc
	 * @param runnable
	 */
	public static void printUsetTime(String desc,Runnable runnable){
		Log.i(LOG_TAG,desc+" start");
		long a = SystemClock.currentThreadTimeMillis();
		runnable.run();
		long b = SystemClock.currentThreadTimeMillis();
		Log.i(LOG_TAG,desc+" end use time:"+ String.valueOf(b-a));

	}
}
