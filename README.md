# Image-cropping



/**
 * Created by ly on 2020/4/7 11:06
 */

public class MyView extends View {

    private static RectF mRect; //框内矩形图片

    private static Bitmap cacheBitmap;

    private float mViewH, mViewW; // 自定义view 的高宽

    int left, right, top, bottom; //矩形框的长宽高

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

        //初始宽高
        left = (int) defaultDis;
        right = (int) (mViewW - defaultDis);
        top = (int) (mViewH / 2 - 40);
        bottom = (int) (mViewH / 2 + 40);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("lylog", "   onDraw  ");
        Paint mKuangPaint;
        //设置背景
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        RectF rect = new RectF(0, 0, mViewW, mViewH);
        canvas.drawBitmap(bmp, null, rect, null);


        //画框
        mKuangPaint = new Paint();
        mKuangPaint.setColor(Color.RED);
        mKuangPaint.setStrokeWidth(5);
        mKuangPaint.setStyle(Paint.Style.STROKE);//空心矩形框
        Rect r1 = new Rect(left, top, right, bottom);
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

        float l = left/mViewW *bw;
        float t = top/mViewH *bh;
        float r = right/mViewW *bw;
        float b = bottom/mViewH *bh;

        mRect = new RectF(l,t,r,b);
        
        Paint pb = new Paint();
        cacheBitmap = Bitmap.createBitmap(right-left,bottom-top, Bitmap.Config.ARGB_8888);
        Canvas bc = new Canvas(cacheBitmap);
        bc.save();
        Matrix m = new Matrix();
        m.setScale(left,top,right,bottom);
        bc.clipRect(r1);
        bc.drawBitmap(cacheBitmap,m,new Paint());
        bc.restore();

        super.onDraw(canvas);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();

            if (judgePiontOnView(x, y)) {

                if (judgeRectMove()) {
                    setViewOnFocus(x, y);
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
            postInvalidate();
        }
        return true;
    }

    private void setViewOnFocus(float x, float y) {
        Log.d("lylog", " setViewOnFocus");
        int rw = right - left;
        int rh = bottom - top;

        int newleft, newright, newtop, newbottom;

        newleft = (int) (x - rw / 2);
        newright = (int) (x + rw / 2);

        newtop = (int) (y - rh / 2);
        newbottom = (int) (y + rh / 2);

        if (newleft > 0) {
            left = newleft;
        }
        if (newright < mViewW) {
            right = newright;
        }
        if (newtop > 0) {
            top = newtop;
        }
        if (newbottom < mViewH) {
            bottom = newbottom;
        }
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    boolean judgePiontOnView(float x, float y) {

        if (x > left && x < right && y > top && y < bottom) {
            return true;
        }
        return false;
    }

    boolean judgeRectMove() {
        if (right < mViewW && left > 0 && top > 0 && bottom < mViewH) {
            return true;
        }
        return false;
    }

    public static RectF getBitmapRect() {
        return mRect;
    }

    public static Bitmap getBitmap() {
        return cacheBitmap;
    }


}






    class BBctivity :BaseActivty(){
  
        override fun initView() {

        val b = MyView.getBitmapRect()
        val bmp = BitmapFactory.decodeResource(resources, R.mipmap.test)
       //iv.setImageBitmap(Bitmap.createBitmap(bmp,b.left.toInt(),b.top.toInt(),b.right.toInt()-

        b.left.toInt(),b.bottom.toInt()-b.top.toInt()))
        
        iv.setImageBitmap(MyView.getBitmap())
    }

    override fun getLayOut(): Int {
        return R.layout.activity_b
    }

    @Subscribe
    override fun onRev(base: BaseEntity) {
    }

}

