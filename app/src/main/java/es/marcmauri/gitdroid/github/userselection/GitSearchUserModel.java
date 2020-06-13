package es.marcmauri.gitdroid.github.userselection;

import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class GitSearchUserModel implements GitSearchUserMVP.Model {

    private static final String TAG = GitSearchUserModel.class.getName();

    private GitSearchUserRepository githubRepository;

    public GitSearchUserModel(GitSearchUserRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    @Override
    public Observable<GitUserModel> getGitUserDetails(String username) {
        return githubRepository.getGitUserDetails(username)
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
