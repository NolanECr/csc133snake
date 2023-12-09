package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

public class Brick {

    private Point mLocation = new Point();

    private Point mSpawnRange;
    private int mSize;

    private Bitmap mBitmapBrick;

    Brick(Context context, Point sr, int s){

        mSpawnRange = sr;
        mSize = s;
        mLocation.x = -10;

        mBitmapBrick = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.brick);
        // Resize the bitmap
        mBitmapBrick = Bitmap
                .createScaledBitmap(mBitmapBrick, s, s, false);
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
        canvas.drawBitmap(mBitmapBrick,
                mLocation.x * mSize, mLocation.y * mSize, paint);

    }
}
