package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.root.App;

public class GitRepositoryDetailActivity extends AppCompatActivity implements GitRepositoryDetailMVP.View {

    private static final String TAG = GitRepositoryDetailActivity.class.getName();

    @Inject
    GitRepositoryDetailMVP.Presenter presenter;

    private ConstraintLayout rootView;
    private TextView tvUserName;
    private ImageView ivUserAvatar;
    private TextView tvRepositoryName;
    private WebView webViewGitRepositoryInfo;
    private ProgressBar progressBar;

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
        presenter.recoverUserDetails();
        presenter.loadRepositoryDetails();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

        presenter.rxJavaUnsubscribe();
        webViewGitRepositoryInfo.clearCache(true);
    }

    @Override
    public void setUserName(String userName) {
        Log.i(TAG, "setUserName(username = " + userName + ")");
        tvUserName.setText(userName);
    }

    @Override
    public void setUserAvatar(String imageUrl) {
        Log.i(TAG, "setUserAvatar(imageUrl = " + imageUrl + ")");
        Picasso.get().load(imageUrl).fit().centerCrop().into(ivUserAvatar);
    }

    @Override
    public void setRepositoryName(String text) {
        Log.i(TAG, "setRepositoryName(text = " + text + ")");
        tvRepositoryName.setText(text);
    }

    @Override
    public void setWebViewContent(String url) {
        Log.i(TAG, "setWebViewContent(url = " + url + ")");
        webViewGitRepositoryInfo.loadUrl(url);
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
        Log.i(TAG, "showSnackBar() msg: " + message);
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

    private void bindUI() {
        rootView = findViewById(R.id.activity_git_repository_detail_root_view);
        tvUserName = findViewById(R.id.tv_username);
        ivUserAvatar = findViewById(R.id.iv_user_avatar);
        tvRepositoryName = findViewById(R.id.tv_repository_name);
        webViewGitRepositoryInfo = findViewById(R.id.webview_git_repository_info);
        progressBar = findViewById(R.id.progressBar_git_repository_detail);
    }
}
