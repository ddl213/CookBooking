package com.android.common.listener;

import android.view.View;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import com.android.common.base.adapter.BaseAdapter;

public interface OnItemChildClickListener {

    void onItemChildClick(@NotNull BaseAdapter<?> adapter, @NonNull View view, int position);

}
