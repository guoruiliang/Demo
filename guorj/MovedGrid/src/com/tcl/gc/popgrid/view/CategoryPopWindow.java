package com.tcl.gc.popgrid.view;

import java.util.ArrayList;

import com.tcl.gc.popgrid.CategoryManage;
import com.tcl.gc.popgrid.R;
import com.tcl.gc.popgrid.adapter.DragAdapter;
import com.tcl.gc.popgrid.adapter.OtherAdapter;
import com.tcl.gc.popgrid.dao.CategoryItem;
import com.tcl.gc.popgrid.util.DensityUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CategoryPopWindow extends PopupWindow implements
		OnItemClickListener {
	private Context mContext;
	private Activity mActivity;
	private View conentView;
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	ArrayList<CategoryItem> otherChannelList = new ArrayList<CategoryItem>();
	/** 用户栏目列表 */
	ArrayList<CategoryItem> userChannelList = new ArrayList<CategoryItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;

	TextView text_done;// 完成按钮
	/** popwindows边界距离 */
	int margin;
	private Runnable dismissRunnable;

	public CategoryPopWindow(final Context context, Runnable runnable) {
		mContext = context;
		mActivity = (Activity) context;
		this.dismissRunnable = runnable;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.cate_popup, null);

		margin = DensityUtil.dip2px(context, 8);// 获取边界值

		int h = mActivity.getWindowManager().getDefaultDisplay().getHeight();
		int w = mActivity.getWindowManager().getDefaultDisplay().getWidth() - 2
				* margin;
		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationForCate);

		text_done = (TextView) conentView.findViewById(R.id.text_done);

		userGridView = (DragGrid) conentView.findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) conentView
				.findViewById(R.id.otherGridView);
		initData();
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				saveChannel();
				if (dismissRunnable != null) {
					dismissRunnable.run();
				}
				backgroundAlpha(1);
			}
		});

		userGridView.setLongClickRunnable(new Runnable() {

			@Override
			public void run() {
				// text_done.setText("完成");//设置编辑文字显示
				// userAdapter.setIsShowDelete(true);
				// userAdapter.notifyDataSetChanged();
			}

		});
		
		text_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果处于删除按钮显示状态
				// if(userAdapter.getIsShowDelete()){
				// userAdapter.setIsShowDelete(false);
				// userAdapter.notifyDataSetChanged();
				// text_done.setText("收起");
				// }else{
				//
				// dismiss();
				// }
				//
				dismiss();
			}
		});

	}

	/**
	 * 显示popupWindow
	 * 
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			// 以下拉方式显示popupwindow
			this.showAsDropDown(parent, margin, -128);
			backgroundAlpha(0.5f);
		} else {
			this.dismiss();

		}
	}

	/** 初始化数据 */
	private void initData() {

		userChannelList = ((ArrayList<CategoryItem>) CategoryManage.getManage(
				mContext).getUserChannel());
		otherChannelList = ((ArrayList<CategoryItem>) CategoryManage.getManage(
				mContext).getOtherChannel());

		userAdapter = new DragAdapter(mContext, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(mContext, otherChannelList);
		otherGridView.setAdapter(this.otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			// 只有当有删除按钮显示时候，点击caiyouxiao
			if (userAdapter.getIsShowDelete()) {
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) {
					TextView newTextView = (TextView) view
							.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final CategoryItem channel = ((DragAdapter) parent
							.getAdapter()).getItem(position);// 获取点击的频道内容
					otherAdapter.setVisible(false);
					// 添加到最后一个
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								// 获取终点的坐标
								otherGridView.getChildAt(
										otherGridView.getLastVisiblePosition())
										.getLocationInWindow(endLocation);
								MoveAnim(moveImageView, startLocation,
										endLocation, channel, userGridView);
								userAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
			}

			break;
		case R.id.otherGridView:
			final ImageView moveImageView2 = getView(view);
			if (moveImageView2 != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final CategoryItem channel = ((OtherAdapter) parent
						.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				// 添加到最后一个
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							userGridView.getChildAt(
									userGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView2, startLocation,
									endLocation, channel, otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final CategoryItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) mActivity.getWindow()
				.getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(mContext);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(mContext);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		CategoryManage.getManage(mContext).deleteAllChannel();
		CategoryManage.getManage(mContext).saveOtherChannel(
				otherAdapter.getChannnelLst());
		CategoryManage.getManage(mContext).saveUserChannel(
				userAdapter.getChannnelLst());

	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		mActivity.getWindow().setAttributes(lp);
	}

}
