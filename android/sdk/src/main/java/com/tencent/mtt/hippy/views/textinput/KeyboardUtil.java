package com.tencent.mtt.hippy.views.textinput;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

/**
 * @author hengyangji
 *   on 2021/8/10
 */
public class KeyboardUtil {

  public static void showSoftInput(View v) {
    v.requestFocus();
    if (v.isFocused()) {
      showInputMethodWithFocus(v);
    } else {
      OnWindowFocusChangeListener listener = new OnWindowFocusChangeListener() {
        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
          if (hasFocus) {
            showInputMethodWithFocus(v);
            v.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
          }
        }
      };
      v.getViewTreeObserver().addOnWindowFocusChangeListener(listener);
    }
  }

  private static void showInputMethodWithFocus(View v) {
    if (v == null || v.getContext() == null || !v.isFocused()) {
      return;
    }

    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm == null) {
      return;
    }

    v.post(() -> imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT));

  }
}
