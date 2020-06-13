package es.marcmauri.gitdroid.github.repositorylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.root.App;

public class GitRepositoriesActivity extends AppCompatActivity implements GitRepositoriesMVP.View {

    private final String TAG = GitRepositoriesActivity.class.getName();

    @Inject
    GitRepositoriesMVP.Presenter presenter;

    private ConstraintLayout rootView;
    private ConstraintLayout gitUserDetails;
    private TextView tvUsername;
    private ImageView ivUserAvatar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private GitRepositoryListAdapter repositoryListAdapter;
    private List<GitRepositoryBasicModel> repositoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        setContentView(R.layout.activity_git_list_repos);

        ((App) getApplication()).getComponent().inject(this);

        // Vinculamos la Vista al Activity
        bindUI();

        // Configuramos el recyclerview con el adapter
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

        presenter.setView(this);
        presenter.loadUserDetails();
        presenter.loadRepositories();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

        presenter.rxJavaUnsubscribe();
        removeAllRepositories();
    }

    @Override
    public void setAvatar(String url) {
        Log.i(TAG, "setAvatar() url = " + url);
        Picasso.get().load(url).fit().centerCrop().into(ivUserAvatar);
    }

    @Override
    public void setUsername(String username) {
        Log.i(TAG, "setUsername() username = " + username);
        tvUsername.setText(username);
    }

    @Override
    public void addRepository(GitRepositoryBasicModel repository) {
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
    public Bundle getExtras() {
        Log.i(TAG, "getExtras()");
        if (getIntent() != null) {
            Log.i(TAG, "getIntent() is not null, we try to return getIntent().getExtras()");
            return getIntent().getExtras();
        } else {
            Log.e(TAG, "getIntent() returns null");
            return null;
        }
    }

    @Override
    public void navigateToNextActivity(Intent i) {
        Log.i(TAG, "navigateToNextActivity()");
        startActivity(i);
    }

    private void bindUI() {
        Log.i(TAG, "bindUI()");
        rootView = findViewById(R.id.activity_git_repositories_root_view);
        gitUserDetails = findViewById(R.id.layout_user_details);
        tvUsername = findViewById(R.id.tv_username);
        ivUserAvatar = findViewById(R.id.iv_user_avatar);
        recyclerView = findViewById(R.id.recyclerView_git_repositories);
        progressBar = findViewById(R.id.progressBar_search_repositories);

    }

    private void setRecyclerView() {
        Log.i(TAG, "setRecyclerView()");
        repositoryListAdapter = new GitRepositoryListAdapter(repositoryList);

        recyclerView.setAdapter(repositoryListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
