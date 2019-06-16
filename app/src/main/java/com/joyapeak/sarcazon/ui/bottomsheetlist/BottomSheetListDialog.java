package com.joyapeak.sarcazon.ui.bottomsheetlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.BasicListAdapter;
import com.joyapeak.sarcazon.ui.base.BaseBottomSheetDialogFragment;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 7/3/2018.
 */

public class BottomSheetListDialog extends BaseBottomSheetDialogFragment {

    public static final String TAG = "BottomSheetListFragment";

    @BindView(R.id.rv_bottomSheetList)
    RecyclerView mItemsRV;

    @Inject
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    String[] mItemsArr;

    private BottomSheetListHandler mHandler;

    public interface BottomSheetListHandler {
        void onBottomDialogItemSelected(int position);
    }


    public static BottomSheetListDialog newInstance(String[] items) {
        BottomSheetListDialog fragment = new BottomSheetListDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_ITEMS, items);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (BottomSheetListHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    protected String getMyDialogTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.bottom_sheet_list, container, false);

        mItemsArr = getArguments().getStringArray(EXTRA_ITEMS);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            ButterKnife.bind(this, root);
            setUp(root);
        }

        return root;
    }

    @Override
    protected void setUp(View view) {
        BasicListAdapter basicListAdapter = new BasicListAdapter(Arrays.asList(mItemsArr),
                new BasicListAdapter.Callback() {

                    @Override
                    public void onItemClicked(int position) {
                       mHandler.onBottomDialogItemSelected(position);
                       dismiss();
                    }
                });

        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mItemsRV.setLayoutManager(mLayoutManager);
        mItemsRV.setAdapter(basicListAdapter);
        mItemsRV.setHasFixedSize(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null && context instanceof BottomSheetListHandler) {
                mHandler = (BottomSheetListHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }
}
