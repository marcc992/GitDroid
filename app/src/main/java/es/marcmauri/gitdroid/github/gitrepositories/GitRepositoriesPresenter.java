package es.marcmauri.gitdroid.github.gitrepositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GitRepositoriesPresenter implements GitRepositoriesMVP.Presenter {

    private static final String TAG = GitRepositoriesPresenter.class.getName();

    // Control values
    // Persistence
    private ArrayList<GitRepositoryBasicModel> repositoryItems;
    private int repositoryPosition = 0;
    private boolean loadingPage = false;
    private boolean allPagesRetrieved = false;
    private int currentPage = 0;
    // All repositories
    private long repositoryLastSeenId = 0;
    // Repositories by name
    private boolean searchingReposByName = false;
    private String searchQuery = "";

    @Nullable
    private GitRepositoriesMVP.View view;
    private GitRepositoriesMVP.Model model;

    private Disposable getDataSubscription = null;

    public GitRepositoriesPresenter(GitRepositoriesMVP.Model model) {
        this.model = model;
    }

    @Override
    public void loadPublicRepositories() {
        Log.i(TAG, "loadRepositories() has called");
        getNextGitRepositoriesPage();
    }

    @Override
    public void loadRepositoryDetails(GitRepositoryBasicModel repository) {
        Log.i(TAG, "loadRepositoryDetails() repository.name = " + repository.getName());

        if (view != null) {
            view.goToGitRepositoryDetail(repository, ExtraTags.EXTRA_GIT_REPOSITORY_BASIC);
        }
    }

    @Override
    public void onSearchFieldChanges(String query) {
        Log.i(TAG, "onSearchFieldChanges(query= " + query + ")");

        if (repositoryItems == null) {
            // Important checking this value !!
            Log.w(TAG, "onSearchFieldChanges() called when repository items are not ready. " +
                    "It is because the search edit text calls onTextChanged() when it gets its last text " +
                    "=> User just rotated their pone");
        } else {
            // Set all control values as default
            allPagesRetrieved = false;
            loadingPage = false;
            currentPage = 0;
            repositoryLastSeenId = 0;

            // Dispose the current data subscription because we do not want this anymore, then we will make a new one
            if (getDataSubscription != null && !getDataSubscription.isDisposed()) {
                getDataSubscription.dispose();
            }

            // Clean current repository items on both the recyclerview and presenter
            if (view != null) {
                view.removeAllRepositories();
            }
            repositoryItems.clear();

            // Check if query to search
            if (query != null && !query.isEmpty()) {
                searchingReposByName = true;
                searchQuery = query;
            } else {
                searchingReposByName = false;
                searchQuery = "";
            }

            // Search new page about whatever
            getNextGitRepositoriesPage();
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
    public void unsubscribe() {
        Log.i(TAG, "rxJavaUnsubscribe() called");
        if (getDataSubscription != null && !getDataSubscription.isDisposed()) {
            getDataSubscription.dispose();
        }

    }


    /*
     * NEW METHODS
     */
    @Override
    public void subscribe(GitRepositoriesMVP.View view) {
        Log.i(TAG, "subscribe(view) called. loadPublicRepositories() must be called from the view");
        this.view = view;
        this.repositoryItems = new ArrayList<>();
    }

    @Override
    public void subscribeAndRestoreState(GitRepositoriesMVP.View view, @NonNull GitRepositoriesMVP.State state) {
        Log.i(TAG, "subscribeAndRestoreState(view). Last data will be shown");
        this.view = view;

        // There are retrieved items, get them from the state
        repositoryItems = state.getRepositoryItems();
        repositoryPosition = state.getRepositoryPosition();
        currentPage = state.getCurrentPage();
        repositoryLastSeenId = state.getRepositoryLastSeenId();
        allPagesRetrieved = state.getAllPagesRetrieved();
        searchingReposByName = state.getSearchingReposByName();
        searchQuery = state.getSearchQuery();

        //  Set items on the view
        if (view != null) {
            Log.i(TAG, "setView(view) called with state -02");
            view.setRepositoryItems(repositoryItems);
            view.setRepositoryPosition(repositoryPosition);
        }
    }

    // Once the state is requested, generate a new immutable state instance.
    @Override
    public GitRepositoriesMVP.State getState() {
        return new GitRepositoriesState(repositoryItems, repositoryPosition, currentPage,
                repositoryLastSeenId, allPagesRetrieved, searchingReposByName, searchQuery);
    }

    // Called by the view when the tab position changes.
    @Override
    public void onRepositoryPositionChange(int position) {
        repositoryPosition = position;
    }

    /*
     * NEW METHODS
     */


    private void getNextGitRepositoriesPage() {
        Log.i(TAG, "getNextGitRepositoriesPage() called");

        if (!allPagesRetrieved) {
            if (!loadingPage) {
                Log.i(TAG, "There is not a loading page process. Let's to load the next page!");
                loadingPage = true;

                if (view != null) {
                    view.showProgress();
                }

                Observable<GitRepositoryBasicModel> observable;
                if (searchingReposByName) {
                    observable = model.getGitPublicRepositoriesByName(searchQuery, ++currentPage);
                } else {
                    observable = model.getGitPublicRepositories(repositoryLastSeenId);
                }

                getDataSubscription = observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .switchIfEmpty(new Observable<GitRepositoryBasicModel>() {
                            @Override
                            protected void subscribeActual(Observer<? super GitRepositoryBasicModel> observer) {
                                Log.e(TAG, "No more pages to retrieve! The previous call was the last one.");
                                if (view != null) {
                                    view.hideProgress();
                                    view.showAllPagesRetrieved();
                                }
                                allPagesRetrieved = true;
                                loadingPage = false;
                            }
                        })
                        .subscribeWith(new DisposableObserver<GitRepositoryBasicModel>() {
                            @Override
                            public void onNext(GitRepositoryBasicModel repository) {
                                Log.i(TAG, "Git repository fetched: " + repository.getName());

                                repositoryItems.add(repository);

                                if (view != null) {
                                    repositoryLastSeenId = repository.getId();
                                    view.addRepositoryItem(repository);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getMessage(), e);
                                if (view != null) {
                                    view.removeAllRepositories();
                                    view.hideProgress();
                                    view.showPublicRepositoriesNotFound();
                                    allPagesRetrieved = true;
                                    loadingPage = false;
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "All repositories fetched from current call");
                                if (view != null) {
                                    view.hideProgress();
                                    view.showRepositoryPageFetched();
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
}
