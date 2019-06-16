package com.joyapeak.sarcazon.ui.tutorial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends BaseActivity implements TutorialMvpView {

    @BindView(R.id.tv_tutorial_msg)
    TextView mTutorialHeaderTV;

    @BindView(R.id.iv_tutorial_image)
    ImageView mTutorialImageIV;

    @BindView(R.id.btn_tutorial_next)
    Button mTutorialBtn;

    @Inject
    TutorialMvpPresenter<TutorialMvpView> mPresenter;

    private final int SWIPE_HOR_TUTORIAL_POS = 0;
    private final int SWIPE_VER_TUTORIAL_POS = 1;
    private final int DOUBLE_TAP_TUTORIAL_POS = 2;

    private Integer mCurrentTutorialPos;
    private final String EXTRA_CURRENT_TUTORIAL_POS = "EXTRA_CURRENT_TUTORIAL_POS";


    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TutorialActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_CURRENT_TUTORIAL_POS)) {
                mCurrentTutorialPos = savedInstanceState.getInt(EXTRA_CURRENT_TUTORIAL_POS);
            }
        }

        mCurrentTutorialPos = mCurrentTutorialPos == null? SWIPE_HOR_TUTORIAL_POS : mCurrentTutorialPos;
        updateTutorial();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentTutorialPos == SWIPE_HOR_TUTORIAL_POS) {
            super.onBackPressed();

        } else {
            mCurrentTutorialPos--;
            updateTutorial();
        }
    }

    private void updateTutorial() {
        String header = "";
        int imageResId = 0;
        String btnText = "";

        switch (mCurrentTutorialPos) {
            case SWIPE_HOR_TUTORIAL_POS:
                header = getString(R.string.tutorial_swipe_hor_header);
                imageResId = R.drawable.ic_swipe_hor;
                btnText = getString(R.string.next);
                mTutorialBtn.setTextColor(ContextCompat.getColor(this, R.color.white));

                mPresenter.logIntroTutStep1();
                break;

            case SWIPE_VER_TUTORIAL_POS:
                header = getString(R.string.tutorial_swipe_ver_header);
                imageResId = R.drawable.ic_swipe_ver;
                btnText = getString(R.string.next);
                mTutorialBtn.setTextColor(ContextCompat.getColor(this, R.color.white));

                mPresenter.logIntroTutStep2();
                break;

            case DOUBLE_TAP_TUTORIAL_POS:
                header = getString(R.string.tutorial_double_tap);
                imageResId = R.drawable.ic_double_tap;
                btnText = getString(R.string.lets_fun_it);
                mTutorialBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

                mPresenter.logIntroTutStep3();
                break;
        }

        mTutorialHeaderTV.setText(header);
        mTutorialImageIV.setImageResource(imageResId);
        mTutorialBtn.setText(btnText);
    }

    @OnClick(R.id.btn_tutorial_next)
    void onTutorialNextClick() {

        if (mCurrentTutorialPos == DOUBLE_TAP_TUTORIAL_POS) {
            finish();
            return;
        }

        mCurrentTutorialPos++;
        updateTutorial();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
