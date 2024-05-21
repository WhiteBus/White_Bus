// Generated by view binder compiler. Do not edit!
package com.google.mediapipe.examples.facelandmarker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.mediapipe.examples.facelandmarker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUserDropinBusBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button busNumBtn;

  @NonNull
  public final ConstraintLayout userBtnBoard;

  private ActivityUserDropinBusBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button busNumBtn, @NonNull ConstraintLayout userBtnBoard) {
    this.rootView = rootView;
    this.busNumBtn = busNumBtn;
    this.userBtnBoard = userBtnBoard;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUserDropinBusBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUserDropinBusBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_user_dropin_bus, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUserDropinBusBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.busNum_btn;
      Button busNumBtn = ViewBindings.findChildViewById(rootView, id);
      if (busNumBtn == null) {
        break missingId;
      }

      id = R.id.user_btn_board;
      ConstraintLayout userBtnBoard = ViewBindings.findChildViewById(rootView, id);
      if (userBtnBoard == null) {
        break missingId;
      }

      return new ActivityUserDropinBusBinding((ConstraintLayout) rootView, busNumBtn, userBtnBoard);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}