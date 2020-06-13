package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.util.Log;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoryDetailRepositoryFromGithub implements GitRepositoryDetailRepository {

    private static final String TAG = GitRepositoryDetailRepositoryFromGithub.class.getName();
    private GitHubApiService gitHubApiService;

    public GitRepositoryDetailRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
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
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Repository " + repositoryName + " obtained from User " + user);
                    }
                });
    }
}
