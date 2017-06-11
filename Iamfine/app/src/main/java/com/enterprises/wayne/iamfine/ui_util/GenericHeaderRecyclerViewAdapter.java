package com.enterprises.wayne.iamfine.ui_util;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * to use it, wrap view holders in {@link GenericRecyclerViewDelegate}
 */
public class GenericHeaderRecyclerViewAdapter extends RecyclerView.Adapter {
	private List<Object> mData;
	private Object mHeader;

	private final List<Class> mDataTypes;

	private final Map<Class, GenericRecyclerViewDelegate> mDelegates;

	public GenericHeaderRecyclerViewAdapter(Map<Class, GenericRecyclerViewDelegate> delegates) {
		mDelegates = delegates;
		mDataTypes = new ArrayList<>(mDelegates.keySet());
		mData = new ArrayList<>();
	}

	public <T extends Object> void changeData(List<T> newData) {
		mData.clear();
		mData.addAll(newData);
		notifyDataSetChanged();
	}

	public void setHeader(Object header) {
		mHeader = header;
		notifyDataSetChanged();
	}

	public void removeHeader() {
		mHeader = null;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(
			ViewGroup parent, int viewType) {
		return mDelegates.get(mDataTypes.get(viewType)).createViewHolder(parent);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Object item = mData.get(position);
		mDelegates.get(item.getClass()).onBindViewHolder(holder, item);
	}

	@Override
	public int getItemCount() {
		return mData.size() + (mHeader == null ? 0 : 1);
	}

	@Override
	public int getItemViewType(int position) {
		if (mHeader != null && position == 0)
			return mDataTypes.indexOf(mHeader.getClass());
		else
			return mDataTypes.indexOf(mData.get(position).getClass());
	}

}
