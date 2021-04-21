package com.hjq.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.hjq.widget.R;

public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {

    //画笔
    private Paint mPaint;
    //边框画笔
    private Paint mCivMarginPaint;
    //圆形图片的半径
    private int mRadius;
    //图片的宿放比例
    private float mScale;
    //边框
    private int civBorderMargin = 0;
    //边框颜色
    private int civMarginColor = Color.WHITE;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        civBorderMargin = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_Margin,0);
        civMarginColor = typedArray.getColor(R.styleable.CircleImageView_civ_border_Margin_Color,Color.WHITE);
        typedArray.recycle();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //由于是圆形，宽高应保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = size / 2;
        setMeasuredDimension(size, size);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        mPaint = new Paint();

        Drawable drawable = getDrawable();

        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            //初始化BitmapShader，传入bitmap对象
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            //计算缩放比例
            mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());

            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            bitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(bitmapShader);
            //画圆形，指定好坐标，半径，画笔
            if (civBorderMargin == 0) {
                canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
            } else {
                mCivMarginPaint = new Paint();
                mCivMarginPaint.setStyle(Paint.Style.STROKE);
                mCivMarginPaint.setColor(civMarginColor);
                mCivMarginPaint.setStrokeWidth(civBorderMargin);
                canvas.drawCircle(mRadius, mRadius, (mRadius - civBorderMargin), mCivMarginPaint);
                canvas.drawCircle(mRadius, mRadius, (mRadius - civBorderMargin), mPaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

}