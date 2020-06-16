package es.marcmauri.gitdroid.github.gitrepositories;

import android.util.Log;

import java.util.List;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.FoundRepositoriesApi;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryItemApi;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoriesRepositoryFromGithub implements GitRepositoriesRepository {

    private static final String TAG = GitRepositoriesRepositoryFromGithub.class.getName();
    private GitHubApiService gitHubApiService;

    public GitRepositoriesRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    @Override
    public Observable<RepositoryApi> getGitPublicRepositories(final long idLastRepoSeen) {
        Log.i(TAG, "Called getGitRepositoriesFromPublic(idLastRepoSeen= " + idLastRepoSeen + ")");

        Observable<List<RepositoryApi>> allReposObservable =
                gitHubApiService.getPublicRepositories(idLastRepoSeen);

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
                        Log.i(TAG, "Repository + " + repositoryApi.getName() + " obtained from All Public");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Repositories obtained since ID " + idLastRepoSeen);
                    }
                });
    }

    @Override
    public Observable<RepositoryItemApi> getGitPublicRepositoriesByName(final String query, final int page, final int reposPerPage) {
        Log.i(TAG, "Called getGitPublicRepositoriesByName(" +
                "query= " + query + ", page= " + page + ", reposPerPage= " + reposPerPage + ")");

        Observable<FoundRepositoriesApi> reposByNameObservable =
                gitHubApiService.getPublicRepositoriesByName(query, page, reposPerPage);

        return reposByNameObservable
                .concatMap(new Function<FoundRepositoriesApi, Observable<RepositoryItemApi>>() {
                    @Override
                    public Observable<RepositoryItemApi> apply(FoundRepositoriesApi foundRepositoriesApi) {
                        return Observable.fromIterable(foundRepositoriesApi.getItems());
                    }
                })
                .doOnNext(new Consumer<RepositoryItemApi>() {
                    @Override
                    public void accept(RepositoryItemApi repositoryItemApi) {
                        Log.i(TAG, "Repository + " + repositoryItemApi.getName() + " obtained from All Public by name");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Repositories obtained from query " + query + ". " +
                                "Page= " + page + ". Repos per page= " + reposPerPage);
                    }
                });
    }
}
