package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BearTrap {

    private Point location = new Point();
    private Point spawnRange;
    private int blockSize;
    private Bitmap bitmapBearTrap;
    private long spawnTime;
    private boolean active;
    private int duration; // New variable to store the duration in milliseconds

    public BearTrap(Context context, Point spawnRange, int blockSize, int duration) {
        this.blockSize = blockSize;
        this.duration = duration;
        bitmapBearTrap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bear_trap);
        bitmapBearTrap = Bitmap.createScaledBitmap(bitmapBearTrap, blockSize, blockSize, false);
        this.spawnRange = spawnRange;
        location.x = -10;
        spawnTime = 0;
        active = false;
    }

    public void spawn(long currentTime) {
        // Generate a random number between 0 and 1
        double randomChance = Math.random();

        // Set the spawn chance (e.g., 20%)
        double spawnChance = 0.9;

        // Check if the random number is less than the spawn chance
        if (randomChance < spawnChance) {
            // If true, spawn the bear trap
            Random random = new Random();
            location.x = random.nextInt(spawnRange.x) + 1;
            location.y = random.nextInt(spawnRange.y - 1) + 1;
            spawnTime = currentTime;
            active = true;
        }
    }

    public Point getLocation() {
        return location;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (active) {
            canvas.drawBitmap(bitmapBearTrap, location.x * blockSize, location.y * blockSize, paint);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isExpired(long currentTime) {
        return active && (currentTime - spawnTime > duration);
    }

    public void update(long currentTime) {
        if (isExpired(currentTime)) {
            deactivate();
        }
    }
}
