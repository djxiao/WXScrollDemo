package com.djxiao.wxscrolldemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.djxiao.wxscrolldemo.R;

/**
 * @author djxiao
 * @create 2016/8/3 14:00
 * @DESC  颜色渐变
 */
public class ColorGradient extends View{

    private Bitmap mBitmap;
    private Paint mPaint;
    private Canvas mCanvas;

    /** 图标 **/
    private Bitmap mIconBitmap;

    /**  包含图标的最小矩形 **/
    private Rect mIconBound;

    /** 颜色 **/
    private int mColor = 0xFF45C01A;
    /**  透明度  0-1之间 **/
    private float mAlpha = 0f;

    /** 文本 **/
    private String mText = "";

    /** 文本大小 **/
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10,getResources().getDisplayMetrics());

    /** 文本画笔 **/
    private Paint mTextPaint;

    /**包含文本的最小矩形**/
    private Rect mTextBound = new Rect();

    public ColorGradient(Context context) {
        super(context);
    }

    public ColorGradient(Context context, AttributeSet attrs) {
        super(context, attrs);

        /** 获取自定义属性**/
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorGradient);
        int n = a.getIndexCount();
        for(int i = 0; i<n;i++){
            int arr = a.getIndex(i);
            switch (arr){
                case R.styleable.ColorGradient_micon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(arr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.ColorGradient_mcolor:
                    mColor = a.getColor(arr,0x45C01A);
                    break;
                case R.styleable.ColorGradient_mtext:
                    mText = a.getString(arr);
                    break;
                case R.styleable.ColorGradient_text_size:
                    mTextSize = (int) a.getDimension(arr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();//注意要回收

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()-getPaddingRight(),
                getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());
        //使图标居中
        int top = (getMeasuredHeight()-mTextBound.height())/2 - bitmapWidth/2;
        int left = getMeasuredWidth()/2 - bitmapWidth/2;

        mIconBound = new Rect(left,top,left+bitmapWidth,top+bitmapWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int alpha = (int) Math.ceil(255 * mAlpha);

        canvas.drawBitmap(mIconBitmap,null,mIconBound,null);
        setupTargetBitmap(alpha);
        drawSrcText(canvas,alpha);
        drawDesText(canvas,alpha);
        canvas.drawBitmap(mBitmap,0,0,null);

    }

    private void setupTargetBitmap(int alpha){

        mBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconBound,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap,null,mIconBound,mPaint);

    }

    private void drawSrcText(Canvas canvas,int alpha){

        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        canvas.drawText(mText,mIconBound.left+mIconBound.width()/2-mTextBound.width()/2,mIconBound.bottom+mTextBound.height(),mTextPaint);

    }

    private void drawDesText(Canvas canvas,int alpha){
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        canvas.drawText(mText,mIconBound.left+mIconBound.width()/2-mTextBound.width()/2,mIconBound.bottom+mTextBound.height(),mTextPaint);
    }

    public void setAlpha(float alpha){
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView(){
        if(Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    public void setColor(int color){
        this.mColor = color;
    }

    public void setIcon(int resId){
        this.mIconBitmap = BitmapFactory.decodeResource(getResources(),resId);
        if(mIconBound != null){
            invalidateView();
        }
    }

    public void setIcon(Bitmap bitmap){
        mIconBitmap = bitmap;
        if(mIconBound != null){
            invalidateView();
        }
    }

    private static final String INSTANCE_STATE = "instance_state";
    private static final String STATE_ALPHA = "state_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i("Tag","onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE,super.onSaveInstanceState());
        bundle.putFloat(STATE_ALPHA,mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.i("Tag","onRestoreInstanceState");
        super.onRestoreInstanceState(state);
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATE_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        }else{
            super.onRestoreInstanceState(state);
        }
    }
}
