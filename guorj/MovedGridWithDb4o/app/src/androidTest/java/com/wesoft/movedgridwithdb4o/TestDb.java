package com.wesoft.movedgridwithdb4o;

import android.test.AndroidTestCase;
import android.util.Log;

import com.wesoft.movedgridwithdb4o.util.Db4oHelper;
import com.wesoft.movedgridwithdb4o.view.CategoryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestDb  extends AndroidTestCase{
    Db4oHelper mDb4oHelper ;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDb4oHelper=new Db4oHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDb4oHelper.close();
    }




    public void testDb4o() throws  Exception{
        AppApplication.printUsetTime("测试添加数据 ",new Runnable(){
            @Override
            public void run() {
                mDb4oHelper.save(defaultUserCategorys);
                mDb4oHelper.save(defaultOtherCategorys);
            }
        });

        AppApplication.printUsetTime("测试查询100次数据 ",new Runnable(){
            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    HashMap map = new HashMap();
                    map.put("selected", 1);
                    AppApplication.mDb4oHelper.getDatasByParam(CategoryItem.class, map);
                }
            }
        });

        AppApplication.printUsetTime("测试删除数据 ",new Runnable(){
            @Override
            public void run() {
             AppApplication.mDb4oHelper.delAll(CategoryItem.class);

            }
        });

    }







    public static List<CategoryItem> defaultUserCategorys= new ArrayList<CategoryItem>();
    public static List<CategoryItem> defaultOtherCategorys= new ArrayList<CategoryItem>();
    static {
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
}
