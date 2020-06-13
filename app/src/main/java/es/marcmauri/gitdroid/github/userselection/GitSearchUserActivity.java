package es.marcmauri.gitdroid.github.userselection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.root.App;

public class GitSearchUserActivity extends AppCompatActivity implements GitSearchUserMVP.View {

    private final String TAG = GitSearchUserActivity.class.getName();

    private ViewGroup rootView;
    private EditText etUsername;
    private Button btnGetRepos;
    private ProgressBar progressBar;

    @Inject
    GitSearchUserMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_search_user);

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

        presenter.rxJavaUnsubscribe();
    }

    @Override
    public void showProgress() {
        etUsername.setEnabled(false);
        btnGetRepos.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        etUsername.setEnabled(true);
        btnGetRepos.setEnabled(true);
    }

    @Override
    public void setUserNotExist(String message) {
        etUsername.setError(message);
    }

    @Override
    public void setUserExists() {
        etUsername.setError(null);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToNextActivity(Intent i) {
        startActivity(i);
    }

    private void bindUI() {
        rootView = findViewById(R.id.activity_search_user_root_view);
        etUsername = findViewById(R.id.et_username);
        btnGetRepos = findViewById(R.id.btn_get_repositories_from_user);
        progressBar = findViewById(R.id.progressBar_search_user);
    }

    private void behaviorUI() {
        btnGetRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkUserAndGoToRepos(etUsername.getText().toString());
            }
        });
    }
}
