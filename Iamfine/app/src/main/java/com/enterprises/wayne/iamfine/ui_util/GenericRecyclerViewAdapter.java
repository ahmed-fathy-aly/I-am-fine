package com.enterprises.wayne.iamfine.ui_util;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.enterprises.wayne.iamfine.screen.main_screen.adapter_delegate.UserViewAdapterDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * to use it, wrap view holders in {@link GenericRecyclerViewDelegate}
 * Created by Ahmed on 2/19/2017.
 */
public class GenericRecyclerViewAdapter extends RecyclerView.Adapter {
    List<GenericRecyclerViewDelegate> mDelegates;

    public GenericRecyclerViewAdapter() {
        mDelegates = new ArrayList<>();
    }

    /**
     * add an enty to any position within the recycler view
     * @param position  must be less than {@code getItemCount()}
     */
    public void     add(int position, GenericRecyclerViewDelegate delegate) {
        mDelegates.add(position, delegate);
        notifyItemInserted(position);
    }


    public void addAll(int position, List<UserViewAdapterDelegate> delegates) {
        mDelegates.addAll(position, delegates);
        notifyItemRangeInserted(position, delegates.size());
    }

    /**
     * removes all delegates of that class type
     */
    public void removeAll(Class deletedClass) {
        List<GenericRecyclerViewDelegate> filteredDelegates = new ArrayList<>();
        for (GenericRecyclerViewDelegate delegate : mDelegates)
            if (!delegate.getClass().equals(deletedClass))
                filteredDelegates.add(delegate);
        mDelegates.clear();
        mDelegates.addAll(filteredDelegates);
        notifyDataSetChanged();
    }

    /**
     * remove the first delegate of that class
     */
    public void removeFirst(Class deletedClass){
        int pos = -1;
        for (int i = 0; i < mDelegates.size(); i++)
            if (mDelegates.get(i).getClass().equals(deletedClass)) {
                pos = i;
                break;
            }
        if (pos != -1) {
            mDelegates.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        for (GenericRecyclerViewDelegate delegate : mDelegates)
            if (delegate.getViewType() == viewType)
                return delegate.createViewHolder(parent);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mDelegates.get(position).onBindViewHolder(holder);

    }

    @Override
    public int getItemCount() {
        return mDelegates.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegates.get(position).getViewType();
    }

}
