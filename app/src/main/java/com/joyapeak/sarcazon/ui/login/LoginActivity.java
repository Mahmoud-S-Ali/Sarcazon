package com.joyapeak.sarcazon.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.utils.EditTextUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.et_login_email)
    EditText mEmailET;

    @BindView(R.id.et_login_password)
    EditText mPasswordET;

    @BindView(R.id.layout_login_error)
    LinearLayout mLoginErrorLayout;

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUnBinder(ButterKnife.bind(this));

        ActivityComponent component = getActivityComponent();
        component.inject(this);

        mPresenter.onAttach(this);
        setupGeneralToolbar(mAppbar, getString(R.string.login));
    }


    @OnClick(R.id.btn_toolbar_done)
    public void onDoneClicked() {
        hideKeyboard();

        String email = mEmailET.getText().toString();
        String password = mPasswordET.getText().toString();

        mLoginErrorLayout.setVisibility(View.GONE);

        mPresenter.onLoginBtnClicked(email, password);
    }


    @Override
    public void onLoginResult(boolean isSuccessful) {
        if (isSuccessful) {
            finish();

        } else {
            mLoginErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEmailError(int emailErrorCode) {
        switch (emailErrorCode) {
            case EditTextUtils.EMAIL_STATUS_EMPTY:
                mEmailET.setError(getString(R.string.error_email_field_empty));
                break;

            case EditTextUtils.EMAIL_STATUS_INVALID:
                mEmailET.setError(getString(R.string.error_email_invalid));
                break;
        }
    }

    @Override
    public void showPasswordError(int passwordErrorCode) {
        switch (passwordErrorCode) {
            case EditTextUtils.PASSWORD_STATUS_LENGTH_SHORT:
                mPasswordET.setError(getString(R.string.error_password_short,
                        String.valueOf(EditTextUtils.PASSWORD_MIN_LENGTH)));
                break;

            case EditTextUtils.PASSWORD_STATUS_INVALID:
                mPasswordET.setError(getString(R.string.error_password_invalid));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
