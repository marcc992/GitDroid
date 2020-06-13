package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.content.Intent;
import android.os.Bundle;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import io.reactivex.Observable;

public interface GitRepositoryDetailMVP {

    interface View {
        void setName(String text);
        void setFullName(String text);
        void setDescription(String text);

        void setWebViewContent(String url);

        void showProgress();
        void hideProgress();
        void showSnackBar(String message);

        Bundle getExtras();
        void shareGitLink(Intent i);
    }

    interface Presenter {
        void loadRepositoryDetails();

        void rxJavaUnsubscribe();

        void setView(GitRepositoryDetailMVP.View view);
    }

    interface Model {
        Observable<GitRepositoryDetailedModel> getGitRepositoryDetail(String user, String repositoryName);
    }
}
