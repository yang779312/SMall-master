package com.liumeng.dblibrary;

import android.content.Context;

import com.liumeng.dblibrary.manager.CartManager;
import com.liumeng.dblibrary.manager.CommodityManager;
import com.liumeng.dblibrary.manager.UserManager;

/**
 * Created by Administrator on 2017/11/13.
 */

public class DBUtils {
    private static CommodityManager commodityManager;
    private static CartManager cartManager;
    private static UserManager userManager;

    public static CommodityManager getManager(Context context) {
        if (commodityManager == null) {
            synchronized (DBUtils.class) {
                if (commodityManager == null) {
                    commodityManager = new CommodityManager(context.getApplicationContext());
                }
            }
        }
        return commodityManager;
    }

    public static CartManager getCartManager(Context context) {
        if (cartManager == null) {
            synchronized (DBUtils.class) {
                if (cartManager == null) {
                    cartManager = new CartManager(context.getApplicationContext());
                }
            }
        }
        return cartManager;
    }

    public static UserManager getUserManager(Context context) {
        if (userManager == null) {
            synchronized (DBUtils.class) {
                if (userManager == null) {
                    userManager = new UserManager(context.getApplicationContext());
                }
            }
        }
        return userManager;
    }
}
