// Generated by view binder compiler. Do not edit!
package com.google.mediapipe.examples.facelandmarker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ActivityUserAddressBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout addressBoard;

  @NonNull
  public final ConstraintLayout dstInfo;

  @NonNull
  public final ConstraintLayout locationBtn;

  @NonNull
  public final TextView userAddress;

  @NonNull
  public final TextView userAddressDstName;

  @NonNull
  public final TextView userDistance;

  @NonNull
  public final TextView userTime;

  private ActivityUserAddressBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout addressBoard, @NonNull ConstraintLayout dstInfo,
      @NonNull ConstraintLayout locationBtn, @NonNull TextView userAddress,
      @NonNull TextView userAddressDstName, @NonNull TextView userDistance,
      @NonNull TextView userTime) {
    this.rootView = rootView;
    this.addressBoard = addressBoard;
    this.dstInfo = dstInfo;
    this.locationBtn = locationBtn;
    this.userAddress = userAddress;
    this.userAddressDstName = userAddressDstName;
    this.userDistance = userDistance;
    this.userTime = userTime;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUserAddressBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUserAddressBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_user_address, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUserAddressBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.address_board;
      ConstraintLayout addressBoard = ViewBindings.findChildViewById(rootView, id);
      if (addressBoard == null) {
        break missingId;
      }

      id = R.id.dst_info;
      ConstraintLayout dstInfo = ViewBindings.findChildViewById(rootView, id);
      if (dstInfo == null) {
        break missingId;
      }

      id = R.id.location_btn;
      ConstraintLayout locationBtn = ViewBindings.findChildViewById(rootView, id);
      if (locationBtn == null) {
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

      id = R.id.user_distance;
      TextView userDistance = ViewBindings.findChildViewById(rootView, id);
      if (userDistance == null) {
        break missingId;
      }

      id = R.id.user_time;
      TextView userTime = ViewBindings.findChildViewById(rootView, id);
      if (userTime == null) {
        break missingId;
      }

      return new ActivityUserAddressBinding((ConstraintLayout) rootView, addressBoard, dstInfo,
          locationBtn, userAddress, userAddressDstName, userDistance, userTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
