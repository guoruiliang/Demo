package com.tcl.gc.popgrid;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import android.os.Environment;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.tcl.gc.popgrid.dao.CategoryItem;

public class CategoryManage {
	private EmbeddedObjectContainer db;

	public static CategoryManage categoryManage;
	/**
	 * 默认的用户选择分类列表
	 * */
	public static List<CategoryItem> defaultUserCategorys;
	/**
	 * 默认的其他分类列表
	 * */
	public static List<CategoryItem> defaultOtherCategorys;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;

	/**
	 * 这里写上默认的数据，比如开始没有网络的情况下，给的一些默认分类
	 * 
	 */
	static {
		defaultUserCategorys = new ArrayList<CategoryItem>();
		defaultOtherCategorys = new ArrayList<CategoryItem>();
		defaultUserCategorys.add(new CategoryItem(1, "推荐", 1, 1));
		defaultUserCategorys.add(new CategoryItem(2, "热点", 2, 1));
		defaultUserCategorys.add(new CategoryItem(3, "娱乐", 3, 1));
		defaultUserCategorys.add(new CategoryItem(4, "时尚", 4, 1));
		defaultUserCategorys.add(new CategoryItem(5, "科技", 5, 1));
		defaultUserCategorys.add(new CategoryItem(6, "体育", 6, 1));
		defaultUserCategorys.add(new CategoryItem(7, "军事", 7, 1));
		defaultOtherCategorys.add(new CategoryItem(8, "财经", 1, 0));
		defaultOtherCategorys.add(new CategoryItem(9, "汽车", 2, 0));
		defaultOtherCategorys.add(new CategoryItem(10, "房产", 3, 0));
		defaultOtherCategorys.add(new CategoryItem(11, "社会", 4, 0));
		defaultOtherCategorys.add(new CategoryItem(12, "情感", 5, 0));
		defaultOtherCategorys.add(new CategoryItem(13, "女人", 6, 0));
		defaultOtherCategorys.add(new CategoryItem(14, "旅游", 7, 0));
		defaultOtherCategorys.add(new CategoryItem(15, "健康", 8, 0));
		defaultOtherCategorys.add(new CategoryItem(16, "美女", 9, 0));
		defaultOtherCategorys.add(new CategoryItem(17, "游戏", 10, 0));
		defaultOtherCategorys.add(new CategoryItem(18, "数码", 11, 0));

	}

	/**
	 * 重新初始化数据库的数据
	 * <P>
	 * 这个后面在加，从网络获取到分类后，初始化，需要更新数据库中的内容，保存用户已经选择的，删除没有的分类。
	 * 
	 * @param userCates
	 * @param otherCates
	 */
	public static void initData(List<CategoryItem> userCates, List<CategoryItem> otherCates) {

	}

	private CategoryManage() throws SQLException {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Environment.getExternalStorageDirectory() + "/"
				+ "db4oTOp.data");
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * 初始化分类管理类
	 * 
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static CategoryManage getManage() throws SQLException {
		if (categoryManage == null)
			categoryManage = new CategoryManage();
		return categoryManage;
	}

	/**
	 * 清除所有的分类
	 */
	public void deleteAllChannel() {
		ObjectSet<CategoryItem> result = db.queryByExample(new CategoryItem());
		while (result.hasNext()) {
			CategoryItem categoryItem = result.next();
			db.delete(categoryItem);
			db.commit();
		}

	}

	/**
	 * 获取用户选择的的分类
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的用户选择分类 : 默认用户选择分类 ;
	 */
	public List<CategoryItem> getUserChannel() {

		Query query = db.query();
		query.constrain(new CategoryItem());
		query.descend("selected").constrain("1");
		ObjectSet<CategoryItem> result = query.execute();
		if (result.size() > 0) {
			userExist = true;
			List<CategoryItem> list = new ArrayList<CategoryItem>();
			while (result.hasNext()) {
				list.add(result.next());
			}
			return list;
		}

		initDefaultChannel();
		return defaultUserCategorys;
	}

	/**
	 * 获取其他的分类
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的其它分类 : 默认其它分类 ;
	 */
	public List<CategoryItem> getOtherChannel() {

		Query query = db.query();
		query.constrain(new CategoryItem());
		query.descend("selected").constrain("0");
		ObjectSet<CategoryItem> result = query.execute();
		List<CategoryItem> list = null;
		if (result.size() > 0) {
			list = new ArrayList<CategoryItem>();
			while (result.hasNext()) {
				list.add(result.next());
			}
			return list;
		}

		if (userExist) {
			return list;
		}
		return (List<CategoryItem>) defaultOtherCategorys;
	}

	/**
	 * 保存用户分类到数据库
	 * 
	 * @param userList
	 */
	public void saveUserChannel(List<CategoryItem> userList) {
		for (CategoryItem channelItem : userList) {
			db.store(channelItem);
			db.commit();
		}

	}

	/**
	 * 保存其他分类到数据库
	 * 
	 * @param otherList
	 */
	public void saveOtherChannel(List<CategoryItem> otherList) {
		for (CategoryItem channelItem : otherList) {
			db.store(channelItem);
			db.commit();
		}

	}

	/**
	 * 初始化数据库内的分类数据
	 */
	private void initDefaultChannel() {
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserCategorys);
		saveOtherChannel(defaultOtherCategorys);
	}
}
