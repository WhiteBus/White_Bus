// Generated by view binder compiler. Do not edit!
package com.google.mediapipe.examples.facelandmarker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.mediapipe.examples.facelandmarker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityViDrawerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout drawerLayout;

  @NonNull
  public final RecyclerView viDrawerRv;

  @NonNull
  public final TextView viDrawerTv;

  private ActivityViDrawerBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout drawerLayout, @NonNull RecyclerView viDrawerRv,
      @NonNull TextView viDrawerTv) {
    this.rootView = rootView;
    this.drawerLayout = drawerLayout;
    this.viDrawerRv = viDrawerRv;
    this.viDrawerTv = viDrawerTv;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityViDrawerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityViDrawerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_vi_drawer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityViDrawerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout drawerLayout = (ConstraintLayout) rootView;

      id = R.id.vi_drawer_rv;
      RecyclerView viDrawerRv = ViewBindings.findChildViewById(rootView, id);
      if (viDrawerRv == null) {
        break missingId;
      }

      id = R.id.vi_drawer_tv;
      TextView viDrawerTv = ViewBindings.findChildViewById(rootView, id);
      if (viDrawerTv == null) {
        break missingId;
      }

      return new ActivityViDrawerBinding((ConstraintLayout) rootView, drawerLayout, viDrawerRv,
          viDrawerTv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}