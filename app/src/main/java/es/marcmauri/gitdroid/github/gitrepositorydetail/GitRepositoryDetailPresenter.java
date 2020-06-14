package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GitRepositoryDetailPresenter implements GitRepositoryDetailMVP.Presenter {

    private static final String TAG = GitRepositoryDetailPresenter.class.getName();

    private GitUserModel gitUserModel = null;
    private GitRepositoryBasicModel gitRepositoryBasicModel = null;

    @Nullable
    GitRepositoryDetailMVP.View view;
    GitRepositoryDetailMVP.Model model;

    private Disposable getDataSubscription = null;


    public GitRepositoryDetailPresenter(GitRepositoryDetailMVP.Model model) {
        this.model = model;
    }


    private void recoverBasicRepositoryDetails() {
        Log.i(TAG, "recoverBasicRepositoryDetails() has called");
        if (view != null) {
            if (gitRepositoryBasicModel == null) {
                Log.i(TAG, "Git repository basic details is null. We try to get it from view extras");
                gitRepositoryBasicModel = view.getExtras().getParcelable(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC);
            }
            if (gitUserModel != null) {
                Log.i(TAG, "Git repository basic details have gotten successfully");
            } else {
                Log.e(TAG, "Git repository basic details have not been recovered");
                view.showSnackBar("TODO: No se han podido recuperar los datos del repositorio seleccionado");
            }
        }
    }

    @Override
    public void recoverUserDetails() {
        Log.i(TAG, "recoverUserDetails() has called");
        if (view != null) {
            if (gitUserModel == null) {
                Log.i(TAG, "Git user details is null. We try to get it from view extras");
                gitUserModel = view.getExtras().getParcelable(ExtraTags.EXTRA_GIT_USER);
            }
            if (gitUserModel != null) {
                Log.i(TAG, "Git user details have gotten successfully");
                view.setUserName(gitUserModel.getUsername());
                view.setUserAvatar(gitUserModel.getAvatarUrl());
            } else {
                Log.e(TAG, "Git user details have not been recovered");
                view.showSnackBar("TODO: No se han podido recuperar los datos de usuario");
            }
        }
    }

    @Override
    public void loadRepositoryDetails() {
        Log.i(TAG, "loadRepositoryDetails() has called");

        if (view != null) {
            view.showProgress();
        }

        if (gitUserModel == null) {
            recoverUserDetails();
        }

        if (gitRepositoryBasicModel == null) {
            recoverBasicRepositoryDetails();
        }

        if (gitUserModel != null && gitRepositoryBasicModel != null) {
            getDataSubscription = model.getGitRepositoryDetail(gitUserModel.getUsername(), gitRepositoryBasicModel.getName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GitRepositoryDetailedModel>() {
                        @Override
                        public void accept(GitRepositoryDetailedModel gitRepositoryDetailedModel) {
                            if (view != null) {
                                view.setRepositoryName(gitRepositoryDetailedModel.getFullName());
                                view.setWebViewContent(gitRepositoryDetailedModel.getHtmlUrl());

                                view.hideProgress();
                            }
                        }
                    });

        } else {
            Log.e(TAG, "Activity Extras / Bundle could not be fetched successfully...");
            if (view != null) {
                view.hideProgress();
                view.showSnackBar("TODO: No se han podido recuperar los datos del usuario y del repositorio seleccionado");
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
    public void setView(GitRepositoryDetailMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
