package com.goach.tabdemo.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import android.util.AttributeSet;

import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goach.tabdemo.R;
import com.goach.tabdemo.bean.ChannelBean;
import com.goach.tabdemo.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by 钟光新 on 2016/9/24 0024.
 * 1.
 */

public class ZTabLayout extends HorizontalScrollView {
    //默认字体大小
    private final int DEFAULT_NORMAL_TEXT_SIZE_SP = AppUtils.sp2px(14);
    private int mNormalTextSize = DEFAULT_NORMAL_TEXT_SIZE_SP;
    //选中字体大小
    private final int DEFAULT_SELECT_TEXT_SIZE_SP = AppUtils.sp2px(16);
    private int mSelectTextSize = DEFAULT_SELECT_TEXT_SIZE_SP;
    //字体颜色
    private final int DEFAULT_NORMAL_TEXT_COLOR = Color.BLACK;
    private final int DEFAULT_SELECT_TEXT_COLOR = Color.RED;
    private ColorStateList mTextColor;
    //指示器高度
    private final int DEFAULT_INDICATOR_HEIGHT_DP = AppUtils.dp2px(2);
    private int mIndicatorHeight = DEFAULT_INDICATOR_HEIGHT_DP ;
    //指示器颜色
    private final int DEFAULT_INDICATOR_COLOR = Color.RED;
    private int mIndicatorColor = DEFAULT_INDICATOR_COLOR ;
    //tab最小宽度
    private final int DEFAULT_TAB_MIN_WIDTH = AppUtils.dp2px(50);
    private int mMinTabWidth = DEFAULT_TAB_MIN_WIDTH;
    //tab之间的间距
    private int mTabPadding;
    //关联的viewpager
    private ViewPager mViewPager;
    //第一个子View
    private IndicationTabLayout mTabContainer;
    //Tab总数
    private int mTabCount;
    //当前选中的Tab
    private int mCurrentTabPosition;
    //当前切换Tab的偏移量
    private float mCurrentOffset;
    //数据源
    private List<ChannelBean> mDataList;
    //中间线
    private final int DEFAULT_DIVIDER_WIDTH =AppUtils.dp2px(1);
    private int mDividerWidth = DEFAULT_DIVIDER_WIDTH;
    private final int DEFAULT_DIVIDER_COLOR = Color.GRAY;
    private int mDividerColor = DEFAULT_DIVIDER_COLOR;
    private Paint mDividerPaint;
    private int DEFAULT_DIVIDER_PADDING = AppUtils.dp2px(5);
    private int mDividerPadding = DEFAULT_DIVIDER_PADDING ;
    private boolean hasShowDivider = false ;

    //红点显示
    private final int DEFAULT_MSG_ROUND_COLOR = Color.RED;
    private int mMsgRoundColor = DEFAULT_MSG_ROUND_COLOR;
    private SparseBooleanArray mInitSetMap ;
    private SparseIntArray mMsgNumMap;
    private Paint mMsgPaint;
    private Paint mMsgNumPaint;
    private int mMsgNumColor = Color.WHITE;
    private int mMsgTextSizeSp = AppUtils.sp2px(8);
    private int mMsgPadding;

    public ZTabLayout(Context context) {
        this(context,null);
    }

    public ZTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context,attrs);
        setFillViewport(true);
        setHorizontalScrollBarEnabled(false);
        mTabContainer = new IndicationTabLayout(context);
        mTabContainer.setSelectedIndicatorColor(mIndicatorColor);
        mTabContainer.setSelectedIndicatorHeight(mIndicatorHeight);
        addView(mTabContainer,0, new HorizontalScrollView.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mDataList = new ArrayList<>();
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMsgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMsgNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInitSetMap = new SparseBooleanArray();
        mMsgNumMap = new SparseIntArray();
    }
    private void initStyle(Context context, AttributeSet attrs){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ZTabLayout,0,0);
        mNormalTextSize = typedArray.getDimensionPixelSize(R.styleable.ZTabLayout_tab_normal_textSize,DEFAULT_NORMAL_TEXT_SIZE_SP);
        mSelectTextSize = typedArray.getDimensionPixelSize(R.styleable.ZTabLayout_tab_select_textSize,DEFAULT_SELECT_TEXT_SIZE_SP);
        mTextColor = typedArray.getColorStateList(R.styleable.ZTabLayout_tab_textColor);
        if(mTextColor==null)
            mTextColor = createDefaultTextColor();
        mIndicatorHeight = (int) typedArray.getDimension(R.styleable.ZTabLayout_tab_indicatorHeight,DEFAULT_INDICATOR_HEIGHT_DP);
        mIndicatorColor = typedArray.getColor(R.styleable.ZTabLayout_tab_indicatorColor,DEFAULT_INDICATOR_COLOR);
        mMinTabWidth = typedArray.getColor(R.styleable.ZTabLayout_tab_min_width,DEFAULT_TAB_MIN_WIDTH);
        mDividerColor = typedArray.getColor(R.styleable.ZTabLayout_tab_dividerColor,DEFAULT_DIVIDER_COLOR);
        mDividerWidth = (int) typedArray.getDimension(R.styleable.ZTabLayout_tab_dividerWidth,DEFAULT_DIVIDER_WIDTH);
        mDividerPadding = (int) typedArray.getDimension(R.styleable.ZTabLayout_tab_dividerPadding,DEFAULT_DIVIDER_PADDING);
        mTabPadding = (int) typedArray.getDimension(R.styleable.ZTabLayout_tab_Padding,0);
        hasShowDivider = typedArray.getBoolean(R.styleable.ZTabLayout_tab_dividerShow,false);
        typedArray.recycle();
    }
    private ColorStateList createDefaultTextColor(){
        ColorStateList colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_selected}
                ,{0}}, new int[]{DEFAULT_SELECT_TEXT_COLOR,DEFAULT_NORMAL_TEXT_COLOR});
        return colorStateList;
    }
    public void setDataList(List<ChannelBean> dataList){
        this.mDataList.clear();
        this.mDataList.addAll(dataList);
    }
    public void setupWithViewPager(ViewPager viewPager){
        this.mViewPager = viewPager ;
        if(viewPager == null)
            throw new IllegalArgumentException("viewpager not is null");
        PagerAdapter pagerAdapter = viewPager.getAdapter() ;
        if(pagerAdapter == null)
            throw new IllegalArgumentException("pagerAdapter not is null");
        this.mViewPager.addOnPageChangeListener(new TabPagerChanger());

        mCurrentTabPosition = viewPager.getCurrentItem();
        notifyDataSetChanged();
    }
    public void notifyDataSetChanged(){
        mTabContainer.removeAllViews();
        mTabCount = mDataList.size();
        for (int i = 0 ; i<mTabCount;i++) {
            final int currentPosition = i ;
            TextView tabTextView = createTextView() ;
            tabTextView.setPadding(mTabPadding,0,mTabPadding,0);
            tabTextView.setText(mDataList.get(i).getTabName());
            tabTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(currentPosition);
                }
            });
            mTabContainer.addView(tabTextView,new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1));
        }
        setSelectedTabView(mCurrentTabPosition);
    }
    private TextView createTextView(){
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mNormalTextSize);
        textView.setTextColor(mTextColor);
        textView.setMinWidth(mMinTabWidth);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
    protected void setSelectedTabView(int position)
    {
        for (int i = 0; i < mTabCount; i++) {
            View view = mTabContainer.getChildAt(i);
            if (view instanceof TextView)
            {
                TextView textView = (TextView)view;
                textView.setSelected(position==i);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,position==i?mSelectTextSize:mNormalTextSize);
            }
        }
    }

    private void setScrollPosition(int position, float positionOffset){
        this.mCurrentTabPosition = position ;
        this.mCurrentOffset = positionOffset ;
        mTabContainer.setIndicatorPositionFromTabPosition(position, positionOffset);
        scrollTo(calculateScrollXForTab(mCurrentTabPosition,mCurrentOffset), 0);
        if(position==3)
            hideMsg(position);
    }
    private int calculateScrollXForTab(int position, float positionOffset) {
            final View selectedChild = mTabContainer.getChildAt(position);
            final View nextChild = position + 1 < mTabContainer.getChildCount()
                    ? mTabContainer.getChildAt(position + 1)
                    : null;
            final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
            final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
            return selectedChild.getLeft()
                    + ((int) ((selectedWidth + nextWidth) * positionOffset * 0.5f))
                    + (selectedChild.getWidth() / 2)
                    - (getWidth() / 2);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount <= 0) {
            return;
        }
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // 画中间线间隔
        if (mDividerWidth > 0&&hasShowDivider) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }
        //画消息提示
        for (int i = 0; i < mTabCount - 1; i++) {
            if (mInitSetMap.get(i)) {
                updateMsgPosition(canvas,mTabContainer.getChildAt(i),mMsgNumMap.get(i));
            }
        }
    }
    private void updateMsgPosition(final Canvas canvas,final View updateView,final int msgNum) {
        if(updateView == null)
            return;
        int circleX, circleY;
        if (updateView.getWidth() > 0) {
            int selectTextPadding = (int) ((updateView.getWidth()-measureTextLength(updateView))/2+0.5f);
            circleX = updateView.getRight()-selectTextPadding+mMsgPadding;
            circleY = (int) ((mTabContainer.getHeight() - measureTextHeight(updateView))/2 -mMsgPadding);
            drawMsg(canvas,circleX,circleY,msgNum);
        }
    }
    private void drawMsg(Canvas canvas,int mMsgCircleX,int mMsgCircleY,int mMsgNum){
        mMsgPaint.setStyle(Paint.Style.FILL);
        mMsgPaint.setColor(mMsgRoundColor);
        if(mMsgNum>0){
            mMsgNumPaint.setTextSize(mMsgTextSizeSp);
            mMsgNumPaint.setColor(mMsgNumColor);
            mMsgNumPaint.setTextAlign(Paint.Align.CENTER);
            String showTxt = mMsgNum>99?"99+":String.valueOf(mMsgNum);
            int mMsgNumRadius = (int) Math.max(mMsgNumPaint.descent()-mMsgNumPaint.ascent(),
                    mMsgNumPaint.measureText(showTxt))/2+AppUtils.dp2px(2);
            canvas.drawCircle(mMsgCircleX+mMsgNumRadius,mMsgCircleY,mMsgNumRadius, mMsgPaint);
            Paint.FontMetricsInt fontMetrics = mMsgNumPaint.getFontMetricsInt();
            int baseline = (int) ((2*mMsgCircleY - (fontMetrics.descent- fontMetrics.ascent))/2 - fontMetrics.ascent+0.5f);
            canvas.drawText(showTxt, mMsgCircleX+mMsgNumRadius,
                    baseline,mMsgNumPaint);
        }else{
            canvas.drawCircle(mMsgCircleX+AppUtils.dp2px(2),mMsgCircleY,AppUtils.dp2px(2), mMsgPaint);
        }
    }
    public void showMsg(int msgPosition,int msgNum,int msgPadding) {
        mInitSetMap.put(msgPosition,true);
        this.mMsgNumMap.put(msgPosition,msgNum);
        mMsgPadding = msgPadding;
        ViewCompat.postInvalidateOnAnimation(this);
    }
    public void hideMsg(int msgPosition) {
        mInitSetMap.put(msgPosition,false);
        this.mMsgNumMap.delete(msgPosition);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private float measureTextLength(View measureView){
        if(measureView instanceof TextView){
            TextView textView = ((TextView)measureView);
            String text =textView .getText().toString();
            return textView.getPaint().measureText(text);
        }
        return 0;
    }
    private float measureTextHeight(View measureView){
        if(measureView instanceof TextView){
            TextView textView = ((TextView)measureView);
            Paint textPaint =textView.getPaint();
            return textPaint.descent()-textPaint.ascent();
        }
        return 0;
    }
    private class TabPagerChanger implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setScrollPosition(position,positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            setSelectedTabView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
