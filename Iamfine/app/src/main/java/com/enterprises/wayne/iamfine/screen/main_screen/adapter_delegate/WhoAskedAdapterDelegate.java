package com.enterprises.wayne.iamfine.screen.main_screen.adapter_delegate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;
import com.enterprises.wayne.iamfine.ui_util.GenericRecyclerViewDelegate;

import java.util.List;

import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarviewglide.GlideLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WhoAskedAdapterDelegate implements GenericRecyclerViewDelegate<WhoAskedAdapterDelegate.ViewHolder> {

	private final Listener mListener;
	private List<WhoAskedViewModel> mWhoAsked;

	public WhoAskedAdapterDelegate(Listener listener, List<WhoAskedViewModel> whoAsked) {
		mListener = listener;
		mWhoAsked = whoAsked;
	}

	@Override
	public int getViewType() {
		return WhoAskedViewModel.class.hashCode();
	}

	@Override
	public ViewHolder createViewHolder(ViewGroup parent) {
		View view = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.layout_who_asked_card, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder) {
		holder.linearLayoutUsers.removeAllViews();
		for (WhoAskedViewModel user: mWhoAsked){
			View view = LayoutInflater
					.from(holder.linearLayoutUsers.getContext())
					.inflate(R.layout.coloumn_user, null);
			holder.linearLayoutUsers.addView(view);
			AvatarView avatarView = (AvatarView) view.findViewById(R.id.image_view_pp);
			new GlideLoader().loadImage(avatarView, user.getImageUrl(), user.getDisplayName());
			TextView textView = (TextView) view.findViewById(R.id.text_view_title);
			textView.setText(user.getDisplayName());
		}

	}

	class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.view_group_users)
		LinearLayout linearLayoutUsers;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

		@OnClick(R.id.button_i_am_fine)
		public void onButtoniaMFineClicked() {
			mListener.onIamFineClicked();
		}
	}

	public interface Listener {
		void onIamFineClicked();
	}
}

