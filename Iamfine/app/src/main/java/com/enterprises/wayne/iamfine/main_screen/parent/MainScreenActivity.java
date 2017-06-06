package com.enterprises.wayne.iamfine.main_screen.parent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.base.BaseActivity;
import com.enterprises.wayne.iamfine.main_screen.search_users.view.SearchUsersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainScreenActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        setupViewPager();
    }

    private void setupViewPager() {
        String[] titles = {getString(R.string.search_users), getString(R.string.search_users), getString(R.string.search_users)};
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final String[] titles;

        public PagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int i) {
            return SearchUsersFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
