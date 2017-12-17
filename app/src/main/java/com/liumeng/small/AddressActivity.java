package com.liumeng.small;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.liumeng.dblibrary.DBUtils;
import com.liumeng.dblibrary.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.changeaddress)
    Button changeaddress;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.modifsex)
    Button modifsex;
    @BindView(R.id.modifycontact)
    Button modifycontact;
    @BindView(R.id.addmails)
    Button addmails;
    @BindView(R.id.mail)
    EditText mail;
    @BindView(R.id.sex)
    EditText sex;
    @BindView(R.id.contact)
    EditText contact;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        long id = getIntent().getLongExtra("id", 0);
        user = DBUtils.getUserManager(this).QueryById(id, User.class);

        if (user.getAddress() != null) {
            address.setText(user.getAddress());
        }
        if (user.getSex() != null) {
            sex.setText(user.getSex());
        }
        if (user.getContact() != null) {
            contact.setText(user.getContact());
        }
        if (user.getQQ() != null) {
            mail.setText(user.getQQ());
        }

    }

    @OnClick(R.id.changeaddress)
    public void onViewClicked() {
        if (user != null && address.getText() != null) {
            user.setAddress(address.getText().toString());
            DBUtils.getUserManager(this).updateObject(user);
        }
        finish();
    }

    @OnClick(R.id.modifsex)
    public void onSetSex() {
        if (user != null && sex.getText() != null) {
            user.setSex(sex.getText().toString());
            DBUtils.getUserManager(this).updateObject(user);
        }
        finish();
    }

    @OnClick(R.id.addmails)
    public void onSetmail() {
        if (user != null && mail.getText() != null) {
            user.setQQ(mail.getText().toString());
            DBUtils.getUserManager(this).updateObject(user);
        }
        finish();
    }

    @OnClick(R.id.modifycontact)
    public void onSetContastc() {
        if (user != null && contact.getText() != null) {
            user.setContact(contact.getText().toString());
            DBUtils.getUserManager(this).updateObject(user);
        }
        finish();
    }


}
