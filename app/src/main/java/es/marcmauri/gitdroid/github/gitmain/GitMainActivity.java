package es.marcmauri.gitdroid.github.gitmain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesActivity;
import es.marcmauri.gitdroid.root.App;

public class GitMainActivity extends AppCompatActivity implements GitMainMVP.View {

    private final String TAG = GitMainActivity.class.getName();

    private ViewGroup rootView;
    private Button btnGetRepos;

    @Inject
    GitMainMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_main);

        Log.i(TAG, "onCreate()");

        ((App) getApplication()).getComponent().inject(this);

        // Vinculamos la Vista al Activity
        bindUI();

        // Dotamos de comportamiento a los inputs del usuario
        behaviorUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

        presenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    public void goToGitRepositories() {
        Intent intentToRepos = new Intent(this, GitRepositoriesActivity.class);
        startActivity(intentToRepos);
    }

    private void bindUI() {
        rootView = findViewById(R.id.activity_search_user_root_view);
        btnGetRepos = findViewById(R.id.btn_get_repositories_from_user);
    }

    private void behaviorUI() {
        btnGetRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadPublicRepositories();
            }
        });
    }
}
