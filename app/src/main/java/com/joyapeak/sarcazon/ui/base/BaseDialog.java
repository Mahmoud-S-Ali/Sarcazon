package com.joyapeak.sarcazon.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.joyapeak.sarcazon.di.component.ActivityComponent;

import butterknife.Unbinder;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 *
 * Normal dialog appears in the center of the screen
 */

public abstract class BaseDialog extends DialogFragment implements DialogMvpView {

    protected String mTag;
    private BaseActivity mActivity;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getMyDialogTag();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getDialog().getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mActivity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void dismissDialog() {
        if (mActivity != null) {
            mActivity.onFragmentDetached(mTag);
        }

        dismiss();
    }

    public void show(FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(mTag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        show(transaction, mTag);
    }

    @Override
    public void showLoadingDialog() {
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
    public void onError(int resId, ApiErrorHandler handler) {
        if (mActivity != null) {
            mActivity.onError(resId, handler);
        }
    }

    @Override
    public void onError(String message, ApiErrorHandler handler) {
        if (mActivity != null) {
            mActivity.onError(message, handler);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    @Override
    public void hideKeyboard() {
        /*if (mActivity != null) {
            mActivity.hideKeyboard();
        }*/

        View view = getDialog().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }
}
