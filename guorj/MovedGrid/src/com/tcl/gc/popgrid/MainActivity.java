package com.tcl.gc.popgrid;

import com.tcl.gc.popgrid.dao.CategoryItem;
import com.tcl.gc.popgrid.view.CustomCategory;
import com.tcl.gc.popgrid.view.CustomCategory.OnCateItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	CustomCategory mCustomCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCustomCategory = (CustomCategory) findViewById(R.id.customCategory);
		mCustomCategory.setOnCateItemClickListener(new OnCateItemClickListener() {

			@Override
			public void onItemClick(CategoryItem cateGrovyItem) {
				Toast.makeText(MainActivity.this, "CateItem: " + cateGrovyItem, Toast.LENGTH_SHORT).show();

			}
		});

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		default:
			break;
		}

	}

}
