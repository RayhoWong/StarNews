package com.rayho.tsxiu.activity;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.message)
    TextView mTextMessage;
    @BindView(R.id.toolbarMain)
    Toolbar mToolBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterSetContentView() {
        hideBaseToolbar();
        mToolBar.setNavigationIcon(R.mipmap.arrow_back);
        mToolBar.setTitle("首页");
        mToolBar.setLogo(R.mipmap.ic_launcher);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "haha", Toast.LENGTH_SHORT).show();
            }
        });
        mToolBar.inflateMenu(R.menu.setting_menu);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.search:
                        Intent intent = new Intent(MainActivity.this,TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favor:
                        break;
                    case R.id.like:
                        break;
                }
                return false;
            }
        });



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
