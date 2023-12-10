package com.gamecodeschool.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.Random;

class SnakeGame extends SurfaceView implements Runnable{

    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    private volatile boolean mGameOver = false;

    //    // for playing sound effects
    private Audio mAudioPlayer;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // How many points does the player have
    private int mScore;
    private int mHighScore;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    // A snake ssss
    private Snake mSnake;
    // And an apple and bad Apple
    private Apple mApple;
    private BadApple mBadApple;

    private Pauser mPauser;

    private Brick mBrick;

    int updateCounter = 0;

    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);

        mPauser = new Pauser(context, 2000, 50, 100, 100);

        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        //Initialize Soundplayer
        mAudioPlayer = new Audio(context);

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Call the constructors of our two game objects
            mApple = new Apple.Builder(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize)
                    .goodApple()
                    .build();

            mBadApple = new BadApple.Builder(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize)
                    .badApple()
                    .build();

        mSnake = new Snake(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);

        mBrick = new Brick(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);


    }

    // Called to start a new game
    public void newGame() {

        // reset the snake
        mGameOver = false;
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the apple ready for dinner
        mApple.spawn();

        mBadApple.spawn();

        mBrick.spawn();

        // Reset the mScore
        mScore = 0;

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
    }

    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }

            draw();
        }
    }

    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }

    // Update all the game objects
    public void update() {

        // Move the snake
        mSnake.move();
        // Make game harder after 10 points
        if (mScore >= 10){
            //every 50 iterations(5 seconds), move the BadApple to random position
            updateCounter++;
            if (updateCounter%50==0) {
                mBadApple.spawn();
            }
        }

        // Did the head of the snake eat the apple?
        if(mSnake.checkCollision(mApple.getLocation())){

            // Move apple to new location
            mApple.spawn();
            // Add to  mScore
            mScore = mScore + 1;
            // Play a sound
            mAudioPlayer.playEat();
            // Move brick to new location
            mBrick.spawn();

        }
        else if(mSnake.checkCollision(mBadApple.getLocation())){

            // Move apple to new location
            mBadApple.spawn();
            // Add to  mScore
            mScore = mScore - 1;
            // Play a sound
            mAudioPlayer.playEat();

        }
        else if(mSnake.checkCollision(mBrick.getLocation())){

            mAudioPlayer.playCrash();
            // Keep track of highscore
            if (mScore > mHighScore) {
                mHighScore = mScore;
            }

            gameOver();
            paused();
        }

        // Did the snake die?
        if (mSnake.detectDeath()) {

            mAudioPlayer.playCrash();
            // Keep track of highscore
            if (mScore > mHighScore) {
                mHighScore = mScore;
            }

            gameOver();
            paused();
        }

    }

    // Do all the drawing
    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Fill the screen with a color
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the size and color of the mPaint for the text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(120);

            // Draw the score
            mCanvas.drawText("" + mScore, 20, 120, mPaint);

            // Draw the apple and the snake
            mApple.draw(mCanvas, mPaint);
            mBadApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            mPauser.draw(mCanvas, mPaint);
            mBrick.draw(mCanvas, mPaint);

            // Draw some text while paused
            if(mPaused){

                // Set the size and color of the mPaint for the text
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(120);

                if(mGameOver){
                    mCanvas.drawText("Tap the screen to start a new game", 200, 700, mPaint);
                    // Set size of Highscore text
                    mCanvas.drawText("Game Over!", 700, 200, mPaint);
                    mCanvas.drawText("Highscore: " +mHighScore, 700, 400, mPaint);
                } else {
                    // Draw the message
                    // We will give this an international upgrade soon
                    mCanvas.drawText("Tap screen to restart game,", 100, 700, mPaint);
                    mCanvas.drawText("Pause button to resume/pause", 100, 900, mPaint);
                    // Set size of Highscore text
                    mCanvas.drawText("Highscore: " + mHighScore, 700, 400, mPaint);
                }
            }


            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    if (mPauser.touched(touchX, touchY) && !mGameOver) {
                        paused();
                    } else {
                        mPaused = false;
                        newGame();
                        }
                    // Don't want to process snake direction for this tap
                    return true;
                }

                if (mPauser.touched(touchX, touchY)) {
                    paused();
                    return true;}

                // Let the Snake class handle the input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }

    public void paused() {
        mPaused = !mPaused;
    }

    public void gameOver() {
        mGameOver = !mGameOver;
    }

    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }


    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }
}