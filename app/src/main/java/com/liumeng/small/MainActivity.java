package com.liumeng.small;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.liumeng.small.fragment.BaseFragment;
import com.liumeng.small.fragment.MyFragment;
import com.liumeng.small.fragment.ShopFragment;
import com.liumeng.small.fragment.ShoppingCartFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liumeng.small.constant.FragmentTag.FM_My;
import static com.liumeng.small.constant.FragmentTag.FM_SHOP;
import static com.liumeng.small.constant.FragmentTag.FM_SHOPPING_CART;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_fragment)
    FrameLayout mainFragment;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        startFragment(FM_SHOP);
        check();
    }

    private void check() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
    }

    private void initData() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_shop:
                startFragment(FM_SHOP);
                break;
            case R.id.menu_shopping_cart:
                startFragment(FM_SHOPPING_CART);
                break;
            case R.id.menu_my:
                startFragment(FM_My);
                break;
        }
        return true;
    }

    private void startFragment(String flag) {
        initFragment();
        BaseFragment baseFragment = getFragment(flag);
        if (baseFragment == null) return;
        fragmentTransaction.replace(R.id.main_fragment, baseFragment, flag).commit();
    }

    private BaseFragment getFragment(String flag) {
        BaseFragment fragment = null;
        switch (flag) {
            case FM_SHOP:
                fragment = new ShopFragment();
                break;
            case FM_SHOPPING_CART:
                fragment = new ShoppingCartFragment();
                break;
            case FM_My:
                fragment = new MyFragment();
                break;
        }
        return fragment;
    }
}
