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

    private GitRepositoriesRepository githubRepository;

    public GitRepositoriesModel(GitRepositoriesRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    private Observable<GitRepositoryBasicModel> getGitRepositoriesFromUser(String username) {
        return githubRepository.getGitRepositoriesFromUser(username).flatMap(this);
    }

    private Observable<GitRepositoryBasicModel> getGitRepositoriesFromOrganization(String org) {
        return githubRepository.getGitRepositoriesFromOrganization(org).flatMap(this);
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
    public Observable<GitRepositoryBasicModel> getGitRepositories(final String user) {
        return getGitRepositoriesFromUser(user)
                .onErrorResumeNext(new Function<Throwable, Observable<GitRepositoryBasicModel>>() {
                    @Override
                    public Observable<GitRepositoryBasicModel> apply(Throwable throwable) {
                        Log.w(TAG, "Repositories from Username not found, " +
                                "then we try to get repositories from Organization. User = " + user);
                        return getGitRepositoriesFromOrganization(user);
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
