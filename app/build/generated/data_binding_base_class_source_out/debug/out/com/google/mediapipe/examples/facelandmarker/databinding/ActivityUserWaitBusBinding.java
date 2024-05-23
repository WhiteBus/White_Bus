// Generated by view binder compiler. Do not edit!
package com.google.mediapipe.examples.facelandmarker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.mediapipe.examples.facelandmarker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUserWaitBusBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout addressBoard;

  @NonNull
  public final Button busNumBtn;

  @NonNull
  public final ConstraintLayout dstInfo;

  @NonNull
  public final TextView userAddress;

  @NonNull
  public final TextView userAddressDstName;

  @NonNull
  public final ConstraintLayout userBtnBoard;

  @NonNull
  public final TextView userBusTime;

  private ActivityUserWaitBusBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout addressBoard, @NonNull Button busNumBtn,
      @NonNull ConstraintLayout dstInfo, @NonNull TextView userAddress,
      @NonNull TextView userAddressDstName, @NonNull ConstraintLayout userBtnBoard,
      @NonNull TextView userBusTime) {
    this.rootView = rootView;
    this.addressBoard = addressBoard;
    this.busNumBtn = busNumBtn;
    this.dstInfo = dstInfo;
    this.userAddress = userAddress;
    this.userAddressDstName = userAddressDstName;
    this.userBtnBoard = userBtnBoard;
    this.userBusTime = userBusTime;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUserWaitBusBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUserWaitBusBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_user_wait_bus, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUserWaitBusBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.address_board;
      ConstraintLayout addressBoard = ViewBindings.findChildViewById(rootView, id);
      if (addressBoard == null) {
        break missingId;
      }

      id = R.id.busNum_btn;
      Button busNumBtn = ViewBindings.findChildViewById(rootView, id);
      if (busNumBtn == null) {
        break missingId;
      }

      id = R.id.dst_info;
      ConstraintLayout dstInfo = ViewBindings.findChildViewById(rootView, id);
      if (dstInfo == null) {
        break missingId;
      }

      id = R.id.user_address;
      TextView userAddress = ViewBindings.findChildViewById(rootView, id);
      if (userAddress == null) {
        break missingId;
      }

      id = R.id.user_address_dst_name;
      TextView userAddressDstName = ViewBindings.findChildViewById(rootView, id);
      if (userAddressDstName == null) {
        break missingId;
      }

      id = R.id.user_btn_board;
      ConstraintLayout userBtnBoard = ViewBindings.findChildViewById(rootView, id);
      if (userBtnBoard == null) {
        break missingId;
      }

      id = R.id.user_bus_time;
      TextView userBusTime = ViewBindings.findChildViewById(rootView, id);
      if (userBusTime == null) {
        break missingId;
      }

      return new ActivityUserWaitBusBinding((ConstraintLayout) rootView, addressBoard, busNumBtn,
          dstInfo, userAddress, userAddressDstName, userBtnBoard, userBusTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}