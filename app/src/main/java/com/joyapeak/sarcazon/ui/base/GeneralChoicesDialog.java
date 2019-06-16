package com.joyapeak.sarcazon.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.BasicListAdapter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeneralChoicesDialog extends BaseBottomSheetDialogFragment {

    private static final String TAG = "GeneralChoicesDialog";

    @BindView(R.id.rv_dialogBasicList)
    protected RecyclerView mChoiceRV;

    private String[] mItems;

    private GeneralChoicesDialogHandler mHandler;

    public interface GeneralChoicesDialogHandler {
        void onItemSelected(int position);
    }

    public static GeneralChoicesDialog newInstance(String[] items) {
        GeneralChoicesDialog fragment = new GeneralChoicesDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_ITEMS, items);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_basic_list, container, false);
        ButterKnife.bind(this, view);

        mItems = getArguments().getStringArray(EXTRA_ITEMS);

        return view;
    }

    public void setHandler(GeneralChoicesDialogHandler handler) {
        mHandler = handler;
    }

    @Override
    public String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {


        BasicListAdapter adapter = new BasicListAdapter(Arrays.asList(mItems), new BasicListAdapter.Callback() {

            @Override
            public void onItemClicked(int position) {
                if (mHandler != null) {
                    mHandler.onItemSelected(position);
                }
                dismiss();
            }
        });
        adapter.setHasStableIds(true);

        mChoiceRV.setLayoutManager(new LinearLayoutManager(getBaseActivity(), RecyclerView.VERTICAL, false));
        mChoiceRV.setAdapter(adapter);
        mChoiceRV.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
