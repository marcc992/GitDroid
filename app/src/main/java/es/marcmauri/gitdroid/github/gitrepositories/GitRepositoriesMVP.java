package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Intent;
import android.os.Bundle;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import io.reactivex.Observable;

public interface GitRepositoriesMVP {

    interface View {
        void setAvatar(String url);
        void setUsername(String username);

        void addRepository(GitRepositoryBasicModel repository);
        void removeAllRepositories();

        void showProgress();
        void hideProgress();
        void showSnackBar(String message);

        Bundle getExtras();
        void navigateToNextActivity(Intent i);
    }

    interface Presenter {
        void recoverUserDetails();
        void loadRepositories();
        void loadRepositoryDetails(GitRepositoryBasicModel repository);

        void rxJavaUnsubscribe();

        void setView(GitRepositoriesMVP.View view);
    }

    interface Model {
        // TODO: Pagination
        Observable<GitRepositoryBasicModel> getGitRepositories(String username);
    }
}
