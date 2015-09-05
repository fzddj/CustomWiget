package com.ddj.customwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by dingdj on 2015/9/5.
 */
public class DynamicListView extends ListView implements AdapterView.OnItemLongClickListener {


    /**
     * ��ǰ�ƶ���view
     */
    private BitmapDrawable mMobileViewDrawable;

    /**
     * ��ǰ�ƶ���view��λ��
     */
    private Rect mMobileViewDrawableBounds;

    /**
     * ��ǰ�ƶ���ǰһ��ItemId
     */
    private long mMobileAboveItemId = -1;

    /**
     * ��ǰ�ƶ����һ��Itemid
     */
    private long mMobileBelowItemId = -1;

    /**
     * ��ָ��X����ֵ
     */
    private float mTouchX;

    /**
     * ��ָ��Y����ֵ
     */
    private float mTouchY;


    public DynamicListView(Context context) {
        super(context);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        View selectedView = view;
        mMobileViewDrawable = getDrawableFromView(selectedView);
        selectedView.setVisibility(View.INVISIBLE);
        updateNeibourId();
        return true;
    }

    private void updateNeibourId() {
        int position = getSelectedItemPosition();
        if(position > 0) {
            mMobileAboveItemId = getItemIdAtPosition(position--);
        }

        if(position < getCount() - 1) {
            mMobileAboveItemId = getItemIdAtPosition(position++);
        }
    }

    private BitmapDrawable getDrawableFromView(View selectedView) {
        Bitmap bitmap = selectedView.getDrawingCache();
        int top = selectedView.getTop();
        int left = selectedView.getLeft();
        int bottom = selectedView.getBottom();
        int right = selectedView.getRight();
        mMobileViewDrawableBounds = new Rect(left, top, right, bottom);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getContext().getResources(), bitmap);
        bitmapDrawable.setBounds(mMobileViewDrawableBounds);
        return bitmapDrawable;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mMobileViewDrawable != null) {
            mMobileViewDrawable.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurrentTouchX = ev.getX();
                float mCurrentTouchY = ev.getY();
                float deltaX = mCurrentTouchX - mTouchX;
                float deltaY = mCurrentTouchY - mTouchY;
                if(mMobileViewDrawableBounds != null) {
                    mMobileViewDrawableBounds.left = (int)(mMobileViewDrawableBounds.left + deltaX);
                    mMobileViewDrawableBounds.right = (int)(mMobileViewDrawableBounds.right + deltaX);
                    mMobileViewDrawableBounds.top = (int)(mMobileViewDrawableBounds.top + deltaY);
                    mMobileViewDrawableBounds.bottom = (int)(mMobileViewDrawableBounds.bottom + deltaY);
                }
                if(mMobileViewDrawable != null) {
                    mMobileViewDrawable.setBounds(mMobileViewDrawableBounds);
                }
                mTouchX = mCurrentTouchX;
                mTouchY = mCurrentTouchY;
                //�ж��Ƿ�Ҫ��������Itemλ��





                invalidate();


                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(ev);
    }

}
