package com.liumeng.small;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/12/11.
 */

public class CircleImage extends android.support.v7.widget.AppCompatImageView {
    private int mRadius;
    private Paint paint;


    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRadius = getWidth() / 2;
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.GRAY);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable == null) return;
        if (getWidth() == 0 || getHeight() == 0) return;
        Bitmap bitmap = null;
        if (!(drawable instanceof BitmapDrawable)) return;
        bitmap = ((BitmapDrawable) drawable).getBitmap();

        Bitmap croppedBitmap = getCroppedBitmap(bitmap);


    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap bitmap1;
        if (bitmap.getWidth() != mRadius||bitmap.getHeight()!=mRadius){
            bitmap1 = Bitmap.createScaledBitmap(bitmap,mRadius,mRadius,false);
        }else bitmap1 = bitmap;

        Bitmap output = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        return null;
    }
}
