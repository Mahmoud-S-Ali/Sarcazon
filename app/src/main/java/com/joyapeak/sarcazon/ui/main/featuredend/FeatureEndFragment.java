package com.joyapeak.sarcazon.ui.main.featuredend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by test on 9/12/2018.
 */

public class FeatureEndFragment extends BaseFragment {

    private Integer mFragmentPosition;
    private final static String EXTRA_FRAGMENT_POS = "EXTRA_FRAGMENT_POS";

    public static FeatureEndFragment newInstance(int fragmentPos) {
        FeatureEndFragment featureEndFragment = new FeatureEndFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_FRAGMENT_POS, fragmentPos);
        featureEndFragment.setArguments(bundle);
        return featureEndFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_featured_end, container, false);
        setUnBinder(ButterKnife.bind(this, root));

        mFragmentPosition = getArguments().getInt(EXTRA_FRAGMENT_POS);

        return root;
    }

    @Override
    protected void setUp(View view) {
    }

    public int getFragmentPosition() {
        return mFragmentPosition;
    }

    @OnClick(R.id.btn_featuredEnd_all)
    void onAllClicked() {
        ((MainActivity) getActivity()).switchToNewestComics();
    }
}
