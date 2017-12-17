package com.liumeng.small.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liumeng.dblibrary.entity.Commodity;
import com.liumeng.small.R;

/**
 * Created by Administrator on 2017/11/19.
 */

public class ShopListAdapter extends BaseQuickAdapter<Commodity, BaseViewHolder> {

    public ShopListAdapter(int item_specialty_layout) {
        super(item_specialty_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, Commodity item) {
        View view = helper.getView(R.id.img_product);

        helper.setText(R.id.tv_price, item.getPrice() + "")
                .setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_sales, item.getSales() + "");

        Glide.with(mContext)
                .load(getUrl(item.getImgUrl()))
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into((ImageView) view);
    }

    private String getUrl(String imgUrl) {
        String[] split = imgUrl.split("#@#");
        return split[0];
    }
}
