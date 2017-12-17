package com.liumeng.dblibrary.manager;

import android.content.Context;

import com.liumeng.dblibrary.entity.ShoppingCart;
import com.liumeng.dblibrary.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */

public class UserManager extends BaseManager<User> {
    public UserManager(Context context) {
        super(context);
    }

    public List<User> QueryAll() {
        return super.QueryAll(User.class);
    }
}
