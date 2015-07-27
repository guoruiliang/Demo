package com.tcl.gc.popgrid.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcl.gc.popgrid.R;
import com.tcl.gc.popgrid.dao.CategoryItem;

public class DragAdapter extends BaseAdapter {
	/** TAG */
	private final static String TAG = "DragAdapter";
	/** 是否显示底部的ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;
	/** 是否可见 */
	boolean isVisible = true;
	/** 可以拖动的列表（即用户选择的分类列表） */
	public List<CategoryItem> channelList;
	/** TextView 分类内容 */
	private TextView item_text;
	
	
	
	/** 要删除的position */
	public int remove_position = -1;

	private ImageView image_delete;
	private boolean isShowDelete=true;// 根据这个判断是否显示删除图标

	public DragAdapter(Context context, List<CategoryItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	public void setIsShowDelete(boolean isShowDelete){
		this.isShowDelete=isShowDelete;
	}

	public boolean getIsShowDelete(){
		return this.isShowDelete;
	}
	
	@Override
	public CategoryItem getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.cate_item,
				null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		image_delete=(ImageView)view.findViewById(R.id.image_delete);
		
		image_delete.setVisibility(isShowDelete?View.VISIBLE:View.GONE);
		
		CategoryItem channel = getItem(position);
		item_text.setText(channel.getName());
		// if ((position == 0) || (position == 1)){
		// //
		// item_text.setTextColor(context.getResources().getColor(R.color.black));
		// item_text.setEnabled(false);
		// }
		if (isChanged && (position == holdPosition) && !isItemShow) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if (remove_position == position) {
			item_text.setText("");
		}
		return view;
	}

	/** 添加分类列表 */
	public void addItem(CategoryItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 拖动变更分类排序 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		CategoryItem dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition="
				+ dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	/** 获取分类列表 */
	public List<CategoryItem> getChannnelLst() {
		return channelList;
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** 删除分类列表 */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	/** 设置分类列表 */
	public void setListDate(List<CategoryItem> list) {
		channelList = list;
	}

	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}

	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** 显示放下的ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}