package com.liumeng.dblibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.liumeng.dblibrary.entity.DaoMaster;
import com.liumeng.dblibrary.entity.DaoSession;

/**
 * Created by Administrator on 2017/11/19.
 */

public class DaoManager {
    private static final DaoManager ourInstance = new DaoManager();
    private static final String DB_NAME = "shop.db";
    public static final String TAG = DaoManager.class.getSimpleName();

    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoSession mDaoSession;
    private static SQLiteDatabase db;
    private Context context;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;


    public static DaoManager getInstance() {
        return ourInstance;
    }

    private DaoManager() {

    }

    public void init(Context context) {
        this.context = context;
    }

    /**
     * 1. 创建数据库
     *
     * @return mDaoMaster
     */
    private DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
            mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
        }
        return mDaoMaster;
    }

    /**
     * 2.管理Dao 对象
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }


    public void closeDataBase() {
        closeDaoHelper();
        closeDaoSession();
    }

    private void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }


    }

    private void closeDaoHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }


}
