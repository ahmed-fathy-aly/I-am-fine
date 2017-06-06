package com.enterprises.wayne.iamfine.ui_util;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Ahmed on 2/19/2017.
 */

public interface GenericRecyclerViewDelegate<VH extends RecyclerView.ViewHolder> {
    VH createViewHolder(ViewGroup parent);

    void onBindViewHolder(VH holder);
}
