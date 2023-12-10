package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

public class Shield {

    private Point mLocation = new Point();

    private Point mSpawnRange;
    private int mSize;

    private Bitmap mBitmapShield;

    Shield(Context context, Point sr, int s) {

        mSpawnRange = sr;
        mSize = s;
        mLocation.x = -10;

        mBitmapShield = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.shield);
        // Resize the bitmap
        mBitmapShield = Bitmap
                .createScaledBitmap(mBitmapShield, s, s, false);
    }

    public void defaultSpawn(){
        mLocation.x = -10;
    }

    void spawn(){
        // Choose two random values and place the Brick
        Random random = new Random();
        mLocation.x = random.nextInt(mSpawnRange.x) + 1;
        mLocation.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    Point getLocation(){
        return mLocation;
    }

    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapShield,
                mLocation.x * mSize, mLocation.y * mSize, paint);

    }
}