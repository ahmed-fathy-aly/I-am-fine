package com.enterprises.wayne.iamfine.common.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Ahmed on 2/19/2017.
 */

public interface GenericRecyclerViewDelegate<VH extends RecyclerView.ViewHolder, O> {
	VH createViewHolder(ViewGroup parent);

	void onBindViewHolder(VH holder, O data);
}
