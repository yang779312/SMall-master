package com.liumeng.small.fragment;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liumeng.dblibrary.DBUtils;
import com.liumeng.dblibrary.entity.ShoppingCart;
import com.liumeng.dblibrary.entity.User;
import com.liumeng.dblibrary.manager.CartManager;
import com.liumeng.dblibrary.manager.UserManager;
import com.liumeng.small.LoginActivity;
import com.liumeng.small.R;
import com.liumeng.small.adapter.CartListAdapter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/19.
 */

public class ShoppingCartFragment extends BaseFragment implements CartListAdapter.CheckedChanged {

    @BindView(R.id.cartList)
    RecyclerView cartList;
    @BindView(R.id.checked_all)
    CheckBox checkedAll;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.btnPay)
    Button btnPay;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.progress_text)
    TextView progressText;
    @BindView(R.id.progress)
    LinearLayout progress;
    private List<ShoppingCart> commodities;
    private CartListAdapter cartListAdapter;
    double allMoney = 0.0;
    List<Double> itemMoneys = new ArrayList<>();

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_shopping_cart;
    }

    @OnClick(R.id.btnPay)
    public void pay() {
        final TranslateAnimation animationL2R = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        final TranslateAnimation animationU2D = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f
        );
        animationL2R.setDuration(600);
        animationU2D.setDuration(300);
        animationU2D.setFillAfter(true);
        progress.setVisibility(View.VISIBLE);
        progress.setAnimation(animationL2R);
        animationL2R.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress.setAnimation(animationU2D);
                animationU2D.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        progress.setVisibility(View.GONE);
                        showOrder();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    boolean isLogin = false;
    long loginId;

    /**
     * 展示订单
     */
    private void showOrder() {
        // 判断有没有登陆
        List<User> users = DBUtils.getUserManager(getContext()).QueryAll();
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                if (user.getLogin()) {
                    isLogin = true;
                    loginId = user.getId();
                    break;
                }
            }
        }

        // 如果没有登陆先登录
        if (!isLogin) {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            // 如果登陆的话，把假装生成订单弹出来
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(R.layout.dialog_order_view);
            bottomSheetDialog.show();

            View delegateView = bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(delegateView);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            TextView name = bottomSheetDialog.findViewById(R.id.name);
            TextView id = bottomSheetDialog.findViewById(R.id.id);
            final EditText adderss = bottomSheetDialog.findViewById(R.id.address);
            TextView money = bottomSheetDialog.findViewById(R.id.money);

            User user = DBUtils.getUserManager(getActivity()).QueryById(loginId, User.class);
            name.setText(user.getName() == null ? user.getUsername() : user.getName());
            id.setText(getMD5(user.getId() + ""));
            String saddress = user.getAddress();
            if (adderss != null) {
                adderss.setText(saddress);
            }

            money.setText(allMoney + "");

            bottomSheetDialog.findViewById(R.id.dialogBuy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adderss.getText() == null || adderss.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "地址，地址，", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bottomSheetDialog.cancel();
                    Toast.makeText(getContext(), "订单已提交，包裹马上飞到您的家中 ~", Toast.LENGTH_SHORT).show();
                    buyFinish();
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

    /**
     * 买完成了
     */
    private void buyFinish() {
        CartManager cartManager = DBUtils.getCartManager(getActivity());
        List<ShoppingCart> deleteShopList = new ArrayList<>();
        for (String id : payList) {
            Long key = Long.valueOf(id);
            ShoppingCart user = cartManager.QueryById(key, ShoppingCart.class);
            deleteShopList.add(user);
        }
        cartManager.deleteMultObject(deleteShopList, ShoppingCart.class);
        initData();
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

    /**
     * 获取商品数据
     */
    public void initData() {
        CartManager manager = DBUtils.getCartManager(getContext().getApplicationContext());
        commodities = manager.QueryAll(ShoppingCart.class);
        initView();

    }

    private void initView() {
        checkedAll.setChecked(true);
        checkedAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    money.setText(getAllMoney() + "");
                    btnPay.setText(getBtnPayText((null == commodities) ? 0 : commodities.size()));
                } else {
//                    money.setText("0.0");
//                    btnPay.setText(getBtnPayText(0));
                }
            }
        });
        cartListAdapter = new CartListAdapter(R.layout.item_cart_layout);
        cartList.setLayoutManager(new LinearLayoutManager(getContext()));
        cartList.setAdapter(cartListAdapter);
        setList();
    }

    private void setList() {
        if (commodities == null && commodities.isEmpty()) {
            Toast.makeText(getContext(), "空空如也~", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        for (ShoppingCart cart : commodities) {
            double itemMoney = cart.getPrice() * Integer.parseInt(cart.getNum());
            itemMoneys.add(itemMoney);
            payList.add((i++) + "");
            allMoney += itemMoney;
        }

        btnPay.setText(getBtnPayText(commodities.size()));
        money.setText(allMoney + "");
        cartListAdapter.setNewData(commodities);
        cartListAdapter.setCheckedChanged(this);
    }

    private String getBtnPayText(int size) {
        return String.format(("结算(%d)"), size);
    }

    private double getAllMoney() {
        if (itemMoneys == null || itemMoneys.isEmpty()) return 0;
        allMoney = 0;
        for (Double aDouble : itemMoneys) {
            allMoney += aDouble;
        }
        return allMoney;
    }

    List<String> payList = new ArrayList<>();

    @Override
    public void onCheckedChanged(long id, int position, boolean isChecked) {
        Double aDouble = itemMoneys.get(position);
        allMoney = (isChecked ? (allMoney + aDouble) : (allMoney - aDouble));
        money.setText(allMoney + "");
        if (isChecked) payList.add(position, id + "");
        else payList.remove(id + "");
        checkedAll.setChecked(payList.size() == commodities.size());
    }


}
