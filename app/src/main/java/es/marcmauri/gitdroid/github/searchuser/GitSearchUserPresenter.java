package es.marcmauri.gitdroid.github.searchuser;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import io.reactivex.disposables.Disposable;

public class GitSearchUserPresenter implements GitSearchUserMVP.Presenter {

    private static final String TAG = GitSearchUserPresenter.class.getName();

    @Nullable
    private GitSearchUserMVP.View view;
    private GitSearchUserMVP.Model model;

    private Disposable getDataSubscription = null;

    public GitSearchUserPresenter(GitSearchUserMVP.Model model) {
        this.model = model;
    }


    @Override
    public void checkIfUserExists(String user) {
        Log.i(TAG, "Github user to found: " + user);

        if (user == null || user.isEmpty()) {
            if (view != null) {
                view.setUserNotExist("TODO: El nombre de usuario no es valido!");
            }
        } else {
            if (view != null) {
                view.showProgress();
            }

            new DummyAsyncTask().execute();
        }

    }

    class DummyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
                //model.getGitUserDetails(user);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (view != null) {
                view.hideProgress();
            }
        }
    }

    @Override
    public void rxJavaUnsubscribe() {
        Log.i(TAG, "rxJavaUnsubscribe() called");
        if (getDataSubscription != null && !getDataSubscription.isDisposed()) {
            getDataSubscription.dispose();
        }
    }

    @Override
    public void setView(GitSearchUserMVP.View view) {
        Log.i(TAG, "setView(view) called");
        this.view = view;
    }
}
