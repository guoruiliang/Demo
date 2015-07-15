package com.wesoft.movedgridwithdb4o.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wesoft.movedgridwithdb4o.R;
import com.wesoft.movedgridwithdb4o.dao.CategoryItem;

import java.util.List;


public class OtherAdapter extends BaseAdapter {
	private Context context;
	public List<CategoryItem> channelList;
	private TextView item_text;
	/** 是否可见 */
	boolean isVisible = true;
	/** 要删除的position */
	public int remove_position = -1;

	public OtherAdapter(Context context, List<CategoryItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public CategoryItem getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.cate_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		CategoryItem channel = getItem(position);
		item_text.setText(channel.getName());
		if (!isVisible && (position == -1 + channelList.size())){
			item_text.setText("");
		}
		if(remove_position == position){
			item_text.setText("");
		}
		return view;
	}
	
	/** 获取分类列表 */
	public List<CategoryItem> getChannnelLst() {
		return channelList;
	}
	
	/** 添加分类列表 */
	public void addItem(CategoryItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
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
}