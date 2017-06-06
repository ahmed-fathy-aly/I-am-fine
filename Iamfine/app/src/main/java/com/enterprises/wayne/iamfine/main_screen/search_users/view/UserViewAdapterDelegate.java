package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.ui_util.GenericRecyclerViewDelegate;

import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 2/20/2017.
 */

public class UserViewAdapterDelegate implements GenericRecyclerViewDelegate<UserViewAdapterDelegate.ViewHolder> {

    private final Listener mListener;

    public UserViewAdapterDelegate(Listener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder) {
        //holder.textViewTitle.setText(mViewModel.getDisplayName());
        //holder.textViewTime.setText(mViewModel.getLastFineTimeStr());
        //new GlideLoader().loadImage(holder.imageViewPp, mViewModel.getImageUrl(), mViewModel.getDisplayName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view_pp)
        AvatarView imageViewPp;
        @BindView(R.id.text_view_title)
        TextView textViewTitle;
        @BindView(R.id.text_view_time)
        TextView textViewTime;
        @BindView(R.id.button_ask_if_fine)
        View buttonAskIfFine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(
                    (v) -> {
                        //if (mListener != null)
          //                  mListener.onUserClicked(mViewModel.getId());
                    }
            );
            buttonAskIfFine.setOnClickListener(
                    (v) -> {
                       // if (mListener != null)
            //                mListener.onAskIfFine(mViewModel.getId());
                    }
            );

        }
    }

    public interface Listener {
        void onUserClicked(String userId);

        void onAskIfFine(String userId);
    }
}
