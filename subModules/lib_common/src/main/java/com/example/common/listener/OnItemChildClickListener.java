package com.example.common.listener;

import android.view.View;
import androidx.annotation.NonNull;
import com.example.common.base.adapter.BaseAdapter;

import org.jetbrains.annotations.NotNull;

public interface OnItemChildClickListener {

    void onItemChildClick(@NotNull BaseAdapter<?> adapter, @NonNull View view, int position);

}
