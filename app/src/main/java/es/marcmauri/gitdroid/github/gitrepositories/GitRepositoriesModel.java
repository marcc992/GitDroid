package es.marcmauri.gitdroid.github.gitrepositories;

import android.util.Log;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.http.apimodel.github.Owner;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryItemApi;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoriesModel implements GitRepositoriesMVP.Model {

    private static final String TAG = GitRepositoriesModel.class.getName();
    private static final int REPOS_PER_PAGE = 20;

    private GitRepositoriesRepository repository;

    public GitRepositoriesModel(GitRepositoriesRepository repository) {
        this.repository = repository;
    }

    private String getOwnerName(Owner owner) {
        if (owner != null && owner.getLogin() != null) {
            return owner.getLogin();
        } else {
            return "";
        }
    }

    @Override
    public Observable<GitRepositoryBasicModel> getGitPublicRepositories(final long idLastRepoSeen) {
        Log.i(TAG, "getGitRepositoriesFromPublic(idLastRepoSeen= " + idLastRepoSeen + ")");
        return repository.getGitPublicRepositories(idLastRepoSeen)
                .flatMap(new Function<RepositoryApi, Observable<GitRepositoryBasicModel>>() {
                    @Override
                    public Observable<GitRepositoryBasicModel> apply(RepositoryApi repositoryApi) throws Exception {
                        return Observable.just(
                                new GitRepositoryBasicModel(
                                        repositoryApi.getId(),
                                        repositoryApi.getName(),
                                        repositoryApi.getFullName(),
                                        repositoryApi.getDescription(),
                                        getOwnerName(repositoryApi.getOwner())));
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    public Observable<GitRepositoryBasicModel> getGitPublicRepositoriesByName(String query, int page) {
        Log.i(TAG, "getGitPublicRepositoriesByName(query= " + query + ", page= " + page + ")");
        return repository.getGitPublicRepositoriesByName(query, page, REPOS_PER_PAGE)
                .flatMap(new Function<RepositoryItemApi, Observable<GitRepositoryBasicModel>>() {
                    @Override
                    public Observable<GitRepositoryBasicModel> apply(RepositoryItemApi repositoryItemApi) {
                        return Observable.just(
                                new GitRepositoryBasicModel(
                                        repositoryItemApi.getId(),
                                        repositoryItemApi.getName(),
                                        repositoryItemApi.getFullName(),
                                        repositoryItemApi.getDescription(),
                                        getOwnerName(repositoryItemApi.getOwner())));
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
