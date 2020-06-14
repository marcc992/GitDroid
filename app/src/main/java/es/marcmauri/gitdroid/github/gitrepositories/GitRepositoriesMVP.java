package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import io.reactivex.Observable;

public interface GitRepositoriesMVP {

    interface View {
        void setRepositoryItems(List<GitRepositoryBasicModel> repositories);
        void addRepositoryItem(GitRepositoryBasicModel repository);
        void removeAllRepositories();
        void setRepositoryPosition(int position);

        void showProgress();
        void hideProgress();
        void showSnackBar(String message);

        void navigateToNextActivity(Intent i);
    }

    interface State {
        ArrayList<GitRepositoryBasicModel> getRepositoryItems();
        int getRepositoryPosition();
        int getCurrentPage();
        long getRepositoryLastSeenId();
        boolean getAllPagesRetrieved();
        boolean getSearchingReposByName();
        String getSearchQuery();
    }

    interface Presenter {
        void loadPublicRepositories();
        void loadRepositoryDetails(GitRepositoryBasicModel repository);

        void onSearchFieldChanges(String query);
        void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int pastVisibleItems, int dy);

        GitRepositoriesMVP.State getState();
        void onRepositoryPositionChange(int position);

        void unsubscribe();
        void subscribe(GitRepositoriesMVP.View view, GitRepositoriesMVP.State state);
    }

    interface Model {
        Observable<GitRepositoryBasicModel> getGitPublicRepositories(final long idLastRepoSeen);
        Observable<GitRepositoryBasicModel> getGitPublicRepositoriesByName(final String query, final int page);
    }
}
