package com.liumeng.small.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liumeng.dblibrary.entity.ShoppingCart;
import com.liumeng.small.R;

/**
 * Created by Administrator on 2017/11/19.
 */

public class CartListAdapter extends BaseQuickAdapter<ShoppingCart, BaseViewHolder> {

    public CartListAdapter(int item_specialty_layout) {
        super(item_specialty_layout);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ShoppingCart item) {
        View view = helper.getView(R.id.img);

        helper.setText(R.id.title, item.getTitle())
                .setText(R.id.num, item.getNum() + item.getQuantifier())
                .setText(R.id.money, (item.getPrice() * Integer.parseInt(item.getNum())) + "");
        CheckBox checkBox = (CheckBox) helper.getView(R.id.checkBox);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkedChanged != null)
                    checkedChanged.onCheckedChanged(item.getId(),helper.getLayoutPosition(), isChecked);
            }
        });

        Glide.with(mContext)
                .load(getUrl(item.getImgUrl()))
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into((ImageView) view);
    }

//    @Override
//    protected void convert(BaseViewHolder helper, Commodity item) {
//        View view = helper.getView(R.id.img_product);
//
//        helper.setText(R.id.tv_price, item.getPrice() + "")
//                .setText(R.id.tv_title, item.getTitle())
//                .setText(R.id.tv_sales, item.getSales() + "");
//
//        Glide.with(mContext)
//                .load(getUrl(item.getImgUrl()))
//                .asBitmap()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher_round)
//                .into((ImageView) view);
//    }

    private String getUrl(String imgUrl) {
        String[] split = imgUrl.split("#@#");
        return split[0];
    }

    CheckedChanged checkedChanged;

    public void setCheckedChanged(CheckedChanged checkedChanged) {
        this.checkedChanged = checkedChanged;
    }

    public interface CheckedChanged {
        void onCheckedChanged(long id,int position, boolean isChecked);
    }


}
