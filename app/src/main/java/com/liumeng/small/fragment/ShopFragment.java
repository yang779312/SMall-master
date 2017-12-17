package com.liumeng.small.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liumeng.dblibrary.DBUtils;
import com.liumeng.dblibrary.entity.Commodity;
import com.liumeng.small.DetailActivity;
import com.liumeng.small.R;
import com.liumeng.small.adapter.ShopListAdapter;
import com.liumeng.small.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/19.
 */

public class ShopFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_shop_list)
    RecyclerView rvShopList;
    private List<Commodity> commodities;
    private ShopListAdapter shopListAdapter;


    @Override
    public int setLayoutResource() {
        return R.layout.fragment_shop;
    }


    /**
     * 获取商品数据
     * private Long id; // id
     * private String tvName; // 名字
     * private String title; // 标题
     * private double price; // 价格
     * private String quantifier;//量词 箱/袋/盒 ...
     * private int reserve;// 库存
     * private int sales; // 销量
     * private String imgUrl;// 图片链接
     */
    public void initData() {
        Commodity commodity;
        commodities = new ArrayList<>();

        Uri uri = Uri.parse("content://com.small.business/course");
        Cursor query = getContext().getContentResolver().query(uri,
                new String[]{"id", "tvName", "title", "price", "quantifier", "reserve", "sales", "imgUrl","des","detail"}, null, null, null);
        if (query != null)
            while (query.moveToNext()) {
                commodity = new Commodity();
                commodity.setId(query.getLong(0));
                commodity.setName(query.getString(1));
                commodity.setTitle(query.getString(2));
                commodity.setPrice(query.getDouble(3));
                commodity.setQuantifier(query.getString(4));
                commodity.setReserve(query.getInt(5));
                commodity.setSales(query.getInt(6));
                commodity.setImgUrl(query.getString(7));
                commodity.setDes(query.getString(   8));
                commodity.setDetail(query.getString(9));

                commodities.add(commodity);
            }
        boolean b = DBUtils.getManager(getContext()).insertMultiObj(commodities);
        Log.i(this.getClass().getSimpleName(), "===========》 插入数据库 " + (b ? "成功" : "失败"));
        if (!b) {
            commodities = DBUtils.getManager(getContext()).QueryAll(Commodity.class);
        }
        DBUtils.getManager(getContext()).CloseDataBase();
        initView();
    }

    private void initView() {
        shopListAdapter = new ShopListAdapter(R.layout.item_specialty_layout);
        rvShopList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvShopList.setAdapter(shopListAdapter);
        shopListAdapter.bindToRecyclerView(rvShopList);
        shopListAdapter.setOnItemClickListener(this);
        setList();
    }

    /**
     * 设置商品列表
     */
    private void setList() {
        if (commodities == null || commodities.isEmpty()) {
            shopListAdapter.setEmptyView(R.layout.empty_shop_list);
        } else {
            shopListAdapter.setNewData(commodities);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DBUtils.getManager(getContext()).CloseDataBase();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        Long id = commodities.get(position).getId();
        intent.putExtra(Constant.INTENT_KEY_ID, id);
        getContext().startActivity(intent);
    }
}
