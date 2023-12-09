package com.gamecodeschool.snake;
//test

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.gamecodeschool.snakegamegroup9.R;

import java.util.Random;

class Apple {

    // The location of the apple on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the apple
    private Bitmap mBitmapApple;

    /// Set up the apple in the constructor
    private Apple(Builder builder){

        // Make a note of the passed in spawn range
        mSpawnRange = builder.mSpawnRange;
        // Make a note of the size of an apple
        mSize = builder.mSize;
        // Hide the apple off-screen until the game starts
        location.x = -10;

        bitmap(builder.mContext, builder.mAppleImage);
    }

    void bitmap(Context context, int appleImage) {
        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), appleImage);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, mSize, mSize, false);
    }


    // This is called every time an apple is eaten
    void spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the apple
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapApple,
                location.x * mSize, location.y * mSize, paint);

    }

    //Use builder class to separate construction of good/bad apples
    public static class Builder {
        private final Context mContext;
        private final Point mSpawnRange;
        private final int mSize;
        private int mAppleImage;

        public Builder(Context context, Point spawnRange, int size) {
            mContext = context;
            mSpawnRange = spawnRange;
            mSize = size;
        }

        //create a good apple with good attributes
        public Builder goodApple() {
            mAppleImage = R.drawable.apple;
            return this;
        }

        //create a bad apple with bad attributes
        public Builder badApple() {
            mAppleImage = R.drawable.apple_core;
            return this;
        }

        public Apple build() {
            return new Apple(this);
        }
    }

}