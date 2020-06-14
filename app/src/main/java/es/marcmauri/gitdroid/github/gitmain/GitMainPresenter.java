package es.marcmauri.gitdroid.github.gitmain;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesActivity;

public class GitMainPresenter implements GitMainMVP.Presenter {

    private static final String TAG = GitMainPresenter.class.getName();

    @Nullable
    private GitMainMVP.View view;


    @Override
    public void goToPublicRepositories() {
        Log.i(TAG, "goToPublicRepositories() called");
        if (view != null) {
            Intent intentToRepos = new Intent((Context) view, GitRepositoriesActivity.class);
            view.navigateToNextActivity(intentToRepos);
        }
    }

    @Override
    public void setView(GitMainMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
