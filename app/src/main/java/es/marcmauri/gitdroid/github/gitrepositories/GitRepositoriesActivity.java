package es.marcmauri.gitdroid.github.gitrepositories;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailActivity;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.root.App;

public class GitRepositoriesActivity extends AppCompatActivity implements GitRepositoriesMVP.View {

    private final String TAG = GitRepositoriesActivity.class.getName();

    private final String BUNDLE_TAG_REPOSITORY_ITEMS = "bundle.repository.items";
    private final String BUNDLE_TAG_REPOSITORY_POSITION = "bundle.repository.position";
    private final String BUNDLE_TAG_CURRENT_PAGE = "bundle.current.page";
    private final String BUNDLE_TAG_REPO_LAST_SEEN_ID = "bundle.repository.last.seen.id";
    private final String BUNDLE_TAG_ALL_PAGES_RETRIEVED = "bundle.all.pages.retrieved";
    private final String BUNDLE_TAG_SEARCHING_REPOS_BY_NAME = "bundle.searching.repositories.by.name";
    private final String BUNDLE_TAG_REPOSITORY_SEARCH_QUERY = "bundle.search.query";

    @Inject
    GitRepositoriesMVP.Presenter presenter;

    private ConstraintLayout rootView;
    private EditText etSearchQuery;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private GitRepositoryListAdapter repositoryListAdapter;
    private List<GitRepositoryBasicModel> repositoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        setContentView(R.layout.activity_git_repositories);

        ((App) getApplication()).getComponent().inject(this);

        // Vinculamos la Vista al Activity
        bindUI();

        // Dotamos comportamiento a la vista
        behaviorUI();

        // Configuramos el recyclerview con el adapter
        setRecyclerView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i(TAG, "onPostCreate()");

        // Pass state when subscribing; it can be null.
        GitRepositoriesMVP.State stateFromBundle = readFromBundle(savedInstanceState);
        if (stateFromBundle != null) {
            Log.i(TAG, "onPostCreate() -> stateFromBundle is VALID!");
            presenter.subscribeAndRestoreState(this, stateFromBundle);
        } else {
            Log.i(TAG, "onPostCreate() -> stateFromBundle is NULL!");
            presenter.subscribe(this);
            presenter.loadPublicRepositories();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");

        writeToBundle(outState, presenter.getState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

        //presenter.setView(this);
        //presenter.loadPublicRepositories();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

        //presenter.unsubscribe();
        //removeAllRepositories();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        presenter.unsubscribe();
    }

    private void writeToBundle(@NonNull Bundle outState, GitRepositoriesMVP.State state) {
        Log.i(TAG, "writeToBundle() called");

        outState.putParcelableArrayList(BUNDLE_TAG_REPOSITORY_ITEMS, state.getRepositoryItems());
        outState.putInt(BUNDLE_TAG_REPOSITORY_POSITION, state.getRepositoryPosition());
        outState.putInt(BUNDLE_TAG_CURRENT_PAGE, state.getCurrentPage());
        outState.putLong(BUNDLE_TAG_REPO_LAST_SEEN_ID, state.getRepositoryLastSeenId());
        outState.putBoolean(BUNDLE_TAG_ALL_PAGES_RETRIEVED, state.getAllPagesRetrieved());
        outState.putBoolean(BUNDLE_TAG_SEARCHING_REPOS_BY_NAME, state.getSearchingReposByName());
        outState.putString(BUNDLE_TAG_REPOSITORY_SEARCH_QUERY, state.getSearchQuery());
    }

    private GitRepositoriesMVP.State readFromBundle(@Nullable Bundle bundle) {
        if (bundle == null) {
            Log.i(TAG, "readFromBundle() bundle is NULL");
            return null;
        } else {
            Log.i(TAG, "readFromBundle() bundle has data");

            try {
                ArrayList<GitRepositoryBasicModel> lastRepoItems = bundle.getParcelableArrayList(BUNDLE_TAG_REPOSITORY_ITEMS);

                return new GitRepositoriesState(
                        lastRepoItems,
                        bundle.getInt(BUNDLE_TAG_REPOSITORY_POSITION),
                        bundle.getInt(BUNDLE_TAG_CURRENT_PAGE),
                        bundle.getLong(BUNDLE_TAG_REPO_LAST_SEEN_ID),
                        bundle.getBoolean(BUNDLE_TAG_ALL_PAGES_RETRIEVED),
                        bundle.getBoolean(BUNDLE_TAG_SEARCHING_REPOS_BY_NAME),
                        bundle.getString(BUNDLE_TAG_REPOSITORY_SEARCH_QUERY));
            } catch (Exception e) {
                Log.e(TAG, "One or more Status values from bundle are null or has some problem.");
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    public void setRepositoryItems(List<GitRepositoryBasicModel> repositories) {
        Log.i(TAG, "setRepositoryItems() #repos: " + repositories.size());
        repositoryList.clear();
        repositoryList.addAll(repositories);
        repositoryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addRepositoryItem(GitRepositoryBasicModel repository) {
        Log.i(TAG, "addRepository() repository.name = " + repository.getName());
        repositoryList.add(repository);
        repositoryListAdapter.notifyItemChanged(repositoryList.size() - 1);
    }

    @Override
    public void removeAllRepositories() {
        Log.i(TAG, "removeAllRepositories()");
        repositoryList.clear();
        repositoryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRepositoryPosition(int position) {
        Log.i(TAG, "setRepositoryPosition(position= " + position + ")");
        if (recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    public void showProgress() {
        Log.i(TAG, "showProgress()");
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        Log.i(TAG, "hideProgress()");
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSnackBar(String message) {
        Log.i(TAG, "showSnackBar() message = " + message);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void goToGitRepositoryDetail(GitRepositoryBasicModel repo, String tagKeyToPutInExtras) {
        Log.i(TAG, "goToGitRepositoryDetail(repo, extrasKey)");
        Intent intentToRepoDetail = new Intent(this, GitRepositoryDetailActivity.class);
        intentToRepoDetail.putExtra(tagKeyToPutInExtras, repo);
        startActivity(intentToRepoDetail);
    }

    private void bindUI() {
        Log.i(TAG, "bindUI()");
        rootView = findViewById(R.id.activity_git_repositories_root_view);
        etSearchQuery = findViewById(R.id.et_search_query);
        recyclerView = findViewById(R.id.recyclerView_git_repositories);
        progressBar = findViewById(R.id.progressBar_git_repositories);

    }

    private void behaviorUI() {
        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() start= " + start + ", before= " + before + ", count= " + count);
                if (before != count) {
                    presenter.onSearchFieldChanges(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setRecyclerView() {
        Log.i(TAG, "setRecyclerView()");

        repositoryListAdapter = new GitRepositoryListAdapter(repositoryList, new GitRepositoryListAdapter.OnItemClickListener() {
            @Override
            public void onRepositoryClick(GitRepositoryBasicModel repository) {
                presenter.loadRepositoryDetails(repository);
            }
        });

        recyclerView.setAdapter(repositoryListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onRecyclerViewScrolled(
                        layoutManager.getChildCount(), layoutManager.getItemCount(),
                        layoutManager.findFirstVisibleItemPosition(), dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Presenter notification when scroll is not moving
                    presenter.onRepositoryPositionChange(layoutManager.findFirstVisibleItemPosition());
                }
            }
        });
    }
}
