package com.joyapeak.sarcazon.di.component;

import com.joyapeak.sarcazon.di.PerActivity;
import com.joyapeak.sarcazon.di.module.ActivityModule;
import com.joyapeak.sarcazon.ui.bottomsheetlist.BottomSheetListDialog;
import com.joyapeak.sarcazon.ui.category.CategorySelectionActivity;
import com.joyapeak.sarcazon.ui.comic.SingleComicFragment;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.comments.CommentsActivity;
import com.joyapeak.sarcazon.ui.leaderboard.LeaderboardActivity;
import com.joyapeak.sarcazon.ui.leaderboard.leaderboard.LeaderboardFragment;
import com.joyapeak.sarcazon.ui.login.LoginActivity;
import com.joyapeak.sarcazon.ui.main.MainActivity;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.newmain.maincomics.MainComicsFragment;
import com.joyapeak.sarcazon.ui.pendingcommentsforreview.PendingCommentsForReviewActivity;
import com.joyapeak.sarcazon.ui.photosource.PhotoSourceDialog;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsFragment;
import com.joyapeak.sarcazon.ui.profile.profilenotifications.ProfileNotificationsFragment;
import com.joyapeak.sarcazon.ui.profilesettings.ProfileSettingsActivity;
import com.joyapeak.sarcazon.ui.registerprompt.RegisterPromptDialog;
import com.joyapeak.sarcazon.ui.replies.RepliesActivity;
import com.joyapeak.sarcazon.ui.report.ReportTypesBSDialog;
import com.joyapeak.sarcazon.ui.search.SearchActivity;
import com.joyapeak.sarcazon.ui.search.searchcomics.SearchComicsFragment;
import com.joyapeak.sarcazon.ui.search.searchusers.SearchUsersFragment;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialog;
import com.joyapeak.sarcazon.ui.share.photo.SharePhotoDialog;
import com.joyapeak.sarcazon.ui.splash.SplashActivity;
import com.joyapeak.sarcazon.ui.subs.SubsActivity;
import com.joyapeak.sarcazon.ui.tutorial.TutorialActivity;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationActivity;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.category.UploadCategoryFragment;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.tags.UploadTagsFragment;
import com.joyapeak.sarcazon.ui.upload.uploadoptions.UploadOptionsActivity;
import com.joyapeak.sarcazon.ui.upload.urlpaste.UrlPasteDialog;

import dagger.Component;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(NewMainActivity activity);

    void inject(CommentsActivity activity);

    void inject(RepliesActivity activity);

    void inject(ProfileActivity activity);

    void inject(ProfileSettingsActivity activity);

    void inject (UploadOptionsActivity activity);

    void inject (UploadConfirmationActivity activity);

    void inject (ComicViewActivity activity);

    void inject(SubsActivity activity);

    void inject(LoginActivity activity);

    void inject(SearchActivity activity);

    void inject(TutorialActivity activity);

    void inject(LeaderboardActivity activity);

    void inject(CategorySelectionActivity activity);

    void inject(PendingCommentsForReviewActivity activity);

    void inject(SingleComicFragment fragment);

    void inject(MainComicsFragment fragment);

    void inject(ProfileNotificationsFragment fragment);

    void inject(ProfileComicsFragment fragment);

    void inject(SearchComicsFragment fragment);

    void inject(SearchUsersFragment fragment);

    void inject(BottomSheetListDialog fragment);

    void inject(LeaderboardFragment fragment);

    void inject(UploadTagsFragment fragment);

    void inject(UploadCategoryFragment fragment);

    void inject(SharePhotoDialog dialog);

    void inject(ShareLinkDialog dialog);

    void inject(PhotoSourceDialog dialog);

    void inject(ReportTypesBSDialog dialog);

    void inject(RegisterPromptDialog dialog);

    void inject(UrlPasteDialog dialog);
}
