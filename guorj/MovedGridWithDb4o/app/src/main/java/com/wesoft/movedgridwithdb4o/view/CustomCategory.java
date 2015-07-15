package com.wesoft.movedgridwithdb4o.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wesoft.movedgridwithdb4o.CategoryManage;
import com.wesoft.movedgridwithdb4o.R;
import com.wesoft.movedgridwithdb4o.dao.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CustomCategory extends LinearLayout implements OnClickListener {
	TextView txtView;
	CategoryPopWindow mPopupWindow;
	private OnCateItemClickListener mCateItemClickListener;

	/**
	 * 设置点击时间监听
	 * 
	 * @param listener
	 */
	public void setOnCateItemClickListener(OnCateItemClickListener listener) {
		this.mCateItemClickListener = listener;
	}

	/** 用户栏目列表 */
	ArrayList<CategoryItem> userCategoryist = new ArrayList<CategoryItem>();

	private static final String TAG = "MainActivity";
	private static final int GRIDVIEW_COUNT = 3;// 一行现实几个item,这个最好和cate_gridview_main布局里的个数保持一致。
	private List<MyAdapter> mGridViewAdapters = new ArrayList<MyAdapter>();
	private ViewPager mViewPager;
	private List<View> mAllViews = new ArrayList<View>();
	private MyViewPagerAdapter myViewPagerAdapter;
	private Context mContext;
	private LinearLayout ll_cateLayout;

	public CustomCategory(Context context) {
		super(context);
		init(context);
	}

	public CustomCategory(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CustomCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void init(Context context) {
		this.mContext = context;
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.cate_main, this);

		mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
		ll_cateLayout = (LinearLayout) contentView.findViewById(R.id.ll_cateLayout);
		myViewPagerAdapter = new MyViewPagerAdapter();

		txtView = (TextView) contentView.findViewById(R.id.text_edit);
		mPopupWindow = new CategoryPopWindow(mContext, new Runnable() {
			@Override
			public void run() {
				initData();
			}
		});

		txtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.showPopupWindow(ll_cateLayout);
			}
		});

		initData();

	}

	private void initData() {
		userCategoryist = ((ArrayList<CategoryItem>) CategoryManage.getManage(mContext).getUserChannel());
		Log.d("yy", "initData: " + userCategoryist.size());
		int pageCount = (userCategoryist.size() + GRIDVIEW_COUNT - 1) / GRIDVIEW_COUNT;
		Log.d(TAG, "pageCount: " + pageCount);
		mGridViewAdapters.clear();
		mAllViews.clear();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		for (int i = 0; i < pageCount; i++) {
			View mView = inflater.inflate(R.layout.cate_gridview_main, null);
			GridView mGridView = (GridView) mView.findViewById(R.id.gridview);
			MyAdapter adapter = new MyAdapter(mContext, i);
			mGridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mGridViewAdapters.add(adapter);
			mAllViews.add(mView);
		}

		mViewPager.setAdapter(myViewPagerAdapter);
		myViewPagerAdapter.notifyDataSetChanged();
	}

	private class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mAllViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			mViewPager.removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mAllViews.get(position), new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			return mAllViews.get(position);
		}
	}

	private class MyAdapter extends BaseAdapter {

		private Context mContext;
		private int pagePosition;
		private LayoutInflater mInflater;

		private MyAdapter(Context context, int pagePosition) {
			this.mContext = context;
			this.pagePosition = pagePosition;
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			int size = (userCategoryist == null ? 0 : userCategoryist.size() - GRIDVIEW_COUNT * pagePosition);
			Log.d(TAG, "size: " + size);
			if (size > GRIDVIEW_COUNT) {
				return GRIDVIEW_COUNT;
			} else {
				return size > 0 ? size : 0;
			}
		}

		@Override
		public CategoryItem getItem(int position) {
			if (userCategoryist != null && userCategoryist.size() != 0) {
				return userCategoryist.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int nowPosition = GRIDVIEW_COUNT * pagePosition + position;
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.cate_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.text_item);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon_new);
				holder.layout = (RelativeLayout) convertView.findViewById(R.id.rl_subscribe);
				convertView.setTag(holder);
			}
			holder.layout.setTag(nowPosition);
			holder.layout.setOnClickListener(CustomCategory.this);
			CategoryItem channel = getItem(nowPosition);
			holder.name.setText(channel.getName());
			return convertView;
		}
	}

	static class ViewHolder {
		RelativeLayout layout;
		ImageView icon;
		TextView name;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_subscribe:
			int id = (Integer) v.getTag();
			CategoryItem item = userCategoryist.get(id);

			if (mCateItemClickListener != null && item != null) {
				mCateItemClickListener.onItemClick(item);
			}
			break;

		default:
			break;
		}
	}

	public interface OnCateItemClickListener {
		void onItemClick(CategoryItem cateGrovyItem);
	}

}
