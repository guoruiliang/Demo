package com.tcl.gc.popgrid;

import android.app.Application;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
	}

	/** 获取Application */
	public static AppApplication getApp() {
		return mAppApplication;
	}

	public void clearAppCache() {
	}
}
