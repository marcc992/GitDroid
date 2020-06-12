package es.marcmauri.gitdroid.github.searchuser;

import es.marcmauri.gitdroid.github.GitUserViewModel;
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
    public Observable<GitUserViewModel> getGitUserDetails(String username) {
        return githubRepository.getGitUserDetails(username)
                .flatMap(new Function<UserApi, Observable<GitUserViewModel>>() {
                    @Override
                    public Observable<GitUserViewModel> apply(UserApi userApi) {
                        GitUserViewModel gitUser = new GitUserViewModel(
                                userApi.getId(),
                                userApi.getLogin(),
                                userApi.getAvatarUrl());

                        return Observable.just(gitUser);
                    }
                });
    }
}
