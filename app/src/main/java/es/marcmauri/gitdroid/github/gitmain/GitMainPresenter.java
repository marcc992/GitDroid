package es.marcmauri.gitdroid.github.gitmain;

import android.util.Log;

import androidx.annotation.Nullable;

public class GitMainPresenter implements GitMainMVP.Presenter {

    private static final String TAG = GitMainPresenter.class.getName();

    @Nullable
    private GitMainMVP.View view;


    @Override
    public void loadPublicRepositories() {
        Log.i(TAG, "goToPublicRepositories() called");
        if (view != null) {
            view.goToGitRepositories();
        }
    }

    @Override
    public void setView(GitMainMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
