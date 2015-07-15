package com.wesoft.movedgridwithdb4o;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.wesoft.movedgridwithdb4o.view.CategoryItem;
import com.wesoft.movedgridwithdb4o.view.CustomCategory;
import com.wesoft.movedgridwithdb4o.view.CustomCategory.OnCateItemClickListener;
public class MainActivity extends AppCompatActivity {

    CustomCategory mCustomCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomCategory=(CustomCategory)findViewById(R.id.customCategory);

        mCustomCategory.setOnCateItemClickListener(new OnCateItemClickListener() {

            @Override
            public void onItemClick(CategoryItem cateGrovyItem) {
                Toast.makeText(MainActivity.this, "CateItem: " +cateGrovyItem, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
