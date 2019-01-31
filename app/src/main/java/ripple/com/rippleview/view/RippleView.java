package ripple.com.rippleview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangzheng on 2019/1/30 5:17 PM.
 * E-mail : ivring11@163.com
 **/
public class RippleView extends View {
    //最小半径
    private float mixRadius;
    //最大半径
    private float maxRadius;

    private Paint paint;
    private Paint textPaint;

    private String text = "活久见";

    @ColorInt
    int color;

    private int          centerX;
    private int          centerY;
    private boolean      mIsRunning;
    private float        mDuration     = 2000;
    private Interpolator mInterpolator = new LinearInterpolator();

    private List<Circle> circleList = new ArrayList<>();

    //创建速率
    private long mRate;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Circle circle = new Circle(System.currentTimeMillis());
            circleList.add(circle);
            postDelayed(this, mRate);
        }
    };

    public RippleView(Context context) {
        super(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        maxRadius = getWidth() / 2;

        Iterator<Circle> iterator = circleList.iterator();
        while (iterator.hasNext()) {
            Circle circle        = iterator.next();
            int    alpha         = circle.getAlpha();
            float  currentRidius = circle.getCurrentRidius();
            long   creatTime     = circle.getmCreatTime();
            //如果创建时间小于 mDuration再画，剩下的移除
            if (System.currentTimeMillis() - creatTime < mDuration) {
                paint.setAlpha(alpha);
                canvas.drawCircle(centerX, centerY, currentRidius, paint);
            } else {
                iterator.remove();
            }
        }

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaint.setTextSize(50);
        canvas.drawText(text, centerX, centerY - (fontMetrics.descent + fontMetrics.ascent) / 2, textPaint);

        if (circleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    /**
     * 开始动画
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mRunnable.run();
        }
    }


    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 设置最小半径
     *
     * @param mixRadius 最小半径
     */
    public void setMixRadius(float mixRadius) {
        this.mixRadius = mixRadius;
    }

    /**
     * 设置最大半径
     * <p>
     * 未设置就是默认宽度的一半
     *
     * @param maxRadius
     */
    public void setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
    }

    /**
     * 设置动画时间
     *
     * @param mDuration
     */
    public void setmDuration(float mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * 创建波纹频率
     *
     * @param mRate 时间
     */
    public void setmRate(long mRate) {
        this.mRate = mRate;
    }

    /**
     * 设置动画插值器，可以控制波纹效果。
     * <p>
     * 设置LinearOutSlowInInterpolator 可以实现波纹渐变小的效果
     *
     * @param mInterpolator
     */
    public void setmInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    /**
     * 设置颜色，色值
     *
     * @param color
     */
    public void setColor(@ColorInt int color) {
        this.color = color;
        paint.setColor(color);
    }

    public class Circle {
        private long mCreatTime;

        public Circle(long mCreatTime) {
            this.mCreatTime = mCreatTime;
        }

        public long getmCreatTime() {
            return mCreatTime;
        }

        public int getAlpha() {
            //计算透明度，越往外越透明
            float precent = (System.currentTimeMillis() - mCreatTime) * 1.0f / mDuration;
            return (int) (255 - mInterpolator.getInterpolation(precent) * 255);
        }

        public float getCurrentRidius() {
            //计算半径，(System.currentTimeMillis() - mCreatTime) 大于 mDuration就进不来这了
            float precent = (System.currentTimeMillis() - mCreatTime) * 1.0f / mDuration;
            return mixRadius + mInterpolator.getInterpolation(precent) * (maxRadius - mixRadius);
        }
    }
}
