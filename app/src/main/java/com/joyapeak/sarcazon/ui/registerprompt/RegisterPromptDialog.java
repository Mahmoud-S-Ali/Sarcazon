package com.joyapeak.sarcazon.ui.registerprompt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseDialog;
import com.joyapeak.sarcazon.ui.login.LoginActivity;
import com.joyapeak.sarcazon.ui.profilesettings.ProfileSettingsActivity;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 5/31/2018.
 */

public class RegisterPromptDialog extends BaseDialog implements RegisterPromptDialogMvpView {

    public static final String TAG = "RegisterPromptDialog";

    @Inject
    RegisterPromptDialogMvpPresenter<RegisterPromptDialogMvpView> mPresenter;

    // Needed for facebook login
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;


    public static RegisterPromptDialog newInstance() {
        RegisterPromptDialog dialog = new RegisterPromptDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_register_prompt, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        mAuth = FirebaseAuth.getInstance();
        setupFacebookLoginBtn();

        mPresenter.logRegRequest();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {
    }

    @OnClick(R.id.btn_registerPrompt_login)
    public void onLoginClicked() {
        startActivity(LoginActivity.getStartIntent(getActivity()));
        getDialog().dismiss();
    }

    @OnClick(R.id.btn_registerPrompt_facebookLogin)
    public void onFacebookLoginClicked() {
        showLoadingDialog();
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                "email", "public_profile", "user_friends"));

        mPresenter.logFacebookRegRequest();
    }

    @OnClick(R.id.btn_registerPrompt_register)
    public void onRegisterClicked() {
        mPresenter.logSignUpRequest();
        startActivity(ProfileSettingsActivity.getStartIntent(getActivity(), null));
        getDialog().dismiss();
    }


    private void setupFacebookLoginBtn() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Timber.d("facebook:onCancel");
                // ...
                hideLoadingDialog();
            }

            @Override
            public void onError(FacebookException error) {
                Timber.d("facebook:onError " + error);
                // ...
                hideLoadingDialog();
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Timber.d("handleFacebookAccessToken:" + token.getToken());
        Timber.d("Facebook User ID:" + token.getUserId());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getBaseActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mPresenter.onFacebookSignInWithCredentialsCompleted(task, mAuth);
                    }
                });
    }

    @Override
    public void onFacebookLoginFinished(boolean isSuccessful) {
        showMessage("Facebook login " + isSuccessful);
        hideLoadingDialog();
        getDialog().dismiss();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
