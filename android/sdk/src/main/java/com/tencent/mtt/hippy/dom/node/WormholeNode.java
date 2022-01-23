package com.tencent.mtt.hippy.dom.node;

import android.text.TextUtils;

import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.views.wormhole.HippyWormholeManager;

public class WormholeNode extends StyleNode {
  private final boolean mIsVirtual;
  private String mWormholeId;

  public WormholeNode(boolean isVirtual) {
    this.mIsVirtual = isVirtual;
  }

  public boolean isVirtual() {
    return mIsVirtual;
  }

  @Override
  public void setProps(HippyMap props) {
    super.setProps(props);
    if (TextUtils.isEmpty(mWormholeId)) {
      mWormholeId = HippyWormholeManager.getInstance().onWormholeNodeSetProps(props, getId());
    }
  }

  public String getWormholeId() {
    return mWormholeId;
  }
}
