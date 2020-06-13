package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.root.App;

public class GitRepositoryDetailActivity extends AppCompatActivity implements GitRepositoryDetailMVP.View {

    private static final String TAG = GitRepositoryDetailActivity.class.getName();

    @Inject
    GitRepositoryDetailMVP.Presenter presenter;

    private ConstraintLayout rootView;
    private TextView tvName;
    private TextView tvFullName;
    private TextView tvDescription;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        setContentView(R.layout.activity_git_repository_detail);

        ((App) getApplication()).getComponent().inject(this);

        // Vinculamos la Vista al Activity
        bindUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

        presenter.setView(this);
        presenter.loadRepositoryDetails();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

        presenter.rxJavaUnsubscribe();
    }

    @Override
    public void setName(String text) {
        Log.i(TAG, "setName() text = " + text);
        //todo tvName.setText(text);
    }

    @Override
    public void setFullName(String text) {
        Log.i(TAG, "setFullName() text = " + text);
        //todo tvFullName.setText(text);
        showSnackBar(text);
    }

    @Override
    public void setDescription(String text) {
        Log.i(TAG, "setDescription() text = " + text);
        //todo tvDescription.setText(text);
    }

    @Override
    public void setWebViewContent(String url) {
        //TODO
    }

    @Override
    public void showProgress() {
        //TODO
    }

    @Override
    public void hideProgress() {
        //TODO
    }

    @Override
    public void showSnackBar(String message) {
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
    public void shareGitLink(Intent i) {
        //TODO
    }

    private void bindUI() {
        //TODO
        rootView = findViewById(R.id.activity_git_repository_detail_root_view);
    }
}
