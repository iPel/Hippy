package com.tencent.mtt.hippy.views.scroll;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import com.tencent.mtt.hippy.annotation.HippyController;
import com.tencent.mtt.hippy.annotation.HippyControllerProps;
import com.tencent.mtt.hippy.common.HippyArray;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.HippyGroupController;
import com.tencent.mtt.hippy.utils.PixelUtil;
import com.tencent.mtt.hippy.views.list.HippyListView;
import com.tencent.mtt.supportui.views.recyclerview.BaseLayoutManager;
import java.lang.ref.WeakReference;

@SuppressWarnings({"deprecation", "unused", "rawtypes"})
@HippyController(name = HippyScrollViewController.CLASS_NAME)
public class HippyScrollViewController<T extends ViewGroup & HippyScrollView> extends
    HippyGroupController {

  protected static final String SCROLL_TO = "scrollTo";
  private static final String SCROLL_TO_WITHOPTIONS = "scrollToWithOptions";

  public static final String CLASS_NAME = "ScrollView";

  private Handler mScrollHandler = new Handler(Looper.getMainLooper());
  private ScrollObserveRunnable mScrollRunnable = new ScrollObserveRunnable();

  @Override
  protected View createViewImpl(Context context, HippyMap iniProps) {
    boolean enableScrollEvent = false;
    boolean isHorizontal = false;
    if (iniProps != null) {
      isHorizontal = iniProps.getBoolean("horizontal");
      enableScrollEvent = iniProps.getBoolean("onScroll");
    }

    View scrollView;
    if (isHorizontal) {
      scrollView = new HippyHorizontalScrollView(context);
    } else {
      scrollView = new HippyVerticalScrollView(context);
    }
    ((HippyScrollView)scrollView).setScrollEventEnable(enableScrollEvent);
    initScrollListener(scrollView);
    return scrollView;
  }

  @Override
  protected View createViewImpl(Context context) {
    return null;
  }

  private void initScrollListener(View view) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
      view.setOnScrollChangeListener(new OnScrollChangeListener() {
        @Override
        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
          mScrollRunnable.setTargetView(view);
          mScrollHandler.removeCallbacks(mScrollRunnable);
          mScrollHandler.postDelayed(mScrollRunnable, 200);
        }
      });
    }
  }

  @HippyControllerProps(name = "scrollEnabled", defaultType = HippyControllerProps.BOOLEAN, defaultBoolean = true)
  public void setScrollEnabled(HippyScrollView view, boolean flag) {
    view.setScrollEnabled(flag);
  }

  @HippyControllerProps(name = "showScrollIndicator", defaultType = HippyControllerProps.BOOLEAN)
  public void setShowScrollIndicator(HippyScrollView view, boolean flag) {
    view.showScrollIndicator(flag);
  }

  @HippyControllerProps(name = "onScrollBeginDrag", defaultType = HippyControllerProps.BOOLEAN)
  public void setScrollBeginDragEventEnable(HippyScrollView view, boolean flag) {
    view.setScrollBeginDragEventEnable(flag);
  }

  @HippyControllerProps(name = "onScrollEndDrag", defaultType = HippyControllerProps.BOOLEAN)
  public void setScrollEndDragEventEnable(HippyScrollView view, boolean flag) {
    view.setScrollEndDragEventEnable(flag);
  }

  @HippyControllerProps(name = "onMomentumScrollBegin", defaultType = HippyControllerProps.BOOLEAN)
  public void setMomentumScrollBeginEventEnable(HippyScrollView view, boolean flag) {
    view.setMomentumScrollBeginEventEnable(flag);
  }

  @HippyControllerProps(name = "onMomentumScrollEnd", defaultType = HippyControllerProps.BOOLEAN)
  public void setMomentumScrollEndEventEnable(HippyScrollView view, boolean flag) {
    view.setMomentumScrollEndEventEnable(flag);
  }

  @HippyControllerProps(name = "flingEnabled", defaultType = HippyControllerProps.BOOLEAN, defaultBoolean = true)
  public void setFlingEnabled(HippyScrollView view, boolean flag) {
    view.setFlingEnabled(flag);
  }

  @HippyControllerProps(name = "contentOffset4Reuse")
  public void setContentOffset4Reuse(HippyScrollView view, HippyMap offsetMap) {
    view.setContentOffset4Reuse(offsetMap);
  }

  @HippyControllerProps(name = "pagingEnabled", defaultType = HippyControllerProps.BOOLEAN)
  public void setPagingEnabled(HippyScrollView view, boolean pagingEnabled) {
    view.setPagingEnabled(pagingEnabled);
  }

  @HippyControllerProps(name = "scrollEventThrottle", defaultType = HippyControllerProps.NUMBER, defaultNumber = 30.0D)
  public void setScrollEventThrottle(HippyScrollView view, int scrollEventThrottle) {
    view.setScrollEventThrottle(scrollEventThrottle);
  }

  @HippyControllerProps(name = "scrollMinOffset", defaultType = HippyControllerProps.NUMBER, defaultNumber = 5)
  public void setScrollMinOffset(HippyScrollView view, int scrollMinOffset) {
    view.setScrollMinOffset(scrollMinOffset);
  }

  @HippyControllerProps(name = "initialContentOffset", defaultType = HippyControllerProps.NUMBER, defaultNumber = 0)
  public void setInitialContentOffset(HippyScrollView view, int offset) {
    view.setInitialContentOffset((int)PixelUtil.dp2px(offset));
  }

  @Override
  public void onBatchComplete(View view) {
    super.onBatchComplete(view);

    if (view instanceof HippyScrollView) {
      ((HippyScrollView)view).scrollToInitContentOffset();
    }
  }

  @Override
  public void dispatchFunction(View view, String functionName, HippyArray args) {
    //noinspection unchecked
    super.dispatchFunction(view, functionName, args);
    if (view instanceof HippyScrollView) {

      if (TextUtils.equals(SCROLL_TO, functionName)) {
        int destX = Math.round(PixelUtil.dp2px(args.getDouble(0)));
        int destY = Math.round(PixelUtil.dp2px(args.getDouble(1)));
        boolean animated = args.getBoolean(2);

        if (animated) {
          ((HippyScrollView) view).callSmoothScrollTo(destX, destY, 0);//用默认的动画事件
        } else {
          view.scrollTo(destX, destY);
        }
      }
      if (TextUtils.equals(SCROLL_TO_WITHOPTIONS, functionName) && args != null
          && args.size() > 0) {
        HippyMap hippyMap = args.getMap(0); //取第一个元素
        int destX = Math.round(PixelUtil.dp2px(hippyMap.getInt("x")));
        int destY = Math.round(PixelUtil.dp2px(hippyMap.getInt("y")));
        int duration = hippyMap.getInt("duration");
        if (duration > 0) {
          ((HippyScrollView) view).callSmoothScrollTo(destX, destY, duration);//用默认的动画事件
        } else {
          view.scrollTo(destX, destY);
        }
      }
    }
  }

  private class ScrollObserveRunnable implements Runnable {

    private WeakReference<View> viewRef;

    public void setTargetView(View view) {
      viewRef = new WeakReference<>(view);
    }

    @Override
    public void run() {
      View view = viewRef != null ? viewRef.get() : null;
      if (view != null) {
        traverseExposure(view);
      }
    }
  }

}
