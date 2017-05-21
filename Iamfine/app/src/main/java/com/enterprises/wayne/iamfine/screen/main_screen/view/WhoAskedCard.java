package com.enterprises.wayne.iamfine.screen.main_screen.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import java.util.List;

import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarviewglide.GlideLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WhoAskedCard extends LinearLayout {

	@BindView(R.id.view_group_users)
	ViewGroup viewGroupUsers;
	@Nullable
	private Listener mListener;

	public WhoAskedCard(Context context) {
		super(context);
		init();
	}

	public WhoAskedCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View view = LayoutInflater
				.from(getContext())
				.inflate(R.layout.layout_who_asked_card, this, true);
		ButterKnife.bind(this, view);
	}

	public void setListener(Listener listener){
		mListener = listener;
	}

	/**
	 * @param users who asked about you, their pps and names will appear in the horizontal scroll
	 */
	public void setUsers(List<WhoAskedViewModel> users){
		for (WhoAskedViewModel user: users){
			View view = LayoutInflater
					.from(getContext())
					.inflate(R.layout.coloumn_user, null);
			viewGroupUsers.addView(view);
			AvatarView avatarView = (AvatarView) view.findViewById(R.id.image_view_pp);
			new GlideLoader().loadImage(avatarView, user.getImageUrl(), user.getDisplayName());
			TextView textView = (TextView) view.findViewById(R.id.text_view_title);
			textView.setText(user.getDisplayName());
		}
	}

	@OnClick(R.id.button_i_am_fine)
	public void onButtonIAmFineClicked(){
		if (mListener != null)
			mListener.onIAmFineClicked();
	}

	public interface Listener{
		void onIAmFineClicked();
	}
}
