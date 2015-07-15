package com.wesoft.movedgridwithdb4o;


import android.app.Application;

import com.wesoft.movedgridwithdb4o.util.Db4oHelper;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;
	public static Db4oHelper mDb4oHelper;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
		mDb4oHelper=new Db4oHelper(this);
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
	}
}
