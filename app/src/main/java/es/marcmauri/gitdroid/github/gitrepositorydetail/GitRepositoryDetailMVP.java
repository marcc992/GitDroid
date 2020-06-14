package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.os.Bundle;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import io.reactivex.Observable;

public interface GitRepositoryDetailMVP {

    interface View {
        void setUserName(String userName);
        void setUserAvatar(String imageUrl);

        void setRepositoryName(String text);
        void setWebViewContent(String url);

        void showProgress();
        void hideProgress();
        void showSnackBar(String message);

        Bundle getExtras();
    }

    interface Presenter {
        void recoverUserDetails();

        void loadRepositoryDetails();

        void rxJavaUnsubscribe();

        void setView(GitRepositoryDetailMVP.View view);
    }

    interface Model {
        Observable<GitRepositoryDetailedModel> getGitRepositoryDetail(String user, String repositoryName);
    }
}
