package com.liumeng.small.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liumeng.dblibrary.DBUtils;
import com.liumeng.dblibrary.entity.User;
import com.liumeng.small.AboutActivity;
import com.liumeng.small.AddressActivity;
import com.liumeng.small.LoginActivity;
import com.liumeng.small.R;
import com.liumeng.small.SettingActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/19.
 */

public class MyFragment extends BaseFragment {

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.tv_mail)
    TextView tvMail;
    @BindView(R.id.action_setting)
    ImageView actionSetting;
    @BindView(R.id.address)
    LinearLayout address;
    @BindView(R.id.about)
    LinearLayout about;

    private boolean isLogin = false;

    private User user;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_my;
    }


    /**
     * 获取商品数据
     */
    public void initData() {
        List<User> users = DBUtils.getUserManager(getContext()).QueryAll();
        if (users == null && users.isEmpty() && users.size() == 0) {
            // 没有用户
            tvName.setText("点击头像登录");

        }
        for (User user : users) {
            if (user.getLogin()) {
                this.user = user;
                isLogin = true;
                setUser();
            }
        }


    }

    private void setUser() {
        String name = user.getName();
        if (name == null) name = user.getUsername();
        tvMail.setText(user.getUsername());
        String avatarPath = user.getAvatar();
        if (avatarPath != null)
            Glide.with(this).fromFile().load(new File(avatarPath))
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .centerCrop()
                    .into(avatar);
        this.tvName.setText(name);
    }

    @OnClick({R.id.avatar, R.id.name, R.id.action_setting, R.id.address, R.id.about})
    public void onViewClicked(View view) {
        if (!isLogin) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }

        switch (view.getId()) {
            case R.id.avatar:
                toSetting();
                break;
            case R.id.name:
                break;
            case R.id.action_setting:
                toSetting();
                break;
            case R.id.address:
                toAddress();
                break;
            case R.id.about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
        }
    }


    private void toSetting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra("key", user.getId());
        startActivity(intent);
    }

    private void toAddress() {
        Intent intent = new Intent(getActivity(), AddressActivity.class);
        if (user == null) return;
        intent.putExtra("id", user.getId());
        startActivity(intent);
    }
}
