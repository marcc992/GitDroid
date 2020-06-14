package es.marcmauri.gitdroid.github.gitrepositories;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;

public interface GitRepositoriesRepository {

    Observable<RepositoryApi> getGitRepositoriesFromUser(final String username, final int page);

    Observable<RepositoryApi> getGitRepositoriesFromOrganization(final String organization, final int page);
}
