package com.tcl.gc.popgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.SQLException;
import android.os.Environment;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.tcl.gc.popgrid.dao.CategoryItem;
import com.tcl.gc.popgrid.util.Db4oUtil;

public class CategoryManage {
	private static final String LOG_TAG=CategoryManage.class.getSimpleName();
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
		//判断是否有数据，如果没有数据则初始化，如果已经有数据了，就不初始化
		if(Db4oUtil.count(CategoryItem.class)<=0){
			initDefaultChannel();
		}
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
		long a=System.currentTimeMillis();
		Db4oUtil.delAll(CategoryItem.class);
		long b=System.currentTimeMillis()-a;
		Log.e("yy", "deleteAllChannel time:"+b);

	}

	/**
	 * 获取用户选择的的分类
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的用户选择分类 : 默认用户选择分类 ;
	 */
	public List<CategoryItem> getUserChannel() {
		
		long a=System.currentTimeMillis();
		List<CategoryItem> list=null ;
		HashMap map=new HashMap();
		map.put("selected", 1);
		list= Db4oUtil.getDatasByParam(CategoryItem.class,map);
		long b=System.currentTimeMillis()-a;
		Log.e("yy", "getUserChannel time:"+b);
		return list;
	}

	/**
	 * 获取其他的分类
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的其它分类 : 默认其它分类 ;
	 */
	public List<CategoryItem> getOtherChannel() {
		long a=System.currentTimeMillis();
		List<CategoryItem> list=null ;
		HashMap map=new HashMap();
		map.put("selected", 0);
		list= Db4oUtil.getDatasByParam(CategoryItem.class,map);
		long b=System.currentTimeMillis()-a;
		Log.e("yy", "getOtherChannel time:"+b);
		return list;
	}

	/**
	 * 保存用户分类到数据库
	 * 
	 * @param userList
	 */
	public void saveUserChannel(List<CategoryItem> userList) {
		long a=System.currentTimeMillis();
		
		Db4oUtil.save(userList);
		long b=System.currentTimeMillis()-a;
		Log.e("yy", "saveUserChannel time:"+b);
	}

	/**
	 * 保存其他分类到数据库
	 * 
	 * @param otherList
	 */
	public void saveOtherChannel(List<CategoryItem> otherList) {
		long a=System.currentTimeMillis();
		Db4oUtil.save(otherList);
		long b=System.currentTimeMillis()-a;
		Log.e("yy", "saveOtherChannel time:"+b);
	}

	
	/**
	 * 初始化数据库内的分类数据
	 */
	private void initDefaultChannel() {
		Log.d(LOG_TAG, "deleteAll");
		long a=System.currentTimeMillis();
		deleteAllChannel();
		saveUserChannel(defaultUserCategorys);
		saveOtherChannel(defaultOtherCategorys);
		long b=System.currentTimeMillis()-a;
		Log.e("yy", "initDefaultChannel time:"+b);
	}
}
