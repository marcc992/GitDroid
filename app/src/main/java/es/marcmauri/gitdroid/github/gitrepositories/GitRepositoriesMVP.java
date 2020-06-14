package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Intent;
import android.os.Bundle;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import io.reactivex.Observable;

public interface GitRepositoriesMVP {

    interface View {

        void addRepository(GitRepositoryBasicModel repository);
        void removeAllRepositories();

        void showProgress();
        void hideProgress();
        void showSnackBar(String message);

        void navigateToNextActivity(Intent i);
    }

    interface Presenter {
        void loadPublicRepositories();
        void loadRepositoryDetails(GitRepositoryBasicModel repository);

        void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int pastVisibleItems, int dy);

        void rxJavaUnsubscribe();

        void setView(GitRepositoriesMVP.View view);
    }

    interface Model {
        Observable<GitRepositoryBasicModel> getGitPublicRepositories(long idLastRepoSeen);
    }
}
