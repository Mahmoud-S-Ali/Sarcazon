package com.joyapeak.sarcazon.ui.report;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.BasicListAdapter;
import com.joyapeak.sarcazon.ui.base.BaseBottomSheetDialogFragment;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 6/25/2018.
 */

public class ReportTypesBSDialog extends BaseBottomSheetDialogFragment implements ReportTypesBSDialogMvpView {
    private final static String TAG = "ReportTypesDialog";

    @BindView(R.id.rv_dialogBasicList)
    RecyclerView mChoiceRV;

    @Inject
    ReportTypesBSDialogMvpPresenter<ReportTypesBSDialogMvpView> mPresenter;

    @Inject
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    private static final int REPORT_TYPE_NUDITY_POS = 0;
    private static final int REPORT_TYPE_HATE_POS = 1;
    private static final int REPORT_TYPE_LANGUAGE_POS = 2;
    private static final int REPORT_TYPE_VIOLENCE_POS = 3;
    private static final int REPORT_TYPE_SPAM_POS = 4;

    private long mItemId;
    private static String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private ReportTypesDialogHandler mHandler;


    public interface ReportTypesDialogHandler {
        void onReportSuccessful();
    }

    // Creating new instance
    public static ReportTypesBSDialog newInstance(long itemId) {
        ReportTypesBSDialog fragment = new ReportTypesBSDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ITEM_ID, itemId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (ReportTypesDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_basic_list, container, false);

        mItemId = getArguments().getLong(EXTRA_ITEM_ID);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);
            setUnBinder(ButterKnife.bind(this, view));
        }

        return view;
    }


    @Override
    protected String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {
        List<String> reportTypesList = Arrays.asList(
                getActivity().getResources().getStringArray(R.array.report_types));

        BasicListAdapter reportTypesAdapter = new BasicListAdapter(reportTypesList,
                new BasicListAdapter.Callback() {

                    @Override
                    public void onItemClicked(int position) {
                        switch (position) {

                            case REPORT_TYPE_NUDITY_POS:
                                mPresenter.reportComic(mItemId, ApiHelper.REPORT_TYPES.NUDITY);
                                break;

                            case REPORT_TYPE_HATE_POS:
                                mPresenter.reportComic(mItemId, ApiHelper.REPORT_TYPES.HATE);
                                break;

                            case REPORT_TYPE_LANGUAGE_POS:
                                mPresenter.reportComic(mItemId, ApiHelper.REPORT_TYPES.LANGUAGE);
                                break;

                            case REPORT_TYPE_VIOLENCE_POS:
                                mPresenter.reportComic(mItemId, ApiHelper.REPORT_TYPES.VIOLENCE);
                                break;

                            case REPORT_TYPE_SPAM_POS:
                                mPresenter.reportComic(mItemId, ApiHelper.REPORT_TYPES.SPAM);
                                break;
                        }
                    }
                });

        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mChoiceRV.setLayoutManager(mLayoutManager);
        mChoiceRV.setAdapter(reportTypesAdapter);
        mChoiceRV.setHasFixedSize(true);
    }

    @Override
    public void onReportSuccess() {
        mHandler.onReportSuccessful();
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null && context instanceof ReportTypesDialogHandler) {
                mHandler = (ReportTypesDialogHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }
}
