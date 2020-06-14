package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GitRepositoryDetailPresenter implements GitRepositoryDetailMVP.Presenter {

    private static final String TAG = GitRepositoryDetailPresenter.class.getName();

    private GitRepositoryBasicModel gitRepositoryBasicModel = null;
    boolean loadingUserInfo;
    boolean loadingRepoInfo;

    @Nullable
    GitRepositoryDetailMVP.View view;
    GitRepositoryDetailMVP.Model model;

    private Disposable getOwnerSubscription = null;
    private Disposable getRepositorySubscription = null;


    public GitRepositoryDetailPresenter(GitRepositoryDetailMVP.Model model) {
        this.model = model;
    }


    private void recoverBasicRepositoryDetails() {
        Log.i(TAG, "recoverBasicRepositoryDetails() has called");
        if (view != null) {
            if (gitRepositoryBasicModel == null) {
                Log.i(TAG, "Git repository basic details is null. We try to get it from view extras");
                if (view.getExtras().getParcelable(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC) != null) {
                    gitRepositoryBasicModel = view.getExtras().getParcelable(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC);
                }
            }
        }
    }

    @Override
    public void loadOwnerDetails() {
        Log.i(TAG, "loadOwnerDetails() has called");

        if (view != null) {
            view.showProgress();
            loadingUserInfo = true;
        }

        if (gitRepositoryBasicModel == null) {
            recoverBasicRepositoryDetails();
        }

        if (gitRepositoryBasicModel != null) {
            getOwnerSubscription = model.getGitUserDetails(gitRepositoryBasicModel.getOwnerName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(TAG, "User " + gitRepositoryBasicModel.getOwnerName() + " not found!");
                            loadingUserInfo = false;

                            if (view != null) {
                                view.showSnackBar("Git User " + gitRepositoryBasicModel.getOwnerName() + " does not found!");

                                if (!loadingRepoInfo) {
                                    view.hideProgress();
                                }
                            }

                            Log.e(TAG, throwable.getMessage(), throwable);
                        }
                    })
                    .subscribe(new Consumer<GitUserModel>() {
                        @Override
                        public void accept(GitUserModel gitUserModel) {
                            Log.i(TAG, "Repository " + gitUserModel.getUsername() + " found! It will set on view");
                            loadingUserInfo = false;

                            if (view != null) {
                                view.setUserName(gitUserModel.getUsername());
                                view.setUserAvatar(gitUserModel.getAvatarUrl());

                                if (!loadingRepoInfo) {
                                    view.hideProgress();
                                }
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
    public void loadRepositoryDetails() {
        Log.i(TAG, "loadRepositoryDetails() has called");

        if (view != null) {
            view.showProgress();
            loadingRepoInfo = true;
        }

        if (gitRepositoryBasicModel == null) {
            recoverBasicRepositoryDetails();
        }

        if (gitRepositoryBasicModel != null) {
            getRepositorySubscription = model.getGitRepositoryDetail(gitRepositoryBasicModel.getOwnerName(), gitRepositoryBasicModel.getName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(TAG, "Repository " + gitRepositoryBasicModel.getName() + " not found!");
                            loadingRepoInfo = false;

                            if (view != null) {
                                view.showSnackBar("The detailed " + gitRepositoryBasicModel.getName() + " does not found!");

                                if (!loadingUserInfo) {
                                    view.hideProgress();
                                }
                            }

                            Log.e(TAG, throwable.getMessage(), throwable);
                        }
                    })
                    .subscribe(new Consumer<GitRepositoryDetailedModel>() {
                        @Override
                        public void accept(GitRepositoryDetailedModel gitRepositoryDetailedModel) {
                            Log.i(TAG, "Repository " + gitRepositoryDetailedModel.getName() + " found! It will set on view");
                            loadingRepoInfo = false;

                            if (view != null) {
                                view.setRepositoryName(gitRepositoryDetailedModel.getFullName());
                                view.setWebViewContent(gitRepositoryDetailedModel.getHtmlUrl());

                                if (!loadingUserInfo) {
                                    view.hideProgress();
                                }
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
        if (getRepositorySubscription != null && !getRepositorySubscription.isDisposed()) {
            getRepositorySubscription.dispose();
        }
        if (getOwnerSubscription != null && !getOwnerSubscription.isDisposed()) {
            getOwnerSubscription.dispose();
        }
    }

    @Override
    public void setView(GitRepositoryDetailMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
