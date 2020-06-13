package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailActivity;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GitRepositoriesPresenter implements GitRepositoriesMVP.Presenter {

    private static final String TAG = GitRepositoriesPresenter.class.getName();

    private GitUserModel gitUserModel = null;

    @Nullable
    private GitRepositoriesMVP.View view;
    private GitRepositoriesMVP.Model model;

    private Disposable getDataSubscription = null;

    public GitRepositoriesPresenter(GitRepositoriesMVP.Model model) {
        this.model = model;
    }

    @Override
    public void loadUserDetails() {
        Log.i(TAG, "loadUserDetails() has called");
        if (view != null) {
            if (gitUserModel == null) {
                Log.i(TAG, "The current git user is null. We try to get it from view extras");
                gitUserModel = view.getExtras().getParcelable(ExtraTags.EXTRA_GIT_USER);
            }
            if (gitUserModel != null) {
                Log.i(TAG, "Git details have gotten successfully");
                view.setAvatar(gitUserModel.getAvatarUrl());
                view.setUsername(gitUserModel.getUsername());
            } else {
                Log.e(TAG, "Git user details have not been recovered");
                view.showSnackBar("TODO: No se han podido recuperar los datos de usuario");
            }
        }
    }

    @Override
    public void loadRepositories() {
        if (view != null) {
            view.showProgress();
        }

        if (gitUserModel == null) {
            loadUserDetails();
        }

        if (gitUserModel != null) {
            getDataSubscription = model.getGitRepositories(gitUserModel.getUsername())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<GitRepositoryBasicModel>() {
                        @Override
                        public void onNext(GitRepositoryBasicModel repository) {
                            Log.i(TAG, "Git repository fetched: " + repository.getName());
                            if (view != null) {
                                view.addRepository(repository);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage(), e);
                            if (view != null) {
                                view.removeAllRepositories();
                                view.hideProgress();
                                view.showSnackBar("No public repositories found from " + gitUserModel.getUsername());
                            }
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG, "All repositories fetched from current call");
                            if (view != null) {
                                view.hideProgress();
                                view.showSnackBar("Repositorios recuperados con exito!");
                            }
                        }
                    });

        } else {
            Log.e(TAG, "Git user details have not been recovered");
            view.showSnackBar("TODO: No se han podido recuperar los repositorios porque el usuario no se encuentra disponible");
        }
    }

    @Override
    public void loadRepositoryDetails(GitRepositoryBasicModel repository) {
        Log.i(TAG, "loadRepositoryDetails() repository.name = " + repository.getName());

        if (gitUserModel == null) {
            loadUserDetails();
        }

        if (view != null) {
            if (gitUserModel != null) {
                Intent intentToRepoDetail = new Intent((Context) view, GitRepositoryDetailActivity.class);
                intentToRepoDetail.putExtra(ExtraTags.EXTRA_GIT_USER, gitUserModel);
                intentToRepoDetail.putExtra(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC, repository);
                view.navigateToNextActivity(intentToRepoDetail);
            } else {
                Log.e(TAG, "Git user details have not been recovered");
                view.showSnackBar("TODO: No se puede cargar el detalle del repositorio porque el usuario no esta disponible");
            }
        }
    }

    @Override
    public void rxJavaUnsubscribe() {
        Log.i(TAG, "rxJavaUnsubscribe() called");
        if (getDataSubscription != null && !getDataSubscription.isDisposed()) {
            getDataSubscription.dispose();
        }
    }

    @Override
    public void setView(GitRepositoriesMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
