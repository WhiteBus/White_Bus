// Generated by view binder compiler. Do not edit!
package com.google.mediapipe.examples.facelandmarker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.mediapipe.examples.facelandmarker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentExitBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button endButton;

  @NonNull
  public final TextView textview;

  private FragmentExitBinding(@NonNull RelativeLayout rootView, @NonNull Button endButton,
      @NonNull TextView textview) {
    this.rootView = rootView;
    this.endButton = endButton;
    this.textview = textview;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentExitBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentExitBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_exit, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentExitBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.endButton;
      Button endButton = ViewBindings.findChildViewById(rootView, id);
      if (endButton == null) {
        break missingId;
      }

      id = R.id.textview;
      TextView textview = ViewBindings.findChildViewById(rootView, id);
      if (textview == null) {
        break missingId;
      }

      return new FragmentExitBinding((RelativeLayout) rootView, endButton, textview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}