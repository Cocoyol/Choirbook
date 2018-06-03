package com.cocoyol.apps.choirbook.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class WebViewInverter extends FrameLayout {
    public WebViewInverter(@NonNull Context context) {
        super(context);
    }

    public WebViewInverter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewInverter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WebViewInverter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean nightMode = false;

    private Paint paint = new Paint();
    private ColorFilter cf;
    private Rect inversionRect;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //super.dispatchDraw(canvas);

        inversionRect = new Rect(0, 0, getWidth(), getHeight());
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cc = new Canvas(b);
        super.dispatchDraw(cc);
        paint.setColorFilter(null);
        canvas.drawBitmap(b, 0, 0, paint);
        paint.setColorFilter(cf);
        canvas.drawBitmap(b, inversionRect, inversionRect, paint);
    }

    private void init(){
        float[] mat = new float[]
                {
                        -1,  0,  0, 0,  255,
                        0, -1,  0, 0,  255,
                        0,  0, -1, 0,  255,
                        0,  0,  0, 1,  0
                };
        cf = new ColorMatrixColorFilter(new ColorMatrix(mat));
    }
}
