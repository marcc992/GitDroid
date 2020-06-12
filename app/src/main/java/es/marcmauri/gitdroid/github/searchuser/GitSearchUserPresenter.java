package es.marcmauri.gitdroid.github.searchuser;

import android.util.Log;

import androidx.annotation.Nullable;

import es.marcmauri.gitdroid.github.GitUserViewModel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

            model.getGitUserDetails(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GitUserViewModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GitUserViewModel gitUserViewModel) {
                            Log.i(TAG, "El usuario " + gitUserViewModel.getUsername() + " encontrado!!");
                            if (view != null) {
                                view.showSnackBar("Usuario " + gitUserViewModel.getUsername() + " encontrado!");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "El usuario no se ha encontrado!!");
                            if (view != null) {
                                view.setUserNotExist("Este usuario no existe");
                                view.hideProgress();
                            }

                            Log.e(TAG, e.getMessage(), e);
                        }

                        @Override
                        public void onComplete() {
                            if (view != null) {
                                view.hideProgress();
                            }
                        }
                    });
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
