package com.joyapeak.sarcazon.ui.profile.profilenotifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.ui.replies.RepliesActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public class ProfileNotificationsFragment extends BaseFragment implements ProfileNotificationsMvpView {

    @BindView(R.id.nsv_profile)
    NestedScrollView mParentNSV;

    @BindView(R.id.rv_profile_notifications)
    RecyclerView mNotificationsRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyView;

    @Inject
    ProfileNotificationsMvpPresenter<ProfileNotificationsMvpView> mPresenter;

    @Inject
    ProfileNotificationsAdapter mNotificationsAdapter;

    @Inject
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;


    public static ProfileNotificationsFragment newInstance() {
        ProfileNotificationsFragment fragment = new ProfileNotificationsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_notifications, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, root));

            mPresenter.onAttach(this);

            setUp(root);

            mPresenter.onViewInitialized();
        }

        return root;
    }

    @Override
    public void onNotificationsConsumed(List<ServerNotification> notifications,
                                        int newNotificationsCount) {
        hideLoadingDialog();
        mNotificationsAdapter.setTotalNotificationsCount(notifications.size());
        mPresenter.loadNotifications();
    }

    @Override
    public void onNewNotificationsLoaded(List<ServerNotification> loadedNotifications) {
        mNotificationsAdapter.addNotifications(loadedNotifications);
        mNotificationsAdapter.resetIsLoadingNewItems();
        mPresenter.setAllNotificationsAsOld();
    }

    @Override
    protected void setUp(View view) {
        setupNotificationsAdapter();
        setupNotificationsRV();
    }

    private void setupNotificationsAdapter() {
        mNotificationsAdapter.setCallback(new ProfileNotificationsAdapter.Callback() {
            @Override
            public void onItemClicked(int position, ServerNotification notificationData) {
                mPresenter.handleNotificationItemClick(notificationData);
            }

            @Override
            public void onProfileClicked(long profileId) {
                openProfilePage(profileId);
            }

            @Override
            public void onComicClicked(long comicId) {
                openComicPage(comicId);
            }
        });

        mNotificationsAdapter.setEmptyView(mEmptyView);
    }
    private void setupNotificationsRV() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNotificationsRV.setLayoutManager(mLayoutManager);
        mNotificationsRV.setAdapter(mNotificationsAdapter);

        mNotificationsAdapter.addNewItemsOnScrollListener(mParentNSV,
                mNotificationsRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {

                    @Override
                    public void onScrollReachedEnd() {
                        mPresenter.loadNotifications();
                    }
                });
    }


    @Override
    public void openProfilePage(long userId) {
        Intent profileIntent = ProfileActivity.getStartIntent(getActivity(), userId);
        profileIntent.putExtra(BaseActivity.EXTRA_PARENT_ACTIVITY, ProfileActivity.class.getSimpleName());
        startActivity(profileIntent);
    }

    @Override
    public void openComicPage(long comicId) {
        Intent comicViewIntent = ComicViewActivity.getStartIntent(getActivity(), comicId, null);
        comicViewIntent.putExtra(BaseActivity.EXTRA_PARENT_ACTIVITY, ProfileActivity.class.getSimpleName());
        startActivity(comicViewIntent);
    }

    @Override
    public void openCommentsPage(long comicId, long commentId) {
        Intent comicViewIntent = ComicViewActivity.getStartIntent(getActivity(), comicId, commentId);
        comicViewIntent.putExtra(BaseActivity.EXTRA_PARENT_ACTIVITY, ProfileActivity.class.getSimpleName());
        startActivity(comicViewIntent);
    }

    @Override
    public void openRepliesPage(long comicId, long commentId, long replyId) {
        Intent repliesIntent = RepliesActivity.getStartIntent(getActivity(), commentId);
        repliesIntent.putExtra(BaseActivity.EXTRA_PARENT_ACTIVITY, ProfileActivity.class.getSimpleName());
        startActivity(repliesIntent);
    }

    @Override
    public void openSubscribersPage() {
        ((ProfileActivity) getActivity()).openSubsActivity(true);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
