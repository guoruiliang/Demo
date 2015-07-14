package com.tcl.gc.popgrid;


import com.tcl.gc.popgrid.util.Db4oUtil;

import android.app.Application;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
		Db4oUtil.init(getApplicationContext());
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
		Db4oUtil.close();
	}
}
