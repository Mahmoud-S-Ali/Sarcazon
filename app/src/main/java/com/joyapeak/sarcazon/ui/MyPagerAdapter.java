package com.joyapeak.sarcazon.ui;

import android.os.Parcelable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.joyapeak.sarcazon.ui.comic.SingleComicFragment;
import com.joyapeak.sarcazon.ui.main.featuredend.FeatureEndFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 4/17/2018.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList;
    private final List<String> mFragmentTitleList;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentTitleList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void clearAll(ViewGroup container, int currentPos, int pagerOffset) {
        int from = currentPos - pagerOffset;
        int to = currentPos + pagerOffset;

        from = from < 0 ? 0 : from;
        to = to > getCount() - 1 ? getCount() - 1 : to;

        for (int i = from; i <= to; i++) {
            destroyItem(container, i, getItem(i));
        }

        mFragmentList.clear();
        mFragmentTitleList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(ViewGroup container, int itemPosition) {
        destroyItem(container, itemPosition, getItem(itemPosition));
        mFragmentList.remove(itemPosition);
        mFragmentTitleList.remove(itemPosition);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        Timber.d("Item destroyed at = " + String.valueOf(position));
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int fragmentPos = POSITION_NONE;

        if (object instanceof SingleComicFragment) {
            SingleComicFragment comicFragment = (SingleComicFragment) object;
            fragmentPos = mFragmentTitleList.indexOf(String.valueOf(comicFragment.getFragmentPosition()));

        } else if (object instanceof FeatureEndFragment) {
            FeatureEndFragment featureEndFragment = (FeatureEndFragment) object;
            fragmentPos = mFragmentList.indexOf(String.valueOf(featureEndFragment.getFragmentPosition()));
        }

        Timber.d("PagerAdapter Fragment Position = " + fragmentPos);
        return fragmentPos < 0 ? POSITION_NONE : fragmentPos;
    }

    @Override
    public Parcelable saveState() {
        /*Bundle bundle = (Bundle) super.saveState();
        if (bundle != null) {
            bundle.putParcelableArray("states", null); // Never maintain any states from the base class, just null it out
        }
        return bundle;*/

        return null;
    }
}