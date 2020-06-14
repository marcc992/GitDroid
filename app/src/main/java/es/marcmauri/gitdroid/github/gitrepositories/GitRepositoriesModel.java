package es.marcmauri.gitdroid.github.gitrepositories;

import android.util.Log;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoriesModel implements GitRepositoriesMVP.Model, Function<RepositoryApi, ObservableSource<? extends GitRepositoryBasicModel>> {

    private static final String TAG = GitRepositoriesModel.class.getName();

    private GitRepositoriesRepository repository;

    public GitRepositoriesModel(GitRepositoriesRepository repository) {
        this.repository = repository;
    }

    private Observable<GitRepositoryBasicModel> getGitRepositoriesFromUser(String username, int page) {
        return repository.getGitRepositoriesFromUser(username, page).flatMap(this);
    }

    private Observable<GitRepositoryBasicModel> getGitRepositoriesFromOrganization(String org, int page) {
        return repository.getGitRepositoriesFromOrganization(org, page).flatMap(this);
    }

    @Override
    public Observable<GitRepositoryBasicModel> apply(RepositoryApi repositoryApi) {
        GitRepositoryBasicModel gitRepositoryBasicModel =
                new GitRepositoryBasicModel(
                        repositoryApi.getId(),
                        repositoryApi.getName(),
                        repositoryApi.getFullName(),
                        repositoryApi.getDescription(),
                        repositoryApi.getCreatedAt(),
                        repositoryApi.getUpdatedAt());

        return Observable.just(gitRepositoryBasicModel);
    }

    @Override
    public Observable<GitRepositoryBasicModel> getGitRepositories(final String user, final int page) {
        Log.e(TAG, "getGitRepositories(user= " + user + ", page= " + page + ")");
        return getGitRepositoriesFromUser(user, page)
                .onErrorResumeNext(new Function<Throwable, Observable<GitRepositoryBasicModel>>() {
                    @Override
                    public Observable<GitRepositoryBasicModel> apply(Throwable throwable) {
                        Log.w(TAG, "Repositories from Username not found, " +
                                "then we try to get repositories from Organization. " +
                                "User = " + user + ", Page = " + page);
                        return getGitRepositoriesFromOrganization(user, page);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                });
    }
}
