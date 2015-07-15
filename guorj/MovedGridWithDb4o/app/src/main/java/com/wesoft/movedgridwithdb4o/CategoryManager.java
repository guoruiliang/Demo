package com.wesoft.movedgridwithdb4o;

import android.content.Context;
import android.database.SQLException;

import com.wesoft.movedgridwithdb4o.view.CategoryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryManager {
	private static final String LOG_TAG = CategoryManager.class.getSimpleName();
	Context mContext;
	public static CategoryManager categoryManage;
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
	public static void initData(List<CategoryItem> userCates,
			List<CategoryItem> otherCates) {

	}

	private CategoryManager(Context context) throws SQLException {
		this.mContext = context;
		// 判断是否有数据，如果没有数据则初始化，如果已经有数据了，就不初始化
		if (AppApplication.mDb4oHelper.count(CategoryItem.class) <= 0) {
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
	public static CategoryManager getManage(Context context) throws SQLException {
		if (categoryManage == null)
			categoryManage = new CategoryManager(context);
		return categoryManage;
	}

	/**
	 * 清除所有的分类
	 */
	public void deleteAllChannel() {
		try {
			AppApplication.mDb4oHelper.delAll(CategoryItem.class);
		} catch (Exception e) {
		} finally {
//			AppApplication.mDb4oHelper.close();
		}
	}

	/**
	 * 获取用户选择的的分类
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的用户选择分类 : 默认用户选择分类 ;
	 */
	public List<CategoryItem> getUserChannel() {
		List<CategoryItem> result = new ArrayList<CategoryItem>();
		try {
			HashMap map = new HashMap();
			map.put("selected", 1);
			result = AppApplication.mDb4oHelper.getDatasByParam(CategoryItem.class, map);
		} catch (Exception e) {
		} finally {
//			AppApplication.mDb4oHelper.close();
		}
		return result;

	}

	/**
	 * 获取其他的分类
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的其它分类 : 默认其它分类 ;
	 */
	public List<CategoryItem> getOtherChannel() {
		List<CategoryItem> result = new ArrayList<CategoryItem>();
		try {
			HashMap map = new HashMap();
			map.put("selected", 0);
			result = AppApplication.mDb4oHelper.getDatasByParam(CategoryItem.class, map);
		} catch (Exception e) {
		} finally {
//			AppApplication.mDb4oHelper.close();
		}
		return result;

	}

	/**
	 * 保存用户分类到数据库
	 * 
	 * @param userList
	 */
	public void saveUserChannel(List<CategoryItem> userList) {
		try {
			// 需要修改slected值
			if (userList != null && !userList.isEmpty()) {
				for (CategoryItem item : userList) {
					item.selected = 1;
				}
				AppApplication.mDb4oHelper.save(userList);
			}
		} catch (Exception e) {
		} finally {
//			AppApplication.mDb4oHelper.close();
		}

	}

	/**
	 * 保存其他分类到数据库
	 * 
	 * @param otherList
	 */
	public void saveOtherChannel(List<CategoryItem> otherList) {
		try {
			// 需要修改slected值
			if (otherList != null && !otherList.isEmpty()) {
				for (CategoryItem item : otherList) {
					item.selected = 0;
				}
				AppApplication.mDb4oHelper.save(otherList);
			}
		} catch (Exception e) {
		} finally {
//			AppApplication.mDb4oHelper.close();
		}
	}

	
	/**
	 * 初始化数据库内的分类数据
	 */
	private void initDefaultChannel() {
		deleteAllChannel();
		saveUserChannel(defaultUserCategorys);
		saveOtherChannel(defaultOtherCategorys);
	}
}
