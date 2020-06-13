package es.marcmauri.gitdroid.github.gituserselection;

import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class GitUserSelectionModel implements GitUserSelectionMVP.Model {

    private static final String TAG = GitUserSelectionModel.class.getName();

    private GitUserSelectionRepository repository;

    public GitUserSelectionModel(GitUserSelectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<GitUserModel> getGitUserDetails(String username) {
        return repository.getGitUserDetails(username)
                .flatMap(new Function<UserApi, Observable<GitUserModel>>() {
                    @Override
                    public Observable<GitUserModel> apply(UserApi userApi) {
                        GitUserModel gitUserModel = new GitUserModel(
                                userApi.getId(),
                                userApi.getLogin(),
                                userApi.getAvatarUrl());

                        return Observable.just(gitUserModel);
                    }
                });
    }
}
