package com.liumeng.small;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liumeng.dblibrary.DBUtils;
import com.liumeng.dblibrary.entity.Commodity;
import com.liumeng.dblibrary.entity.ShoppingCart;
import com.liumeng.dblibrary.entity.User;
import com.liumeng.dblibrary.manager.CommodityManager;
import com.liumeng.small.constant.Constant;
import com.liumeng.small.utils.NetWorkImageGetter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.detail_title)
    TextView detailTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.content_detail)
    TextView contentDetail;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private List<ImageView> imageViews;
    private Commodity commodity;
    private String[] imageUrls;
    private BottomSheetDialog bottomSheetDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        initData();

        /**网络图片路径*/
        final String htmlThree = commodity.getDetail();
//        final String htmlThree = getString(R.string.large_text);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        final NetWorkImageGetter netWorkImageGetter = new NetWorkImageGetter(widthPixels);
        try {
            contentDetail.setText(Html.fromHtml(htmlThree,
                     netWorkImageGetter, null));
            netWorkImageGetter.setLoadPicCallback(new NetWorkImageGetter.LoadPicCallback() {
                @Override
                public void loadPicSuccess() {
                    contentDetail.setText(Html.fromHtml(htmlThree,
                            Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV, netWorkImageGetter, null));
                }
            });
        } catch (Exception e) {
            contentDetail.setText(htmlThree);
        }
        initViewPage();


    }

    private void initData() {
        Long key = getIntent().getLongExtra(Constant.INTENT_KEY_ID, -1);
        if (key == -1) return;
        CommodityManager manager = DBUtils.getManager(this);
        commodity = manager.QueryById(key, Commodity.class);
        if (commodity == null) return;
        imageUrls = commodity.getImgUrl().split("#@#");

        tvPrice.setText(commodity.getPrice() + "");
        tvSales.setText(commodity.getSales() + "");
        detailTitle.setText(commodity.getTitle());

    }


    private void initViewPage() {

        // getData()
        imageViews = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);

        }

        //setadapter
        vp.setAdapter(new ViewAdapter());
    }

    /**
     *
     */
    @OnClick(R.id.fab)
    public void onViewClicked() {
//        startActivity(new Intent(this, LoginActivity.class));
        showDialog();
    }

    boolean isLogin = false;
    long loginId;

    private void showDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.dialog_buy_view);
            Button btnBuy = bottomSheetDialog.findViewById(R.id.btn_buy);
            Button btnAdd2Cart = bottomSheetDialog.findViewById(R.id.btn_add_to_car);
            ImageButton btnAdd = bottomSheetDialog.findViewById(R.id.add);
            ImageButton btnRemove = bottomSheetDialog.findViewById(R.id.remove);
            final EditText etNum = bottomSheetDialog.findViewById(R.id.num);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sNum = etNum.getText().toString();
                    final int num = (sNum == null || sNum.isEmpty()) ? 0 : Integer.valueOf(sNum);
                    etNum.setText(num + 1 + "");
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sNum = etNum.getText().toString();
                    int num = (sNum == null || sNum.isEmpty()) ? 0 : Integer.valueOf(sNum);
                    num = (num - 1) <= 0 ? 1 : (num - 1);
                    etNum.setText(num + "");
                }
            });

            btnAdd2Cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setId(commodity.getId());
                    shoppingCart.setImgUrl(commodity.getImgUrl());
                    shoppingCart.setName(commodity.getName());
                    shoppingCart.setPrice(commodity.getPrice());
                    shoppingCart.setQuantifier(commodity.getQuantifier());
                    shoppingCart.setReserve(commodity.getReserve());
                    shoppingCart.setSales(commodity.getSales());
                    shoppingCart.setTitle(commodity.getTitle());
                    shoppingCart.setNum(etNum.getText().toString());

                    DBUtils.getCartManager(DetailActivity.this).insertObj(shoppingCart);
                    Toast.makeText(DetailActivity.this, "添加成功！快去结账吧 ...", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.cancel();
                }
            });

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 判断有没有登陆
                    List<User> users = DBUtils.getUserManager(DetailActivity.this).QueryAll();
                    if (users != null && !users.isEmpty()) {
                        for (User user : users) {
                            if (user.getLogin()) {
                                isLogin = true;
                                loginId = user.getId();
                                break;
                            }
                        }
                    }// 如果没有登陆先登录
                    if (!isLogin) {
                        Toast.makeText(DetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                    } else {
                        // 如果登陆的话，把假装生成订单弹出来
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
                        bottomSheetDialog.setContentView(R.layout.dialog_order_view);
                        bottomSheetDialog.show();

                        View delegateView = bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(delegateView);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        TextView name = bottomSheetDialog.findViewById(R.id.name);
                        TextView id = bottomSheetDialog.findViewById(R.id.id);
                        final EditText adderss = bottomSheetDialog.findViewById(R.id.address);
                        TextView money = bottomSheetDialog.findViewById(R.id.money);

                        User user = DBUtils.getUserManager(DetailActivity.this).QueryById(loginId, User.class);
                        name.setText(user.getName() == null ? user.getUsername() : user.getName());
                        id.setText(getMD5(user.getId() + ""));
                        String saddress = user.getAddress();
                        if (adderss != null) {
                            adderss.setText(saddress);
                        }
                        int i = Integer.parseInt((etNum.getText().toString()));
                        money.setText((commodity.getPrice() * i) + "");

                        bottomSheetDialog.findViewById(R.id.dialogBuy).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (adderss.getText() == null || adderss.getText().toString().isEmpty()) {
                                    Toast.makeText(DetailActivity.this, "地址，地址，", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                bottomSheetDialog.cancel();
                                Toast.makeText(DetailActivity.this, "订单已提交，包裹马上飞到您的家中 ~", Toast.LENGTH_SHORT).show();

                            }
                        });
                        bottomSheetDialog.findViewById(R.id.dialogCancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheetDialog.cancel();
                            }
                        });

                    }


                }
            });
        }
        bottomSheetDialog.show();
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    private String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return "";
        }
    }

    private class ViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            Glide.with(getApplicationContext()).load(imageUrls[position]).asBitmap().into(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(imageViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
