package com.joyapeak.sarcazon.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.joyapeak.sarcazon.di.component.ActivityComponent;

import butterknife.Unbinder;

/**
 * Created by Mahmoud Ali on 7/3/2018.
 *
 * A dialog that appears from bottom (Like ios dialogs)
 */

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements MvpView {

    private BaseActivity mActivity;
    private ProgressDialog mProgressDialog;
    private Unbinder mUnBinder;

    protected static String EXTRA_ITEMS = "EXTRA_ITEMS";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void showLoadingDialog() {
        hideLoadingDialog();
        if (mActivity != null) {
            mActivity.showLoadingDialog();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (mActivity != null) {
            mActivity.hideLoadingDialog();
        }
    }

    @Override
    public void showView(View view) {
        if (mActivity != null) {
            mActivity.showView(view);
        }
    }

    @Override
    public void hideView(View view) {
        if (mActivity != null) {
            mActivity.hideView(view);
        }
    }

    @Override
    public Animation addAnimationToViews(View[] views, int animationResId, Integer duration, Integer startOffset) {
        if (mActivity != null) {
            return mActivity.addAnimationToViews(views, animationResId, duration, startOffset);
        }

        return null;
    }

    @Override
    public Animation addAnimationToView(View view, int animationResId, Integer duration, Integer startOffset) {
        if (mActivity != null) {
            return mActivity.addAnimationToView(view, animationResId, duration, startOffset);
        }

        return null;
    }

    @Override
    public void onError(String message, ApiErrorHandler handler) {
        if (mActivity != null) {
            mActivity.onError(message, handler);
        }
    }

    @Override
    public void onError(@StringRes int resId, ApiErrorHandler handler) {
        if (mActivity != null) {
            mActivity.onError(resId, handler);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void setViewColor(View view, int color) {
        if (mActivity != null) {
            mActivity.setViewColor(view, color);
        }
    }

    @Override
    public void openActivityOnLogout() {
        if (mActivity != null) {
            mActivity.openActivityOnLogout();
        }
    }

    @Override
    public void showRegistrationDialog() {
        if (mActivity != null) {
            mActivity.showRegistrationDialog();
        }
    }

    @Override
    public void openUrlLink(String url) {
        if (mActivity != null) {
            mActivity.openUrlLink(url);
        }
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    protected abstract String getMyDialogTag();

    protected abstract void setUp(View view);

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
