/* Tencent is pleased to support the open source community by making Hippy available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tencent.mtt.hippy.views.scroll;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.tencent.mtt.hippy.HippyEngineContext;
import com.tencent.mtt.hippy.HippyInstanceContext;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.HippyViewBase;
import com.tencent.mtt.hippy.uimanager.NativeGestureDispatcher;
import com.tencent.mtt.hippy.utils.PixelUtil;
import com.tencent.mtt.hippy.views.common.HippyNestedScrollComponent.HippyNestedScrollTarget2;
import com.tencent.mtt.hippy.views.common.HippyNestedScrollHelper;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class HippyVerticalScrollView extends NestedScrollView implements HippyViewBase,
  HippyScrollView, HippyNestedScrollTarget2 {

  private NativeGestureDispatcher mGestureDispatcher;

  private boolean mScrollEnabled = true;

  private boolean mDoneFlinging;

  private boolean mDragging;

  private final HippyOnScrollHelper mHippyOnScrollHelper;

  private boolean mScrollEventEnable = true;

  private boolean mScrollBeginDragEventEnable = false;

  private boolean mScrollEndDragEventEnable = false;

  private boolean mMomentumScrollBeginEventEnable = false;

  private boolean mMomentumScrollEndEventEnable = false;

  private boolean mFlingEnabled = true;

  private boolean mPagingEnabled = false;

  protected int mScrollEventThrottle = 10;
  private long mLastScrollEventTimeStamp = -1;
  private boolean mHasUnsentScrollEvent;

  protected int mScrollMinOffset = 0;
  private int startScrollY = 0;
  private int mLastY = 0;
  private int initialContentOffset = 0;
  private boolean hasCompleteFirstBatch = false;
  private HippyVerticalScrollViewFocusHelper mFocusHelper = null;
  private final boolean isTvPlatform;
  private final Priority[] mNestedScrollPriority = { Priority.SELF, Priority.SELF, Priority.SELF, Priority.SELF };

  public HippyVerticalScrollView(Context context) {
    super(context);
    mHippyOnScrollHelper = new HippyOnScrollHelper();
    setVerticalScrollBarEnabled(false);

    HippyEngineContext hippyContext = ((HippyInstanceContext) context).getEngineContext();
    isTvPlatform = hippyContext.isRunningOnTVPlatform();
    if (isTvPlatform) {
        mFocusHelper = new HippyVerticalScrollViewFocusHelper(this);
        setFocusableInTouchMode(true);
    }
  }

  public void setScrollEnabled(boolean enabled) {
    this.mScrollEnabled = enabled;
  }

  @Override
  public void showScrollIndicator(boolean showScrollIndicator) {
    this.setVerticalScrollBarEnabled(showScrollIndicator);
  }

  public void setScrollEventThrottle(int scrollEventThrottle) {
    mScrollEventThrottle = scrollEventThrottle;
  }

  @Override
  public void callSmoothScrollTo(final int x, final int y, int duration) {
    if (duration > 0) {
      ValueAnimator realSmoothScrollAnimation =
          ValueAnimator.ofInt(getScrollY(), y);
      realSmoothScrollAnimation.setDuration(duration);
      realSmoothScrollAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          int scrollTo = (Integer) animation.getAnimatedValue();
          HippyVerticalScrollView.this.scrollTo(x, scrollTo);
        }
      });
      realSmoothScrollAnimation.start();
    } else {
      smoothScrollTo(x, y);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(
        MeasureSpec.getSize(widthMeasureSpec),
        MeasureSpec.getSize(heightMeasureSpec));
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // Call with the present values in order to re-layout if necessary
    scrollTo(getScrollX(), getScrollY());
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction() & MotionEvent.ACTION_MASK;
    if (action == MotionEvent.ACTION_DOWN && !mDragging) {
      mDragging = true;
      if (mScrollBeginDragEventEnable) {
        HippyScrollViewEventHelper.emitScrollBeginDragEvent(this);
      }
      // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
//      setParentScrollableIfNeed(false);
    } else if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) && mDragging) {
      if (mHasUnsentScrollEvent) {
        sendOnScrollEvent();
      }
      if (mScrollEndDragEventEnable) {
        HippyScrollViewEventHelper.emitScrollEndDragEvent(this);
      }

      if(mPagingEnabled) {
        post(new Runnable() {
               @Override
               public void run() {
                 doPageScroll();
               }
             }
        );
      }
      // 当手指松开时，让父控件重新获取onTouch权限
//      setParentScrollableIfNeed(true);
      mDragging = false;
    }

    if (!mDragging && isTvPlatform) {
      mFocusHelper.handleRequestFocusFromTouch(event);
    }

    boolean result = mScrollEnabled && super.onTouchEvent(event);
    if (mGestureDispatcher != null) {
      result |= mGestureDispatcher.handleTouchEvent(event);
    }
    return result;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (!mScrollEnabled) {
      return false;
    }

    int action = event.getAction() & MotionEvent.ACTION_MASK;
    if (action == MotionEvent.ACTION_DOWN) {
      startScrollY = getScrollY();
    }

    if (super.onInterceptTouchEvent(event)) {
      if (mScrollBeginDragEventEnable) {
        HippyScrollViewEventHelper.emitScrollBeginDragEvent(this);
      }
      mDragging = true;
      return true;
    }
    return false;
  }

//  // 设置父控件是否可以获取到触摸处理权限
//  private void setParentScrollableIfNeed(boolean flag) {
//    // 若自己能上下滚动
//    if (canScrollVertically(-1) || canScrollVertically(1)) {
//      getParent().requestDisallowInterceptTouchEvent(!flag);
//    }
//  }

  @Override
  public NativeGestureDispatcher getGestureDispatcher() {
    return mGestureDispatcher;
  }

  @Override
  public void setGestureDispatcher(NativeGestureDispatcher dispatcher) {
    mGestureDispatcher = dispatcher;
  }

  @Override
  protected void onScrollChanged(int x, int y, int oldX, int oldY) {
    super.onScrollChanged(x, y, oldX, oldY);
    if (mHippyOnScrollHelper.onScrollChanged(x, y)) {
      if (mScrollEventEnable) {
        long currTime;
        int offsetY = Math.abs(y - mLastY);
        if (mScrollMinOffset > 0 && offsetY >= mScrollMinOffset) {
          mLastY = y;
          sendOnScrollEvent();
        } else if ((mScrollMinOffset == 0) && ((currTime = SystemClock.elapsedRealtime()) - mLastScrollEventTimeStamp >= mScrollEventThrottle)) {
          mLastScrollEventTimeStamp = currTime;
          sendOnScrollEvent();
        } else {
          mHasUnsentScrollEvent = true;
        }
      }
      mDoneFlinging = false;
    }
  }

  private void sendOnScrollEvent() {
    mHasUnsentScrollEvent = false;
    HippyScrollViewEventHelper.emitScrollEvent(this);
  }

  private void scheduleScrollEnd() {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        if (mDoneFlinging) {
          if (mHasUnsentScrollEvent) {
            sendOnScrollEvent();
          }
          if (mMomentumScrollEndEventEnable) {
            HippyScrollViewEventHelper.emitScrollMomentumEndEvent(HippyVerticalScrollView.this);
          }
        } else {
          mDoneFlinging = true;
          postOnAnimationDelayed(this, HippyScrollViewEventHelper.MOMENTUM_DELAY);
        }
      }
    };

    postOnAnimationDelayed(runnable, HippyScrollViewEventHelper.MOMENTUM_DELAY);
  }

  protected void doPageScroll() {
    if (mMomentumScrollBeginEventEnable) {
      HippyScrollViewEventHelper.emitScrollMomentumBeginEvent(this);
    }

    smoothScrollToPage();

    scheduleScrollEnd();
  }

  @Override
  public void fling(int velocityY) {
    if (!mFlingEnabled || mPagingEnabled) {
      return;
    }

    super.fling(velocityY);

    if (mMomentumScrollBeginEventEnable) {
      HippyScrollViewEventHelper.emitScrollMomentumBeginEvent(this);
    }
    scheduleScrollEnd();
  }

  private void smoothScrollToPage() {
    int height = getHeight();
    if (height <= 0) {
      return;
    }
    View view = getChildAt(0);
    if (view == null) {
      return;
    }
    int maxPage = view.getHeight()/height;
    int page = startScrollY / height;
    int offset = getScrollY() - startScrollY;
    if (offset == 0) {
      return;
    }

    if ((page == maxPage - 1) && offset > 0) {
      page = page + 1;
    } else if (Math.abs(offset) > height/5) {
      page = (offset > 0) ? page + 1 : page - 1;
    }

    if (page < 0) {
      page = 0;
    }

    smoothScrollTo(getScrollX(), page * height);
  }

  public void setScrollEventEnable(boolean enable) {
    this.mScrollEventEnable = enable;
  }

  public void setScrollBeginDragEventEnable(boolean enable) {
    this.mScrollBeginDragEventEnable = enable;
  }

  public void setScrollEndDragEventEnable(boolean enable) {
    this.mScrollEndDragEventEnable = enable;
  }

  public void setMomentumScrollBeginEventEnable(boolean enable) {
    this.mMomentumScrollBeginEventEnable = enable;
  }

  public void setMomentumScrollEndEventEnable(boolean enable) {
    this.mMomentumScrollEndEventEnable = enable;
  }

  public void setFlingEnabled(boolean flag) {
    this.mFlingEnabled = flag;
  }

  @Override
  public void setContentOffset4Reuse(HippyMap offsetMap) {
    double offset = offsetMap.getDouble("y");
    scrollTo(0, (int) PixelUtil.dp2px(offset));
  }

  @Override
  public void setPagingEnabled(boolean pagingEnabled) {
    mPagingEnabled = pagingEnabled;
  }

  @Override
  public void setScrollMinOffset(int scrollMinOffset) {
    scrollMinOffset = Math.max(5, scrollMinOffset);
    mScrollMinOffset = (int) PixelUtil.dp2px(scrollMinOffset);
  }

  @Override
  public void setInitialContentOffset(int offset) {
    initialContentOffset = offset;
  }

  @Override
  public void scrollToInitContentOffset() {
    if (hasCompleteFirstBatch) {
      return;
    }

    if (initialContentOffset > 0) {
      scrollTo(0, initialContentOffset);
    }

    hasCompleteFirstBatch = true;
  }

  @Override
  public View focusSearch(View focused, int direction) {
    if (isTvPlatform) {
      return mFocusHelper.focusSearch(focused, direction);
    }
    return super.focusSearch(focused, direction);
  }

  @Override
  protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
    if (getVisibility() != View.VISIBLE) {
      return false;
    }

    if (isTvPlatform) {
      if (mFocusHelper.requestFocusInDescendants(direction, previouslyFocusedRect)) {
        return true;
      }
    }

    return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
  }

  @Override
  public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
    if (isTvPlatform) {
      if (!mFocusHelper.addFocusablesImp(views, direction, focusableMode)) {
        super.addFocusables(views, direction, focusableMode);
      }
    } else {
      super.addFocusables(views, direction, focusableMode);
    }
  }

  @Override
  public void requestChildFocus(View child, View focused) {
    super.requestChildFocus(child, focused);
    if (isTvPlatform) {
      mFocusHelper.scrollToFocusChild(focused);
    }
  }

  @Override
  public void setNestedScrollPriority(int direction, Priority priority) {
    mNestedScrollPriority[direction] = priority;
  }

  @Override
  public Priority getNestedScrollPriority(int direction) {
    return mNestedScrollPriority[direction];
  }

  @Override
  public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                             int type) {
    // 先给当前节点处理
    if (HippyNestedScrollHelper.priorityOfY(target, dyUnconsumed) == Priority.SELF) {
      final int oldScrollY = getScrollY();
      scrollBy(0, dyUnconsumed);
      final int myConsumed = getScrollY() - oldScrollY;
      dyConsumed += myConsumed;
      dyUnconsumed -= myConsumed;
    }
    // 再分发给父级处理
    int parentDx = HippyNestedScrollHelper.priorityOfX(this, dxUnconsumed) == Priority.NONE ? 0 : dxUnconsumed;
    int parentDy = HippyNestedScrollHelper.priorityOfY(this, dyUnconsumed) == Priority.NONE ? 0 : dyUnconsumed;
    if (parentDx != 0 || parentDy != 0) {
      dispatchNestedScroll(dxConsumed, dyConsumed, parentDx, parentDy, null, type);
    }
  }

  @Override
  public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    // 先分发给父级处理
    int parentDx = HippyNestedScrollHelper.priorityOfX(this, dx) == Priority.NONE ? 0 : dx;
    int parentDy = HippyNestedScrollHelper.priorityOfY(this, dy) == Priority.NONE ? 0 : dy;
    if (parentDx != 0 || parentDy != 0) {
      // 把consumed暂存下来，以复用数组
      int consumedX = consumed[0];
      int consumedY = consumed[1];
      consumed[0] = 0;
      consumed[1] = 0;
      dispatchNestedPreScroll(parentDx, parentDy, consumed, null, type);
      dy -= consumed[1];
      consumed[0] += consumedX;
      consumed[1] += consumedY;
    }
    // 再给当前节点处理
    if (HippyNestedScrollHelper.priorityOfY(target, dy) == Priority.PARENT) {
      final int oldScrollY = getScrollY();
      scrollBy(0, dy);
      final int myConsumed = getScrollY() - oldScrollY;
      consumed[1] += myConsumed;
    }
  }

  @Override
  public void onStopNestedScroll(@NonNull View target, int type) {
    super.onStopNestedScroll(target, type);
    if (mPagingEnabled) {
      post(new Runnable() {
        @Override
        public void run() {
          doPageScroll();
        }
      });
    }
  }
}
