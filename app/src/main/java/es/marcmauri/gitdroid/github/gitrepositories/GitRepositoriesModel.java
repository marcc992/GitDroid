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

    @Override
    public Observable<GitRepositoryBasicModel> apply(RepositoryApi repositoryApi) {
        String owner = "";
        if (repositoryApi.getOwner() != null) {
            String ownerApi = repositoryApi.getOwner().getLogin();
            if (ownerApi != null) {
                Log.i(TAG, "Owner username fetched from Repository details. Owner user is " + ownerApi);
                owner = ownerApi;
            }
        }

        GitRepositoryBasicModel gitRepositoryBasicModel =
                new GitRepositoryBasicModel(
                        repositoryApi.getId(),
                        repositoryApi.getName(),
                        repositoryApi.getFullName(),
                        repositoryApi.getDescription(),
                        owner);

        return Observable.just(gitRepositoryBasicModel);
    }

    private Observable<GitRepositoryBasicModel> getGitRepositoriesFromAllGitPublics(long idLastRepoSeen) {
        return repository.getGitPublicRepositories(idLastRepoSeen).flatMap(this);
    }

    @Override
    public Observable<GitRepositoryBasicModel> getGitPublicRepositories(final long idLastRepoSeen) {
        Log.e(TAG, "getGitRepositoriesFromPublic(idLastRepoSeen= " + idLastRepoSeen + ")");
        return getGitRepositoriesFromAllGitPublics(idLastRepoSeen)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                });
    }
}
