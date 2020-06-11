package es.marcmauri.gitdroid.github.searchuser;

import android.content.Intent;

import es.marcmauri.gitdroid.github.GitUserViewModel;
import io.reactivex.Observable;

public interface GitSearchUserMVP {

    interface View {
        void showProgress();

        void hideProgress();

        void setUserNotExist(String message);

        void setUserExists();

        void showSnackBar(String message);

        void navigateToNextActivity(Intent i);
    }

    interface Presenter {
        void checkIfUserExists(String user);

        void rxJavaUnsubscribe();

        void setView(GitSearchUserMVP.View view);
    }

    interface Model {
        Observable<GitUserViewModel> getGitUserDetails(String username);
    }
}
