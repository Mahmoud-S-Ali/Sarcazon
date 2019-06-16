package com.joyapeak.sarcazon.ui.credits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditsActivity extends BaseActivity {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CreditsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        setUnBinder(ButterKnife.bind(this));

        setupGeneralToolbar(mAppbar, getString(R.string.credits));
    }
}
