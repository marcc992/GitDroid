package es.marcmauri.gitdroid.github.gitrepositorydetail;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import io.reactivex.Observable;

public interface GitRepositoryDetailMVP {

    interface View {
        void setUserName(String userName);

        void setUserAvatar(String imageUrl);

        void setRepositoryName(String text);

        void setWebViewContent(String url);

        void showProgress();

        void hideProgress();

        void showUserError();

        void showRepositoryError();

        GitRepositoryBasicModel getRepositoryBasicModelFromExtras(String key);
    }

    interface Presenter {
        void loadOwnerDetails();

        void loadRepositoryDetails();

        void rxJavaUnsubscribe();

        void setView(GitRepositoryDetailMVP.View view);
    }

    interface Model {
        Observable<GitUserModel> getGitUserDetails(String username);

        Observable<GitRepositoryDetailedModel> getGitRepositoryDetail(String owner, String repositoryName);
    }
}
