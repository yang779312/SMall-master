package com.liumeng.dblibrary.manager;

import android.content.Context;
import android.util.Log;

import com.liumeng.dblibrary.DaoManager;
import com.liumeng.dblibrary.entity.DaoSession;
import com.liumeng.dblibrary.entity.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/11/18.
 */

public class BaseManager<T> {
    public static final String TAG = BaseManager.class.getSimpleName();
    public DaoManager manager;
    public DaoSession daoSession;

    public BaseManager(Context context) {
        manager = DaoManager.getInstance();
        manager.init(context);
        daoSession = manager.getDaoSession();
    }

    // insert
    public boolean insertObj(T obj) {
        return manager.getDaoSession().insertOrReplace(obj) != -1;
    }

    // insert list
    public boolean insertMultiObj(final List<T> objs) {
        if (objs == null || objs.isEmpty()) return false;
        try {
            manager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T obj : objs) {
                        daoSession.insertOrReplace(obj);
                    }
                }
            });
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 删除表
     *
     * @param clss
     * @return 是否删除成功
     */
    public boolean deleteAll(Class clss) {
        boolean flag = false;
        try {
            manager.getDaoSession().deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;

    }

    public void deleteObject(T t) {
        try {
            daoSession.delete(t);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 异步批量删除数据
     *
     * @param objects
     * @return
     */
    public boolean deleteMultObject(final List<T> objects, Class clss) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {
            daoSession.getDao(clss).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    // 改=====================================================================================

    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     *
     * @param object
     * @return
     */
    public void updateObject(T object) {

        if (null == object) {
            return;
        }
        try {
            manager.getDaoSession().update(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 批量更新数据
     *
     * @param objects
     * @return
     */
    public void updateMultObject(final List<T> objects, Class clss) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {

            daoSession.getDao(clss).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    // 查=====================================================================================

    /**
     * 获得某个表名
     *
     * @return
     */
    public String getTableName(Class clss) {
        return daoSession.getDao(clss).getTablename();
    }

    /**
     * 查询某个ID的对象是否存在
     *
     * @param clss
     * @param id
     * @return
     */
    public boolean isContentExit(long id, Class clss) {
        QueryBuilder<T> qb = (QueryBuilder<T>) daoSession.getDao(clss).queryBuilder();
        qb.where(UserDao.Properties.Id.eq(id));
        long length = qb.buildCount().count();
        return length > 0 ? true : false;
    }

    /**
     * 根据主键ID来查询
     *
     * @param id
     * @return
     */
    public T QueryById(long id, Class object) {
        return (T) daoSession.getDao(object).loadByRowId(id);
    }

    /**
     * 查询某条件下的对象
     *
     * @param object
     * @return
     */
    public List<T> QueryObject(Class object, String where, String... params) {
        Object obj = null;
        List<T> objects = null;
        try {
            obj = daoSession.getDao(object);
            if (null == obj) {
                return null;
            }
            objects = daoSession.getDao(object).queryRaw(where, params);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return objects;
    }

    /**
     * 查询所有对象
     *
     * @param object
     * @return
     */
    public List<T> QueryAll(Class object) {
        List<T> objects = null;
        try {
            objects = (List<T>) daoSession.getDao(object).loadAll();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return objects;
    }

    /***************************关闭数据库*************************/
    /**
     * 关闭数据库一般在Odestory中使用
     */
    public void CloseDataBase() {
        manager.closeDataBase();
    }

}
