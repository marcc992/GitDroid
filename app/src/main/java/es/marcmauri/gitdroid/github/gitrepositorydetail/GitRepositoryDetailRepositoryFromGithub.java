package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.util.Log;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoryDetailRepositoryFromGithub implements GitRepositoryDetailRepository {

    private static final String TAG = GitRepositoryDetailRepositoryFromGithub.class.getName();
    private GitHubApiService gitHubApiService;

    public GitRepositoryDetailRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }


    @Override
    public Observable<UserApi> getGitUserDetails(final String username) {
        Log.i(TAG, "Called getGitUserDetails(username=" + username + ")");

        Observable<UserApi> userApiObservable =
                gitHubApiService.getUserInfoObservable(username);

        return userApiObservable
                .concatMap(new Function<UserApi, Observable<UserApi>>() {
                    @Override
                    public Observable<UserApi> apply(UserApi userApi) {
                        return Observable.just(userApi);
                    }
                })
                .doOnNext(new Consumer<UserApi>() {
                    @Override
                    public void accept(UserApi userApi) {
                        Log.i(TAG, "User + " + userApi.getName() + " obtained from GitHub");
                    }
                });
    }

    @Override
    public Observable<RepositoryApi> getGitRepositoryDetail(final String user, final String repositoryName) {
        Log.i(TAG, "Called getGitRepositoryDetail(user= " + user + ", repository.name= " + repositoryName + ")");

        Observable<RepositoryApi> repositoryApiObservable =
                gitHubApiService.getRepositoryDetail(user, repositoryName);

        return repositoryApiObservable
                .concatMap(new Function<RepositoryApi, Observable<RepositoryApi>>() {
                    @Override
                    public Observable<RepositoryApi> apply(RepositoryApi repositoryApi) {
                        return Observable.just(repositoryApi);
                    }
                })
                .doOnNext(new Consumer<RepositoryApi>() {
                    @Override
                    public void accept(RepositoryApi repositoryApi) {
                        Log.i(TAG, "Repository + " + repositoryApi.getName() + " obtained from User " + user);
                    }
                });
    }
}
