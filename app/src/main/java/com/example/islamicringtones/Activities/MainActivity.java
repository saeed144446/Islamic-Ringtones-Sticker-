package com.example.islamicringtones.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.islamicringtones.Adapters.MyAdapter;
import com.example.islamicringtones.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE =100 ;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);



        tabLayout.addTab(tabLayout.newTab().setText("Ringtones"));
        tabLayout.addTab(tabLayout.newTab().setText("Stickers"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        myAdapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed
            }
        });



    }
}