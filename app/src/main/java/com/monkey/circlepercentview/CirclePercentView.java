package com.monkey.circlepercentview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形百分比自定义View
 */
public class CirclePercentView extends View {

    private int mCircleColor;
    private int mArcColor;
    private int mArcWidth;
    private int mPercentTextColor;
    private int mPercentTextSize;
    private int mRadius;
    private float mCurPercent = 0.0f;

    private Paint mCirclePaint;
    private Paint mArcPaint;
    private Paint mPercentTextPaint;
    private Rect mTextBound;
    private RectF mArcRectF;

    private OnClickListener mOnClickListener;

    public void setCurPercent(float curPercent) {
        ValueAnimator anim = ValueAnimator.ofFloat(mCurPercent, curPercent);
        anim.setDuration((long) (Math.abs(mCurPercent - curPercent) * 20));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCurPercent = (float) (Math.round(value * 10)) / 10;//四舍五入保留到小数点后两位
                invalidate();
            }
        });
        anim.start();
    }

    public void setOnCircleClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mCircleColor = ta.getColor(R.styleable.CirclePercentView_circleBg, 0xff8e29fa);
        mArcColor = ta.getColor(R.styleable.CirclePercentView_arcColor, 0xffffee00);
        mArcWidth = ta.getDimensionPixelSize(R.styleable.CirclePercentView_arcWidth, DensityUtils.dp2px(context, 16));
        mPercentTextColor = ta.getColor(R.styleable.CirclePercentView_arcColor, 0xffffee00);
        mPercentTextSize = ta.getDimensionPixelSize(R.styleable.CirclePercentView_percentTextSize, DensityUtils.sp2px(context, 16));
        mRadius = ta.getDimensionPixelSize(R.styleable.CirclePercentView_radius, DensityUtils.dp2px(context, 100));
        ta.recycle();

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mCircleColor);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);//使圆弧两头圆滑

        mPercentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentTextPaint.setStyle(Paint.Style.STROKE);
        mPercentTextPaint.setColor(mPercentTextColor);
        mPercentTextPaint.setTextSize(mPercentTextSize);

        mArcRectF = new RectF();//圆弧的外接矩形

        mTextBound = new Rect();//文本的范围矩形

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(CirclePercentView.this);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 2 * mRadius;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mCirclePaint);

        //画圆弧
        mArcRectF.set(getWidth() / 2 - mRadius + mArcWidth / 2, getHeight() / 2 - mRadius + mArcWidth / 2
                , getWidth() / 2 + mRadius - mArcWidth / 2, getHeight() / 2 + mRadius - mArcWidth / 2);
        canvas.drawArc(mArcRectF, 270, 360 * mCurPercent / 100, false, mArcPaint);

        String text = mCurPercent + "%";
        //计算文本宽高
        mPercentTextPaint.getTextBounds(text, 0, String.valueOf(text).length(), mTextBound);
        //画百分比文本
        canvas.drawText(text, getWidth() / 2 - mTextBound.width() / 2
                , getHeight() / 2 + mTextBound.height() / 2, mPercentTextPaint);
    }
}
