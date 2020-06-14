package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailActivity;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GitRepositoriesPresenter implements GitRepositoriesMVP.Presenter {

    private static final String TAG = GitRepositoriesPresenter.class.getName();

    private GitUserModel gitUserModel = null;
    private int lastReposPage = 0;
    private boolean loadingPage = false;
    private boolean allPagesRetrieved = false;

    @Nullable
    private GitRepositoriesMVP.View view;
    private GitRepositoriesMVP.Model model;

    private Disposable getDataSubscription = null;

    public GitRepositoriesPresenter(GitRepositoriesMVP.Model model) {
        this.model = model;
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
                view.setAvatar(gitUserModel.getAvatarUrl());
                view.setUsername(gitUserModel.getUsername());
            } else {
                Log.e(TAG, "Git user details have not been recovered");
                view.showSnackBar("TODO: No se han podido recuperar los datos de usuario");
            }
        }
    }

    private void getNextGitRepositoriesPage() {
        Log.i(TAG, "getNextGitRepositoriesPage() called");

        if (!allPagesRetrieved) {
            if (!loadingPage) {
                Log.i(TAG, "There is not a loading page process. Let's to load the next page!");
                loadingPage = true;

                if (view != null) {
                    view.showProgress();
                }

                getDataSubscription = model.getGitRepositories(gitUserModel.getUsername(), ++lastReposPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .switchIfEmpty(new Observable<GitRepositoryBasicModel>() {
                            @Override
                            protected void subscribeActual(Observer<? super GitRepositoryBasicModel> observer) {
                                Log.e(TAG, "No more pages to retrieve! The previous call was the last one.");
                                if (view != null) {
                                    view.hideProgress();
                                    view.showSnackBar("All pages were retrieved");
                                }
                                allPagesRetrieved = true;
                                loadingPage = false;
                            }
                        })
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
                                    allPagesRetrieved = true;
                                    loadingPage = false;
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "All repositories fetched from current call");
                                if (view != null) {
                                    view.hideProgress();
                                    view.showSnackBar("Repositories from page " + lastReposPage + " fetched successfully!");
                                    loadingPage = false;
                                }
                            }
                        });

            } else {
                Log.i(TAG, "There is a loading page process. Aborting his new load...");
            }

        } else {
            Log.i(TAG, "There are not more pages to retrieve from GitHub server");
        }
    }

    @Override
    public void loadRepositories() {
        Log.i(TAG, "loadRepositories() has called");

        if (view != null) {
            view.showProgress();
        }

        if (gitUserModel == null) {
            recoverUserDetails();
        }

        if (gitUserModel != null) {
            getNextGitRepositoriesPage();

        } else {
            Log.e(TAG, "Git user details have not been recovered");
            if (view != null) {
                view.hideProgress();
                view.showSnackBar("TODO: No se han podido recuperar los repositorios porque el usuario no se encuentra disponible");
            }
        }
    }

    @Override
    public void loadRepositoryDetails(GitRepositoryBasicModel repository) {
        Log.i(TAG, "loadRepositoryDetails() repository.name = " + repository.getName());

        if (gitUserModel == null) {
            recoverUserDetails();
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
    public void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int pastVisibleItems, int dy) {
        if (dy > 0) {
            if ((totalItemCount - visibleItemCount <= pastVisibleItems)) {
                if (view != null) {
                    getNextGitRepositoriesPage();
                }
            }
        }
    }

    @Override
    public void rxJavaUnsubscribe() {
        Log.i(TAG, "rxJavaUnsubscribe() called");
        if (getDataSubscription != null && !getDataSubscription.isDisposed()) {
            getDataSubscription.dispose();
        }

        // TODO: Guardar en bundle para recuperar el estado una vez volvamos del detalle
        Log.i(TAG, "Then the pages controller must be restored as defaults");
        lastReposPage = 0;
        loadingPage = false;
        allPagesRetrieved = false;
    }

    @Override
    public void setView(GitRepositoriesMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
