package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailActivity;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GitRepositoriesPresenter implements GitRepositoriesMVP.Presenter {

    private static final String TAG = GitRepositoriesPresenter.class.getName();

    private long idLastRepoSeen = 0;
    private boolean loadingPage = false;
    private boolean allPagesRetrieved = false;

    @Nullable
    private GitRepositoriesMVP.View view;
    private GitRepositoriesMVP.Model model;

    private Disposable getDataSubscription = null;

    public GitRepositoriesPresenter(GitRepositoriesMVP.Model model) {
        this.model = model;
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

                getDataSubscription = model.getGitPublicRepositories(idLastRepoSeen)
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
                                    idLastRepoSeen = repository.getId();
                                    view.addRepository(repository);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getMessage(), e);
                                if (view != null) {
                                    view.removeAllRepositories();
                                    view.hideProgress();
                                    view.showSnackBar("No public repositories found");
                                    allPagesRetrieved = true;
                                    loadingPage = false;
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "All repositories fetched from current call");
                                if (view != null) {
                                    view.hideProgress();
                                    view.showSnackBar("Repositories till ID " + idLastRepoSeen + " fetched successfully!");
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
    public void loadPublicRepositories() {
        Log.i(TAG, "loadRepositories() has called");

        if (view != null) {
            view.showProgress();
        }

        getNextGitRepositoriesPage();
    }

    @Override
    public void loadRepositoryDetails(GitRepositoryBasicModel repository) {
        Log.i(TAG, "loadRepositoryDetails() repository.name = " + repository.getName());

        if (view != null) {
            Intent intentToRepoDetail = new Intent((Context) view, GitRepositoryDetailActivity.class);
            intentToRepoDetail.putExtra(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC, repository);
            view.navigateToNextActivity(intentToRepoDetail);
        }
    }

    @Override
    public void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int pastVisibleItems, int dy) {
        if (dy > 0) {
            if ((totalItemCount - visibleItemCount <= pastVisibleItems)) {
                if (view != null) {
                    Log.i(TAG, "onRecyclerViewScrolled()");

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
        idLastRepoSeen = 0;
        loadingPage = false;
        allPagesRetrieved = false;

    }

    @Override
    public void setView(GitRepositoriesMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
