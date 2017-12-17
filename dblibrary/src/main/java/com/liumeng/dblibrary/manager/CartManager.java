package com.liumeng.dblibrary.manager;

import android.content.Context;

import com.liumeng.dblibrary.entity.Commodity;
import com.liumeng.dblibrary.entity.ShoppingCart;

import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */

public class CartManager extends BaseManager<ShoppingCart> {
    public CartManager(Context context) {
        super(context);
    }


    public List<ShoppingCart> QueryAll() {
        return super.QueryAll(ShoppingCart.class);

    }

    @Override
    public boolean deleteMultObject(List<ShoppingCart> objects, Class clss) {
        for (ShoppingCart cart : objects) {
            daoSession.getShoppingCartDao().deleteByKey(cart.getId());
        }
        return true;
    }
}
