package es.marcmauri.gitdroid.github.gitrepositories;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;

public interface GitRepositoriesRepository {

    Observable<RepositoryApi> getGitPublicRepositories(final long idLastRepoSeen);
}
