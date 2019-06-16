package com.joyapeak.sarcazon.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.data.network.model.server.search.SearchResponse;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;
import com.joyapeak.sarcazon.di.ActivityContext;
import com.joyapeak.sarcazon.di.PerActivity;
import com.joyapeak.sarcazon.helper.FBVideoSourceGrabber;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.UsersAdapter;
import com.joyapeak.sarcazon.ui.category.CategoryMvpPresenter;
import com.joyapeak.sarcazon.ui.category.CategoryMvpView;
import com.joyapeak.sarcazon.ui.category.CategoryPresenter;
import com.joyapeak.sarcazon.ui.category.MultipleCategoryAdapter;
import com.joyapeak.sarcazon.ui.category.SingleCategoryAdapter;
import com.joyapeak.sarcazon.ui.comic.SingleComicMvpPresenter;
import com.joyapeak.sarcazon.ui.comic.SingleComicMvpView;
import com.joyapeak.sarcazon.ui.comic.SingleComicPresenter;
import com.joyapeak.sarcazon.ui.comics.ComicsMvpPresenter;
import com.joyapeak.sarcazon.ui.comics.ComicsMvpView;
import com.joyapeak.sarcazon.ui.comics.ComicsPresenter;
import com.joyapeak.sarcazon.ui.comicview.ComicViewMvpPresenter;
import com.joyapeak.sarcazon.ui.comicview.ComicViewMvpView;
import com.joyapeak.sarcazon.ui.comicview.ComicViewPresenter;
import com.joyapeak.sarcazon.ui.comments.CommentsMvpPresenter;
import com.joyapeak.sarcazon.ui.comments.CommentsMvpView;
import com.joyapeak.sarcazon.ui.comments.CommentsPresenter;
import com.joyapeak.sarcazon.ui.comments.GeneralCommentsAdapter;
import com.joyapeak.sarcazon.ui.comments.ReportedCommentsAdapter;
import com.joyapeak.sarcazon.ui.leaderboard.leaderboard.LeaderboardAdapter;
import com.joyapeak.sarcazon.ui.leaderboard.leaderboard.LeaderboardMvpPresenter;
import com.joyapeak.sarcazon.ui.leaderboard.leaderboard.LeaderboardMvpView;
import com.joyapeak.sarcazon.ui.leaderboard.leaderboard.LeaderboardPresenter;
import com.joyapeak.sarcazon.ui.login.LoginMvpPresenter;
import com.joyapeak.sarcazon.ui.login.LoginMvpView;
import com.joyapeak.sarcazon.ui.login.LoginPresenter;
import com.joyapeak.sarcazon.ui.main.MainMvpPresenter;
import com.joyapeak.sarcazon.ui.main.MainMvpView;
import com.joyapeak.sarcazon.ui.main.MainPresenter;
import com.joyapeak.sarcazon.ui.newmain.NavViewAdapter;
import com.joyapeak.sarcazon.ui.newmain.maincomics.MainComicsAdapter;
import com.joyapeak.sarcazon.ui.pendingcommentsforreview.PendingCommentsForReviewMvpPresenter;
import com.joyapeak.sarcazon.ui.pendingcommentsforreview.PendingCommentsForReviewMvpView;
import com.joyapeak.sarcazon.ui.pendingcommentsforreview.PendingCommentsForReviewPresenter;
import com.joyapeak.sarcazon.ui.photosource.PhotoSourceDialogMvpPresenter;
import com.joyapeak.sarcazon.ui.photosource.PhotoSourceDialogMvpView;
import com.joyapeak.sarcazon.ui.photosource.PhotoSourceDialogPresenter;
import com.joyapeak.sarcazon.ui.profile.ProfileMvpPresenter;
import com.joyapeak.sarcazon.ui.profile.ProfileMvpView;
import com.joyapeak.sarcazon.ui.profile.ProfilePresenter;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsAdapter;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsMvpPresenter;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsMvpView;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsPresenter;
import com.joyapeak.sarcazon.ui.profile.profilenotifications.ProfileNotificationsAdapter;
import com.joyapeak.sarcazon.ui.profile.profilenotifications.ProfileNotificationsMvpPresenter;
import com.joyapeak.sarcazon.ui.profile.profilenotifications.ProfileNotificationsMvpView;
import com.joyapeak.sarcazon.ui.profile.profilenotifications.ProfileNotificationsPresenter;
import com.joyapeak.sarcazon.ui.profilesettings.ProfileSettingsMvpPresenter;
import com.joyapeak.sarcazon.ui.profilesettings.ProfileSettingsMvpView;
import com.joyapeak.sarcazon.ui.profilesettings.ProfileSettingsPresenter;
import com.joyapeak.sarcazon.ui.registerprompt.RegisterPromptDialogMvpPresenter;
import com.joyapeak.sarcazon.ui.registerprompt.RegisterPromptDialogMvpView;
import com.joyapeak.sarcazon.ui.registerprompt.RegisterPromptDialogPresenter;
import com.joyapeak.sarcazon.ui.replies.RepliesMvpPresenter;
import com.joyapeak.sarcazon.ui.replies.RepliesMvpView;
import com.joyapeak.sarcazon.ui.replies.RepliesPresenter;
import com.joyapeak.sarcazon.ui.report.ReportTypesBSDialogMvpPresenter;
import com.joyapeak.sarcazon.ui.report.ReportTypesBSDialogMvpView;
import com.joyapeak.sarcazon.ui.report.ReportTypesBSDialogPresenter;
import com.joyapeak.sarcazon.ui.search.SearchMvpPresenter;
import com.joyapeak.sarcazon.ui.search.SearchMvpView;
import com.joyapeak.sarcazon.ui.search.SearchPresenter;
import com.joyapeak.sarcazon.ui.search.searchcomics.SearchComicsMvpPresenter;
import com.joyapeak.sarcazon.ui.search.searchcomics.SearchComicsMvpView;
import com.joyapeak.sarcazon.ui.search.searchcomics.SearchComicsPresenter;
import com.joyapeak.sarcazon.ui.search.searchusers.SearchUsersMvpPresenter;
import com.joyapeak.sarcazon.ui.search.searchusers.SearchUsersMvpView;
import com.joyapeak.sarcazon.ui.search.searchusers.SearchUsersPresenter;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialogMvpPresenter;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialogMvpView;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialogPresenter;
import com.joyapeak.sarcazon.ui.share.photo.SharePhotoDialogMvpPresenter;
import com.joyapeak.sarcazon.ui.share.photo.SharePhotoDialogMvpView;
import com.joyapeak.sarcazon.ui.share.photo.SharePhotoDialogPresenter;
import com.joyapeak.sarcazon.ui.splash.SplashMvpPresenter;
import com.joyapeak.sarcazon.ui.splash.SplashMvpView;
import com.joyapeak.sarcazon.ui.splash.SplashPresenter;
import com.joyapeak.sarcazon.ui.subs.SubsAdapter;
import com.joyapeak.sarcazon.ui.subs.SubsMvpPresenter;
import com.joyapeak.sarcazon.ui.subs.SubsMvpView;
import com.joyapeak.sarcazon.ui.subs.SubsPresenter;
import com.joyapeak.sarcazon.ui.tutorial.TutorialMvpPresenter;
import com.joyapeak.sarcazon.ui.tutorial.TutorialMvpView;
import com.joyapeak.sarcazon.ui.tutorial.TutorialPresenter;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationMvpPresenter;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationMvpView;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationPresenter;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.tags.TagsAdapter;
import com.joyapeak.sarcazon.ui.upload.uploadoptions.UploadOptionsMvpPresenter;
import com.joyapeak.sarcazon.ui.upload.uploadoptions.UploadOptionsMvpView;
import com.joyapeak.sarcazon.ui.upload.uploadoptions.UploadOptionsPresenter;
import com.joyapeak.sarcazon.ui.upload.urlpaste.UrlPasteDialogMvpPresenter;
import com.joyapeak.sarcazon.ui.upload.urlpaste.UrlPasteDialogMvpView;
import com.joyapeak.sarcazon.ui.upload.urlpaste.UrlPasteDialogPresenter;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    MyPagerAdapter provideInfinityAdapterPager(AppCompatActivity activity) {
        return new MyPagerAdapter(activity.getSupportFragmentManager());
    }


    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ComicViewMvpPresenter<ComicViewMvpView> provideComicViewPresenter(ComicViewPresenter<ComicViewMvpView> presenter) {
        return presenter;
    }

    @Provides
    ComicsMvpPresenter<ComicsMvpView> provideComicsMvpPresenter(
            ComicsPresenter<ComicsMvpView> presenter) {
        return presenter;
    }

    @Provides
    SingleComicMvpPresenter<SingleComicMvpView> provideSingleComicPresenter(
            SingleComicPresenter<SingleComicMvpView> presenter) {
        return presenter;
    }

    @Provides
    SearchComicsMvpPresenter<SearchComicsMvpView> provideSearchComicsPresenter(
            SearchComicsPresenter<SearchComicsMvpView> presenter) {
        return presenter;
    }

    @Provides
    SearchUsersMvpPresenter<SearchUsersMvpView> provideSearchUsersPresenter(
            SearchUsersPresenter<SearchUsersMvpView> presenter) {
        return presenter;
    }

    @Provides
    SharePhotoDialogMvpPresenter<SharePhotoDialogMvpView> provideSharePhotoDialogPresenter(
            SharePhotoDialogPresenter<SharePhotoDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    ShareLinkDialogMvpPresenter<ShareLinkDialogMvpView> provideShareLinkDialogPresenter(
            ShareLinkDialogPresenter<ShareLinkDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CommentsMvpPresenter<CommentsMvpView> provideCommentsPresenter(
            CommentsPresenter<CommentsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RepliesMvpPresenter<RepliesMvpView> provideRepliesPresenter(
            RepliesPresenter<RepliesMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ProfileMvpPresenter<ProfileMvpView> provideProfilePresenter(
            ProfilePresenter<ProfileMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ProfileSettingsMvpPresenter<ProfileSettingsMvpView> provideProfileSettingsPresenter(
            ProfileSettingsPresenter<ProfileSettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SubsMvpPresenter<SubsMvpView> provideSubsPresenter(
            SubsPresenter<SubsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UploadOptionsMvpPresenter<UploadOptionsMvpView> provideUploadPresenter(
            UploadOptionsPresenter<UploadOptionsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UploadConfirmationMvpPresenter<UploadConfirmationMvpView> provideUploadConfirmationPresenter(
            UploadConfirmationPresenter<UploadConfirmationMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SearchMvpPresenter<SearchMvpView> provideSearchPresenter(
            SearchPresenter<SearchMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    TutorialMvpPresenter<TutorialMvpView> provideTutorialPresenter(
            TutorialPresenter<TutorialMvpView> presenter) {
        return presenter;
    }

    @Provides
    PendingCommentsForReviewMvpPresenter<PendingCommentsForReviewMvpView> provideReportedCommentsPresenter(
            PendingCommentsForReviewPresenter<PendingCommentsForReviewMvpView> presenter) {
        return presenter;
    }

    @Provides
    ProfileNotificationsMvpPresenter<ProfileNotificationsMvpView> provideProfileCommentsPresenter(
            ProfileNotificationsPresenter<ProfileNotificationsMvpView> presenter) {
        return presenter;
    }

    @Provides
    ProfileComicsMvpPresenter<ProfileComicsMvpView> provideProfileComicsPresenter(
            ProfileComicsPresenter<ProfileComicsMvpView> presenter) {
        return presenter;
    }

    @Provides
    LeaderboardMvpPresenter<LeaderboardMvpView> provideLeaderboardPresenter(
            LeaderboardPresenter<LeaderboardMvpView> presenter) {
        return presenter;
    }

    @Provides
    CategoryMvpPresenter<CategoryMvpView> provideCategoryPresenter(
            CategoryPresenter<CategoryMvpView> presenter) {
        return presenter;
    }

    @Provides
    PhotoSourceDialogMvpPresenter<PhotoSourceDialogMvpView> providePhotoSourceDialogPresenter(
            PhotoSourceDialogPresenter<PhotoSourceDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    ReportTypesBSDialogMvpPresenter<ReportTypesBSDialogMvpView> provideReportTypesDialogPresenter(
            ReportTypesBSDialogPresenter<ReportTypesBSDialogMvpView> presenter) {

        return presenter;
    }

    @Provides
    RegisterPromptDialogMvpPresenter<RegisterPromptDialogMvpView> provideRegisterPromptDialogPresenter(
            RegisterPromptDialogPresenter<RegisterPromptDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    UrlPasteDialogMvpPresenter<UrlPasteDialogMvpView> provideUrlPasteDialogPresenter(
            UrlPasteDialogPresenter<UrlPasteDialogMvpView> presenter) {
        return presenter;
    }


    @Provides
    NavViewAdapter provideNavViewAdapter() {
        NavViewAdapter navViewAdapter = new NavViewAdapter(new ArrayList<String>());
        navViewAdapter.setHasStableIds(true);
        return navViewAdapter;
    }

    @Provides
    MainComicsAdapter provideMainComicsAdapter() {
        MainComicsAdapter mainComicsAdapter = new MainComicsAdapter(new ArrayList<ComicResponse.SingleComic>());
        mainComicsAdapter.setHasStableIds(true);
        return mainComicsAdapter;
    }

    @Provides
    GeneralCommentsAdapter provideGeneralCommentsAdapter() {
        GeneralCommentsAdapter commentsAdapter = new GeneralCommentsAdapter(
                new ArrayList<CommentResponse.ComicComment>());
        commentsAdapter.setHasStableIds(true);
        return commentsAdapter;
    }

    @Provides
    ReportedCommentsAdapter provideReportedCommentsAdapter() {
        ReportedCommentsAdapter commentsAdapter = new ReportedCommentsAdapter(
                new ArrayList<CommentResponse.ComicComment>());
        commentsAdapter.setHasStableIds(true);
        return commentsAdapter;
    }

    @Provides
    ProfileComicsAdapter provideProfileComicsAdapter() {
        ProfileComicsAdapter comicsAdapter = new ProfileComicsAdapter(new ArrayList<ProfileResponse.ProfileComicInfo>());
        comicsAdapter.setHasStableIds(true);
        return comicsAdapter;
    }

    @Provides
    SubsAdapter provideSubsAdapter() {
        SubsAdapter subsAdapter = new SubsAdapter(new ArrayList<SubsResponse.UserSub>());
        subsAdapter.setHasStableIds(true);
        return subsAdapter;
    }

    @Provides
    UsersAdapter provideUsersAdapter() {
        UsersAdapter usersAdapter = new UsersAdapter(new ArrayList<SearchResponse.SingleUserSearch>());
        usersAdapter.setHasStableIds(true);
        return usersAdapter;
    }

    @Provides
    ProfileNotificationsAdapter provideNotificationsAdapter() {
        ProfileNotificationsAdapter notificationsAdapter = new ProfileNotificationsAdapter(new ArrayList<ServerNotification>());
        notificationsAdapter.setHasStableIds(true);
        return notificationsAdapter;
    }

    @Provides
    LeaderboardAdapter provideLeaderboardAdapter() {
        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(
                new ArrayList<LeaderboardResponse.LeaderboardUserItem>());
        leaderboardAdapter.setHasStableIds(true);
        return leaderboardAdapter;
    }

    @Provides
    TagsAdapter provideTagsAdapter() {
        TagsAdapter tagsAdapter = new TagsAdapter();
        tagsAdapter.setHasStableIds(true);
        return tagsAdapter;
    }

    @Provides
    SingleCategoryAdapter provideSingleCategoryAdapter() {
        SingleCategoryAdapter singleCategoryAdapter =
                new SingleCategoryAdapter(new ArrayList<CategoryResponse.SingleCategory>());

        singleCategoryAdapter.setHasStableIds(true);
        return singleCategoryAdapter;
    }

    @Provides
    MultipleCategoryAdapter provideMultipleCategoryAdapter() {
        MultipleCategoryAdapter multipleCategoryAdapter =
                new MultipleCategoryAdapter(new ArrayList<CategoryResponse.SingleCategory>(), new ArrayList<String>());

        multipleCategoryAdapter.setHasStableIds(true);
        return multipleCategoryAdapter;
    }

    @Provides
    @Named("scrollable_layoutmanager")
    LinearLayoutManager provideScrollableLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager provideNonScrollableLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    @Provides
    FBVideoSourceGrabber provideFBVideoSourceGrabber() {
        FBVideoSourceGrabber fbVideoSourceGrabber = new FBVideoSourceGrabber();
        return fbVideoSourceGrabber;
    }
}
