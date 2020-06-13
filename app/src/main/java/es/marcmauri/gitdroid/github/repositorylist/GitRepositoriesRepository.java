package es.marcmauri.gitdroid.github.repositorylist;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;

public interface GitRepositoriesRepository {

    Observable<RepositoryApi> getGitRepositoriesFromUser(String username);

    Observable<RepositoryApi> getGitRepositoriesFromOrganization(String organization);
}
