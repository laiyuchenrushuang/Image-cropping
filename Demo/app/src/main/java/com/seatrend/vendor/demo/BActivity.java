package com.seatrend.vendor.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by ly on 2020/4/9 15:12
 */
public class BActivity extends AppCompatActivity {
    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        getView();
    }

    private void getView() {
        try {
            iv = findViewById(R.id.iv);
            RectF b = MyView.getBitmapRect();
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
            iv.setImageBitmap(Bitmap.createBitmap(bmp, (int) b.left, (int) b.top, (int) b.right - (int) b.left, (int) b.bottom - (int) b.top));
//            iv.setImageBitmap(MyView.getBitmap());
            bmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
