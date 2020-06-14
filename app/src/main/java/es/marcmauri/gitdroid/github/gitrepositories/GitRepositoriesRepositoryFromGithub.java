package es.marcmauri.gitdroid.github.gitrepositories;

import android.util.Log;

import java.util.List;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoriesRepositoryFromGithub implements GitRepositoriesRepository {

    private static final String TAG = GitRepositoriesRepositoryFromGithub.class.getName();
    private static final int REPOS_PER_PAGE = 10;
    private GitHubApiService gitHubApiService;

    public GitRepositoriesRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    @Override
    public Observable<RepositoryApi> getGitRepositoriesFromUser(final String username, final int page) {
        Log.i(TAG, "Called getGitRepositoriesFromUser(username=" + username + ", page=" + page + ")");

        Observable<List<RepositoryApi>> allReposObservable =
                gitHubApiService.getRepositoriesFromUser(username, page, REPOS_PER_PAGE);

        return allReposObservable
                .concatMap(new Function<List<RepositoryApi>, Observable<RepositoryApi>>() {
                    @Override
                    public Observable<RepositoryApi> apply(List<RepositoryApi> repositoryApis) {
                        return Observable.fromIterable(repositoryApis);
                    }
                })
                .doOnNext(new Consumer<RepositoryApi>() {
                    @Override
                    public void accept(RepositoryApi repositoryApi) {
                        Log.i(TAG, "Repository + " + repositoryApi.getName() + " obtained from User");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Repositories obtained from User " + username);
                    }
                });
    }

    @Override
    public Observable<RepositoryApi> getGitRepositoriesFromOrganization(final String organization, final int page) {
        Log.i(TAG, "Called getGitRepositoriesFromOrganization(organization=" + organization  + ", page=" + page + ")");

        Observable<List<RepositoryApi>> allReposObservable =
                gitHubApiService.getRepositoriesFromOrganization(organization, page, REPOS_PER_PAGE);

        return allReposObservable
                .concatMap(new Function<List<RepositoryApi>, Observable<RepositoryApi>>() {
                    @Override
                    public Observable<RepositoryApi> apply(List<RepositoryApi> repositoryApis) {
                        return Observable.fromIterable(repositoryApis);
                    }
                })
                .doOnNext(new Consumer<RepositoryApi>() {
                    @Override
                    public void accept(RepositoryApi repositoryApi) {
                        Log.i(TAG, "Repository + " + repositoryApi.getName() + " obtained from Organization");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Repositories obtained from Organization " + organization);
                    }
                });
    }
}
