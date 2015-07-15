package com.wesoft.movedgridwithdb4o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 其他分类显示用到的adapter
 *
 */
public class OtherGridView extends GridView {

	public OtherGridView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
   