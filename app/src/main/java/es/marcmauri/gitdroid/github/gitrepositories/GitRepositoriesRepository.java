package es.marcmauri.gitdroid.github.gitrepositories;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryItemApi;
import io.reactivex.Observable;

public interface GitRepositoriesRepository {

    Observable<RepositoryApi> getGitPublicRepositories(final long idLastRepoSeen);

    Observable<RepositoryItemApi> getGitPublicRepositoriesByName(final String query, final int page, final int reposPerPage);
}
