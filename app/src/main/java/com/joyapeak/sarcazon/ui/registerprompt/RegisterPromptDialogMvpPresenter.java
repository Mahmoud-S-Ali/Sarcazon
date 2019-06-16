package com.joyapeak.sarcazon.ui.registerprompt;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/31/2018.
 */

public interface RegisterPromptDialogMvpPresenter <V extends RegisterPromptDialogMvpView>
        extends MvpPresenter<V> {

    void onFacebookSignInWithCredentialsCompleted(@NonNull Task<AuthResult> task, FirebaseAuth auth);

    void logRegRequest();

    void logFacebookRegRequest();

    void logSignUpRequest();
}
