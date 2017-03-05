package com.enterprises.wayne.iamfine.base;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.enterprises.wayne.iamfine.R;

/**
 * Created by Ahmed on 2/9/2017.
 */

public abstract class BaseFragmentView extends BaseFragment implements BaseContract.BaseView{

    ViewGroup viewContent;
    ProgressBar progressBar;

    protected void setViewContent(ViewGroup viewContent){
        this.viewContent = viewContent;
    }

    protected void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    @Override
    public void showLoading() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNetworkError() {
        if (viewContent != null)
            Snackbar.make(viewContent, R.string.network_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showUnknownError() {
        if (viewContent != null)
            Snackbar.make(viewContent, R.string.something_went_wrong, Snackbar.LENGTH_LONG).show();
    }

    protected void showMessage(int message){
        if (viewContent != null)
            Snackbar.make(viewContent,message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
