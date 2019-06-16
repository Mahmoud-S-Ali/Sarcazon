package com.joyapeak.sarcazon.ui.newupdates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.base.BaseDialog;
import com.joyapeak.sarcazon.ui.custom.CustomViewPager;
import com.joyapeak.sarcazon.ui.newupdates.updatescontent.UpdatesContentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by test on 10/2/2018.
 */

public class NewUpdatesDialog extends BaseDialog {

    private final String TAG = "NewUpdatesDialog";

    @BindView(R.id.viewpager_updates)
    CustomViewPager mViewPager;

    @BindView(R.id.btn_updatesDialog_next)
    Button mNextBtn;

    private MyPagerAdapter mViewPagerAdapter;

    String[] mUpdatesTitles;
    String[] mUpdatesDesc;
    Integer[] mUpdatesDrawableIds;

    private NewUpdatesDialogHandler mHandler;

    public interface NewUpdatesDialogHandler {
        void onUpdatesDialogDoneClicked();
    }


    public static NewUpdatesDialog newInstance() {
        NewUpdatesDialog dialog = new NewUpdatesDialog();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (NewUpdatesDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_updates, container, false);
        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    @Override
    protected void setUp(View view) {
        setupUpdatesInfo();
        setupViewPagerAdapter();
        setupViewPager();

        updateNextBtnView();
    }

    private void setupUpdatesInfo() {

        mUpdatesTitles = new String[] {
                "Facebook Videos"/*,
                "Gif From Gallery"*/
        };

        mUpdatesDesc = new String[] {
                "Te2dar tenazel videohat men el facebook"/*,
                "Te2dar tenazel GIF men el gallery 3andak"*/
        };

        mUpdatesDrawableIds = new Integer[] {
                R.drawable.im_facebook_videos/*,
                R.drawable.im_gif_gallery*/
        };
    }
    private void setupViewPagerAdapter() {
        mViewPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
    }
    private void setupViewPager() {
        List<UpdatesContentFragment> updatesContentFragments = new ArrayList<>();

        for (int i = 0; i < mUpdatesTitles.length; i++) {
            updatesContentFragments.add(UpdatesContentFragment.newInstance(
                    mUpdatesTitles[i],
                    mUpdatesDesc[i],
                    mUpdatesDrawableIds[i]));
        }

        for (UpdatesContentFragment fragment : updatesContentFragments) {
            mViewPagerAdapter.addFragment(fragment, String.valueOf(mViewPagerAdapter.getCount()));
        }

        mViewPager.setAdapter(mViewPagerAdapter);

        // mViewPager.setOffscreenPageLimit(VIEW_PAGER_OFFSET_LIMIT);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateNextBtnView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateNextBtnView() {
        if (mViewPager.getCurrentItem() == mUpdatesTitles.length - 1) {
            mNextBtn.setText(getString(R.string.tamam));
            mNextBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_rect_rounded_accent2));

        } else {
            mNextBtn.setText(getString(R.string.next));
            mNextBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_rect_rounded_parimary_light));
        }
    }

    @Override
    public String getMyDialogTag() {
        return TAG;
    }



    @OnClick(R.id.btn_updatesDialog_next)
    public void onNextClicked() {
        if (mViewPager.getCurrentItem() == mUpdatesTitles.length - 1) {
            if (mHandler != null) {
                mHandler.onUpdatesDialogDoneClicked();
            }

            getDialog().dismiss();

        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null && context instanceof NewUpdatesDialogHandler) {
                mHandler = (NewUpdatesDialogHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }
}
