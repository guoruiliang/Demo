package com.tcl.gc.popgrid;

import java.util.List;

import com.tcl.gc.popgrid.dao.CategoryItem;
import com.tcl.gc.popgrid.util.Db4oHelper.PausingApk;
import com.tcl.gc.popgrid.view.CustomCategory;
import com.tcl.gc.popgrid.view.CustomCategory.OnCateItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 
 */
public class TestActivity extends Activity implements OnClickListener {

	CustomCategory mCustomCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

//		mCustomCategory = (CustomCategory) findViewById(R.id.customCategory);
//		mCustomCategory.setOnCateItemClickListener(new OnCateItemClickListener() {
//
//			@Override
//			public void onItemClick(CategoryItem cateGrovyItem) {
//				Toast.makeText(MainActivity.this, "CateItem: " + cateGrovyItem, Toast.LENGTH_SHORT).show();
//
//			}
//		});

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.setPause:
				AppApplication.mDb4oHelper.saveApkPausingStatus("com.aaa", true);
				AppApplication.mDb4oHelper.saveApkPausingStatus("com.bbb", true);
			break;
		case R.id.setNotPause:
			AppApplication.mDb4oHelper.saveApkPausingStatus("com.aaa", false);
			
			break;
		case R.id.getAll:
		List<PausingApk> result=	AppApplication.mDb4oHelper.getAll(PausingApk.class);
			Log.e("zz", "getall:"+result);
			
			
		
		
			break;
		case R.id.clear:
			AppApplication.mDb4oHelper.clearApkPausingStatus();
			break;
		case R.id.test:
		Log.e("zz", "com.aaa:"+	AppApplication.mDb4oHelper.getIsPausing("com.aaa")+"  com.bbb="+	
		AppApplication.mDb4oHelper.getIsPausing("com.bbb")+"  ccc"+	AppApplication.mDb4oHelper.getIsPausing("com.cc"));
			break;
		default:
			break;
		}

	}

}
