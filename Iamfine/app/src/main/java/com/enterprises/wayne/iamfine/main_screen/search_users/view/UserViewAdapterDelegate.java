package com.enterprises.wayne.iamfine.main_screen.search_users.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.common.view.GenericRecyclerViewDelegate;

import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarviewglide.GlideLoader;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 2/20/2017.
 */

public class UserViewAdapterDelegate implements GenericRecyclerViewDelegate<UserViewAdapterDelegate.ViewHolder, UserCardData> {

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
	public void onBindViewHolder(ViewHolder holder, UserCardData data) {
		holder.textViewTitle.setText(data.getDisplayName());
		holder.textViewTime.setText(data.getTimeStr());
		new GlideLoader().loadImage(holder.imageViewPp, data.getImageUrl(), data.getDisplayName());
		holder.buttonAskIfFine.setVisibility(data.getAskAboutButtonState() == UserCardData.AskAboutButtonState.ENABLED ? View.VISIBLE : View.GONE);
		holder.loading.setVisibility(data.getAskAboutButtonState() == UserCardData.AskAboutButtonState.LOADING ? View.VISIBLE : View.GONE);
		holder.viewAsked.setVisibility(data.getAskAboutButtonState() == UserCardData.AskAboutButtonState.ASKED ? View.VISIBLE : View.GONE);

		holder.buttonAskIfFine.setOnClickListener(v -> {
			if (mListener != null) {
				mListener.onAskIfFine(data.getId());
			}
		});
	}

	public interface Listener {
		void onAskIfFine(String userId);
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
		@BindView(R.id.image_view_asked)
		View viewAsked;
		@BindView(R.id.progress_bar)
		ProgressBar loading;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
