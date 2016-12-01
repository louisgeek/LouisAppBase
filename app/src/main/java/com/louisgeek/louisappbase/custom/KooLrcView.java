package com.louisgeek.louisappbase.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.musicplayer.LrcHelper;
import com.louisgeek.louisappbase.musicplayer.bean.LrcLineBean;
import com.louisgeek.louisappbase.util.RawUtil;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/26.
 */

public class KooLrcView extends View {
    Context mContext;
    Paint mPaint;
    List<LrcLineBean> mLrcAllLineBeanList;
    int mNowPlayLinePos;
    float mTextHeight;
    float mAnimateChangeY;
    int mViewWidth, mViewHeight;
    float mTextCenterY;//baseline和文字中线的偏移  0,0时  就是y
    float mTextLineDis = 20;//行距
    float mHighLightTextCenterY;//画高亮文字的起始Y
    float mTextSize = 35;
    int mNormalTextColor = Color.GRAY;
    int mHighLightTextColor = Color.GREEN;
    int mUpOrDownViewCanDrawLines;//

    public KooLrcView(Context context) {
        this(context, null);
    }

    public KooLrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KooLrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        //  String lrcStr = RawUtil.getStringFromRawWithRN(mContext, R.raw.wwyxhqc);
        String lrcStr = RawUtil.getStringFromRawWithRN(mContext, R.raw.wwyxhqc);

        mLrcAllLineBeanList = LrcHelper.parseAllLrc(lrcStr);
        KLog.d("start：" + mLrcAllLineBeanList.get(0).getTimeStr() + "|" + mLrcAllLineBeanList.get(0).getTimeMillisecond());
        KLog.d("end：" + mLrcAllLineBeanList.get(mLrcAllLineBeanList.size() - 1).getTimeStr() + "|" + mLrcAllLineBeanList.get(mLrcAllLineBeanList.size() - 1).getTimeMillisecond());

        //获取文字的高度  向上取整
        mTextHeight = (float) Math.ceil(mPaint.descent() - mPaint.ascent());
               /* Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
                float fontSpacing =mPaint.getFontSpacing();
                float textHeight2 = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);*/

        KLog.d("mTextHeight"+mTextHeight);
        /**
         * baseline和文字中线的偏移  0,0时  就是y
         */
        mTextCenterY = mTextHeight / 2 - mPaint.descent();


        // handler.post(runnable);//启动歌词滚动
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = resolveSize(widthSize, widthMeasureSpec);
        int height = resolveSize(heightSize, heightMeasureSpec);

        mViewWidth = width;
        mViewHeight = height;
        KLog.d("mViewWidth:" + mViewWidth);
        KLog.d("mViewHeight:" + mViewHeight);


        mHighLightTextCenterY = mViewHeight / 2;

        float temp = mViewHeight - mTextHeight;//减去中间一行
        mUpOrDownViewCanDrawLines = (int) Math.ceil(temp / 2 / (mTextHeight + mTextLineDis));
        KLog.d("mUpOrDownViewCanDrawLines" + mUpOrDownViewCanDrawLines);
        mUpOrDownViewCanDrawLines = mUpOrDownViewCanDrawLines + 1;//多画一行
        //
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       /* int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;*/


        canvas.translate(mViewWidth / 2, mHighLightTextCenterY);


        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        /**
         * 参考中线
         */
        //  canvas.drawLine(-mViewWidth / 2, 0, mViewHeight / 2, 0, mPaint);

        /**
         *
         */
        String testText = "暂无歌词";
        //
        Rect rectOut = new Rect();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.getTextBounds(testText, 0, testText.length(), rectOut);
        //
        Rect rectNew = new Rect(-mViewWidth / 2, (int) (rectOut.top + mTextCenterY),
                mViewWidth / 2, (int) (rectOut.bottom + mTextCenterY));

        canvas.drawRect(rectNew, mPaint);


        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);

        //KLog.d("mNowPlayLinePos:"+mNowPlayLinePos);

        for (int i = 0; i < mLrcAllLineBeanList.size(); i++) {
            //
            String textContent = mLrcAllLineBeanList.get(i).getContent();

            //画  正在播放的行
            if (i == mNowPlayLinePos) {
                mPaint.setColor(mHighLightTextColor);
                canvas.drawText(textContent, 0, 0 + mTextCenterY - mAnimateChangeY, mPaint);

            } else if (i < mNowPlayLinePos) {
                //上面的行
              /*  if ((mNowPlayLinePos - i) > mUpOrDownViewCanDrawLines) {
                    continue;
                }*/

                mPaint.setColor(mNormalTextColor);
                canvas.drawText(textContent, 0,
                        0 - (mTextHeight + mTextLineDis) * (mNowPlayLinePos - i) + mTextCenterY - mAnimateChangeY, mPaint);
            } else {
                //下面的行
              /*  if ((i - mNowPlayLinePos) > mUpOrDownViewCanDrawLines) {
                    continue;
                }*/
                mPaint.setColor(mNormalTextColor);
                canvas.drawText(textContent, 0,
                        0 + (mTextHeight + mTextLineDis) * (i - mNowPlayLinePos) + mTextCenterY - mAnimateChangeY, mPaint);
            }


        }


    }

    /*Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                // long duation = 1 * 5000;
                long duation;
                if (mNowPlayLinePos == 0) {//第一行
                    duation = mLrcAllLineBeanList.get(mNowPlayLinePos).getTimeMillisecond();//00:01.13
                    // nextLine(duation);
                } else {
                    long millisecondDuation = mLrcAllLineBeanList.get(mNowPlayLinePos).getTimeMillisecond() -
                            mLrcAllLineBeanList.get(mNowPlayLinePos - 1).getTimeMillisecond();
                    nextLine(millisecondDuation);
                    duation = millisecondDuation;
                }

                *//**
                 * 第一句歌词播放完
                 *//*
                handler.postDelayed(this, duation);//00:01.13
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/

    //millisecond是定时器传过来的  会调用很多次 值一直在变
    int mLastSaveTempI = -1;
    public void setTimeMillisecond(int millisecond) {
        //KLog.d("zfq millisecond:" + millisecond);
        long LeftTime;
        long rightTime;
        long duation;
        for (int i = 0; i < mLrcAllLineBeanList.size(); i++) {
            if (i == 0) {
                LeftTime = 0;
                rightTime = mLrcAllLineBeanList.get(i).getTimeMillisecond();
            } else {
                LeftTime = mLrcAllLineBeanList.get(i - 1).getTimeMillisecond();
                rightTime = mLrcAllLineBeanList.get(i).getTimeMillisecond();
            }
            //确定是第几句
            if (millisecond >= LeftTime && millisecond < rightTime) {
                // millisecond是定时器传过来的  会调用很多次  所以要处理
                if (mLastSaveTempI != i) {
                    duation = rightTime - LeftTime;
                    nextLine(duation);
                    ///
                    mLastSaveTempI = i;
                }
                break;
            }
        }
    }

    ValueAnimator valueAnimator;

    public void nextLine(long duration) {

        KLog.d("nextLineCxq duration:"+duration);
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }
        /**
         * 播放到最后一行歌词 固定 不需要动画了
         */
        if (mNowPlayLinePos < mLrcAllLineBeanList.size() - 1) {
            /**
             * 向上切换一行动画
             */
            valueAnimator = ValueAnimator.ofFloat(0.0f, mTextHeight + mTextLineDis).setDuration(duration);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 0~mTextHeight + mTextLineDis   0~一行的高度
                    mAnimateChangeY = (float) animation.getAnimatedValue();
                    // KLog.d("mAnimateChangeY：" + mAnimateChangeY);
                    if (mAnimateChangeY==0){
                        KLog.d("nextLineC start"+System.currentTimeMillis());
                        /* do nothing */
                    }else  if (mAnimateChangeY < (mTextHeight + mTextLineDis)) {//逐渐位移1行
                        invalidate();
                    } else if (mAnimateChangeY == (mTextHeight + mTextLineDis)) { //位移1行结束时候
                        mAnimateChangeY = 0;// 当前行停顿 切换下一行 不改变上下Y   重新绘制
                        //下一行
                        mNowPlayLinePos++;
                        invalidate();//只改变下一行颜色
                        KLog.d("nextLineC end"+System.currentTimeMillis());
                    }
                }
            });
            valueAnimator.start();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float startX, startY;
        int action = event.getAction();
//        KLog.d("action:" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // mHighLightTextCenterY = event.getY();
                KLog.d("mHighLightTextCenterY:" + mHighLightTextCenterY);
                // invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(event);
    }
}
