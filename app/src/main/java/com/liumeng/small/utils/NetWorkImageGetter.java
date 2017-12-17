package com.liumeng.small.utils;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/25.
 */

public class NetWorkImageGetter implements Html.ImageGetter {
    private static final String TAG = NetWorkImageGetter.class.getSimpleName();
    /**
     * 网络图片name
     */
    private String picName = "networkPic.jpg";
    private int width;

    public NetWorkImageGetter(int width) {
        this.width = width;
    }

    @Override
    public Drawable getDrawable(String source) {
        Drawable drawable = null;
        picName = getFileName(source);
        File file = new File(Environment.getExternalStorageDirectory(), picName);
        Log.d(TAG, getFileName(source));
        if (source.startsWith("http")) {

            // 判断路径是否存在
            if (file.exists()) {
                // 存在即获取drawable
                drawable = Drawable.createFromPath(file.getAbsolutePath());
                int height = (width / drawable.getIntrinsicWidth()) * drawable.getIntrinsicHeight();

                drawable.setBounds(0, 0, width, height);
            } else {
                // 不存在即开启异步任务加载网络图片
                AsyncLoadNetworkPic networkPic = new AsyncLoadNetworkPic();
                networkPic.execute(source);
            }
        }
        return drawable;
    }


    private String downloadUrl;

    /**
     * 获取文件名
     *
     * @param path 下载路径
     * @return文件名
     */
    public String getFileName(String path) {
        try {
            HttpURLConnection conn;
            this.downloadUrl = path;
            URL url = new URL(this.downloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/') + 1);
            if (filename == null || "".equals(filename.trim())) {//如果获取不到文件名称
                for (int i = 0; ; i++) {
                    String mine = conn.getHeaderField(i);
                    if (mine == null) break;
                    if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
                        Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                        if (m.find()) return m.group(1);
                    }
                }
                filename = UUID.randomUUID() + ".tmp";//默认取一个文件名
            }
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载网络图片异步类
     *
     * @author Susie
     */
    private final class AsyncLoadNetworkPic extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // 加载网络图片
            loadNetPic(params);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // 当执行完成后再次为其设置一次
            if (loadPicCallback != null) loadPicCallback.loadPicSuccess();
        }

        /**
         * 加载网络图片
         */
        private void loadNetPic(String... params) {
            String path = params[0];

            File file = new File(Environment.getExternalStorageDirectory(), picName);

            InputStream in = null;

            FileOutputStream out = null;

            try {
                URL url = new URL(path);

                HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();

                connUrl.setConnectTimeout(5000);

                connUrl.setRequestMethod("GET");

                if (connUrl.getResponseCode() == 200) {

                    in = connUrl.getInputStream();

                    out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];

                    int len;

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } else {
                    Log.i(TAG, connUrl.getResponseCode() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private LoadPicCallback loadPicCallback;

    public void setLoadPicCallback(LoadPicCallback loadPicCallback) {
        this.loadPicCallback = loadPicCallback;
    }

    public interface LoadPicCallback {
        void loadPicSuccess();
    }
}