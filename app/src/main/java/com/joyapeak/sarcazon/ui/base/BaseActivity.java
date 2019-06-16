package com.joyapeak.sarcazon.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.joyapeak.sarcazon.MyApp;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.di.component.DaggerActivityComponent;
import com.joyapeak.sarcazon.di.module.ActivityModule;
import com.joyapeak.sarcazon.ui.registerprompt.RegisterPromptDialog;
import com.joyapeak.sarcazon.ui.splash.SplashActivity;
import com.joyapeak.sarcazon.utils.NetworkUtils;

import butterknife.Unbinder;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback {

    private ProgressDialog mProgressDialog;

    private ActivityComponent mActivityComponent;

    private Unbinder mUnBinder;

    // Used to handle navigation up for activities opened from different sources
    public final static String EXTRA_PARENT_ACTIVITY = "EXTRA_PARENT_ACTIVITY";
    protected String mParentActivityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MyApp) getApplication()).getComponent())
                .build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void showSnackBar(String message, final ApiErrorHandler handler) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));

        TextView msgTV = sbView.findViewById(R.id.snackbar_text);
        TextView actionTV = sbView.findViewById(R.id.snackbar_action);

        msgTV.setTextAppearance(this, R.style.Text_Body3_Bold_Light);
        msgTV.setTextColor(ContextCompat.getColor(this, R.color.white));

        actionTV.setTextAppearance(this, R.style.Text_Body3_Bold_Light);
        actionTV.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.onRetry();
            }
        });
        snackbar.show();
    }

    @Override
    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this) {
                @Override
                public boolean onKeyUp(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mProgressDialog.dismiss();
                        onBackPressed();
                    }

                    return true;
                }
            };

            mProgressDialog.show();

            if (mProgressDialog.getWindow() != null) {
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            mProgressDialog.setContentView(R.layout.progress_dialog);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);

        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showView(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideView(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onError(int resId, ApiErrorHandler handler) {
        onError(getString(resId), handler);
    }

    @Override
    public void onError(String message, ApiErrorHandler handler) {
        if (message != null) {
            showSnackBar(message, handler);

        } else {
            showSnackBar(getString(R.string.some_error), handler);
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(int resId) {
        showMessage(getString(resId));
    }


    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            } catch (NullPointerException ex) {
                Timber.e(ex.getMessage().toString());
            }
        }
    }

    @Override
    public void setViewColor(View view, int color) {
        try {
            if (view instanceof ImageButton) {
                ((ImageButton) view).setColorFilter(ContextCompat.getColor(this, color),
                        PorterDuff.Mode.SRC_IN);

            } else if (view instanceof ImageView) {
                ((ImageView) view).setColorFilter(ContextCompat.getColor(this, color),
                        PorterDuff.Mode.SRC_IN);

            } else if (view instanceof TextView) {
                ((TextView) view).setTextColor(ContextCompat.getColor(this, color));

            } else {
                view.setBackgroundColor(ContextCompat.getColor(this, color));
            }

        } catch (Exception ex) {
            Timber.e("Can't set background color for this type of view");
        }
    }

    @Override
    public void openActivityOnLogout() {
        ActivityCompat.finishAffinity(this);
        Intent intent = SplashActivity.getStartIntent(getApplicationContext());
        startActivity(intent);
    }

    @Override
    public void showRegistrationDialog() {
        RegisterPromptDialog dialog = RegisterPromptDialog.newInstance();
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void openUrlLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public Animation addAnimationToViews(View[] views, int animationResId, Integer duration, Integer startOffset) {

        if (views != null) {
            Animation animation = AnimationUtils.loadAnimation(this, animationResId);

            if (duration != null) {
                if (duration > 0)
                    animation.setDuration(duration);

                else if (duration < 0) {
                    animation.setRepeatCount(Animation.INFINITE);
                }
            }

            if (startOffset != null) {
                animation.setStartOffset(startOffset);
            }

            for (View view : views) {
                view.startAnimation(animation);
            }

            return animation;
        }

        return null;
    }

    @Override
    public Animation addAnimationToView(View view, int animationResId, Integer duration, Integer startOffset) {

        return addAnimationToViews(new View[] {view}, animationResId,
                duration, startOffset);
    }

    public void setupGeneralToolbar(AppBarLayout appbar, String title) {
        Toolbar toolbar = appbar.findViewById(R.id.toolbar);
        TextView titleTV = appbar.findViewById(R.id.tv_toolbar_general_title);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (titleTV != null) {
                titleTV.setText(title);
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left);
        }
    }
}
