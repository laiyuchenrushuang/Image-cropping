package com.seatrend.vendor.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ly on 2020/4/7 11:06
 */
public class MyView extends View {

    private static RectF mRect; //框内矩形图片
    private static RectF mRect1 = new RectF(); //框内矩形图片(测试 getBitmap())

    private static Bitmap cacheBitmap;

    private float mViewH, mViewW; // 自定义view 的高宽

    float left, right, top, bottom; //矩形框的长宽高

    float vfocusX, vfocusY; //中心点

    float defaultDis = 20;  //这个目的是为了可以缩放放大，要有间隔才行

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewH = getMeasuredHeight();
        mViewW = getMeasuredWidth();
        Log.d("lylog","onMeasure mViewW "+mViewW+" mViewH "+mViewH);
        //初始宽高
        left = (int) defaultDis;
        right = (int) (mViewW - defaultDis);
        top = (int) (mViewH / 2 - 40);
        bottom = (int) (mViewH / 2 + 40);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @SuppressLint({"DrawAllocation", "ResourceAsColor"})
    @Override
    protected void onDraw(Canvas canvas) {
        Paint mKuangPaint;
        //设置背景
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        RectF rect = new RectF(0, 0, bmp.getWidth(), bmp.getHeight());
        canvas.drawBitmap(bmp, null, rect, null);
        bmp.recycle();

        //画框
        mKuangPaint = new Paint();
        mKuangPaint.setColor(Color.RED);
        mKuangPaint.setStrokeWidth(5);
        mKuangPaint.setStyle(Paint.Style.STROKE);//空心矩形框
        Rect r1 = new Rect((int) left, (int) top, (int) right, (int) bottom);
        canvas.drawRect(r1, mKuangPaint);

        //画个中心点
        Paint pointP = new Paint();
        pointP.setColor(Color.BLACK);
        pointP.setStrokeWidth(5);
        vfocusX = left + (right - left) / 2;
        vfocusY = top + (bottom - top) / 2;

        canvas.drawPoint(vfocusX, vfocusY, pointP);

        float bw = bmp.getWidth();
        float bh = bmp.getHeight();

        float l = left / mViewW * bw;
        float t = top / mViewH * bh;
        float r = right / mViewW * bw;
        float b = bottom / mViewH * bh;

        mRect = new RectF(l, t, r, b);  // 相对于图片  做比例转化
//        Log.d("lylog"," mViewW "+mViewW+" mViewH "+mViewH);
//        Log.d("lylog"," bw "+bw+" bh "+bh);
        Log.d("lylog", "  " + left + "  " + top + "  " + right + "  " + bottom);
//        Log.d("lylog","  "+l+"  "+t+"  "+r+"  "+b);


        //相对于视图做比例迁移
        mRect1.left = left;
        mRect1.right = right;
        mRect1.top = top;
        mRect1.bottom = bottom;
        super.onDraw(canvas);
        
        Paint shadowP = new Paint();
        shadowP.setStyle(Paint.Style.FILL);
        shadowP.setColor(R.color.colorAccent);
        canvas.drawRect(0,0,mViewW,top,shadowP);

        canvas.drawRect(0,top,left,bottom,shadowP);
        canvas.drawRect(right,top,mViewW,bottom,shadowP);

        canvas.drawRect(0,bottom,mViewW,mViewH,shadowP);

    }


    float dl, dr, dt, db;  //  中心点到left right top bottom的边距离

    float dw, dh;  // 矩形的宽和高，当按下屏幕的时候 固定他

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();


                if (judgePiontOnView(x, y)) {

                    if (judgeRectMove()) {
                        setViewOnFocus(x, y);
                    } else {
                        Log.d("lylog", "  " + left + "  " + top + "  " + right + "  " + bottom + "  " + " mViewW " + mViewW + " mViewH " + mViewH);
                    }

                } else {

                    if (x > vfocusX) {
                        right = (int) (x - defaultDis);

                    } else {
                        left = (int) (x + defaultDis);
                    }

                    if (y > vfocusY) {
                        bottom = (int) (y - defaultDis);
                    } else {
                        top = (int) (y + defaultDis);
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN:

                dl = event.getX() - left;
                dr = right - event.getX();

                dt = event.getY() - top;
                db = bottom - event.getY();


                //触摸点与中心点的距离不变

                dw = right - left;
                dh = bottom - top;
                break;

            case MotionEvent.ACTION_UP:
                dw = right - left;
                dh = bottom - top;
                break;
        }


        invalidate();

        return true;
    }

    private void setViewOnFocus(float x, float y) {
        Log.d("lylog", " setViewOnFocus ");
//        Log.d("lylog", " x = " + x + "  y = " + y);
//        Log.d("lylog", " dw1 = " + dw + "  dh1 = " + dh);
//        Log.d("lylog", " dw2 = " + (right - left) + "  dh2 = " + (bottom - top));
//        Log.d("lylog", " vfocusX = " + vfocusX + "  vfocusY = " + vfocusY);
//        Log.d("lylog", "setViewOnFocus  left " + left + " right " + right);


        if (dw != right - left || dh != bottom - top) {  // 手指抖动 引起的误差情况 矩形长宽一变化 就没衡量的标准了
            Toast.makeText(MAPP.getContext(), "矩形宽度长度在变化不能移动", Toast.LENGTH_SHORT).show();
            return;
        }
        //X方向
        if (left > defaultDis) {
            left = (int) (x - dl);
            if (left < defaultDis) {
                left = defaultDis;
            }
        } else if (left == (int) defaultDis) {
            if (x - dl > defaultDis) {
                left = (int) (x - dl);
            }
            if (left < defaultDis) {
                left = defaultDis;
            }
        } else {
            left = (int) defaultDis;
        }

        if (right < mViewW - defaultDis) {
            right = (int) (x + dr);
            if (right > mViewW - defaultDis) {
                right = mViewW - defaultDis;
            }
        } else if (right == (int) (mViewW - defaultDis)) {
            if (x + dr < (int) (mViewW - defaultDis)) {
                right = (int) (x + dr);
            }
            if (right > mViewW - defaultDis) {
                right = mViewW - defaultDis;
            }
        } else {
            right = (int) (mViewW - defaultDis);
        }

        //Y方向
        if (top > defaultDis) {
            top = (int) (y - dt);
            if (top < defaultDis) {
                top = defaultDis;
            }
        } else if (top == defaultDis) {
            if (y - dt > defaultDis) {
                top = (int) (y - dt);
            }
            if (top < defaultDis) {
                top = defaultDis;
            }
        } else {
            top = (int) defaultDis;
        }

        if (bottom < mViewH - defaultDis) {
            bottom = (int) (y + db);
            if (bottom > mViewH - defaultDis) {
                bottom = mViewH - defaultDis;
            }
        } else if (bottom == (int) (mViewH - defaultDis)) {
            if (y + db < (int) (mViewH - defaultDis)) {
                bottom = (int) (y + db);
            }
            if (bottom > mViewH - defaultDis) {
                bottom = mViewH - defaultDis;
            }
        } else {
            bottom = (int) (mViewH - defaultDis);
        }
//        }


//        if (rw > 0 && rh > 0) {
//            int newleft, newright, newtop, newbottom;
//
//            newleft = (int) (x - rw / 2);
//            newright = (int) (x + rw / 2);
//
//            newtop = (int) (y - rh / 2);
//            newbottom = (int) (y + rh / 2);
//
//            if (newleft > 0) {
//                left = newleft;
//            }
//
//            if (newright < mViewW) {
//                right = newright;
//            }
//
//            if (newtop > 0) {
//                top = newtop;
//            }
//            if (newbottom < mViewH) {
//                bottom = newbottom;
//            }
//        }
    }

    Canvas drawBmCanvas;
    static Bitmap bmp;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("lylog"," onSizeChanged mViewW "+mViewW+" mViewH "+mViewH);
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.test).copy(Bitmap.Config.ARGB_8888, true);
        RectF rect = new RectF(0, 0, bmp.getWidth(), bmp.getHeight());
        drawBmCanvas = new Canvas(bmp);
        drawBmCanvas.drawBitmap(bmp, null, rect, null);
    }

    boolean judgePiontOnView(float x, float y) {

        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        }
        return false;
    }

    boolean judgeRectMove() {
        if (right <= mViewW - defaultDis && left >= defaultDis && top >= defaultDis && bottom <= mViewH - defaultDis) {
            if (right >= defaultDis && left <= mViewW - defaultDis / 2 && top <= mViewH - defaultDis && bottom >= defaultDis) {
                return true;
            }
        }
        return false;
    }

    public static RectF getBitmapRect() {
        return mRect;
    }

    public static Bitmap getBitmap() {
        return Bitmap.createBitmap(bmp, (int) mRect1.left, (int) mRect1.top, (int) mRect1.right - (int) mRect1.left, (int) mRect1.bottom - (int) mRect1.top);
    }
}
