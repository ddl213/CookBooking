package com.example.common.listener;

import android.view.View;
import androidx.annotation.NonNull;
import com.example.common.base.BaseAdapter;

import org.jetbrains.annotations.NotNull;


public interface OnItemLongClickListener {

    Boolean onItemLongClick(@NotNull BaseAdapter<?, ?> adapter, @NonNull View view, int position);

}
