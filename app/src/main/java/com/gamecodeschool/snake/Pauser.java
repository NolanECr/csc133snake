package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamecodeschool.snakegamegroup9.R;

public class Pauser {

    private Bitmap mBitmapPauser;
    private float mWidth;

    private float mHeight;
    private float mX;
    private float mY;

    public Pauser(Context context, float x, float y, float width, float height) {
        mBitmapPauser = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        mBitmapPauser = Bitmap.createScaledBitmap(mBitmapPauser, (int) width, (int) height, false);

        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapPauser, mX, mY, paint);
    }

    public boolean touched(float touchX, float touchY){
        if (touchX >= mX && touchX <= mX + mWidth && touchY >= mY && touchY <= mY + mHeight)
            return true;
        else return false;
    }
}
