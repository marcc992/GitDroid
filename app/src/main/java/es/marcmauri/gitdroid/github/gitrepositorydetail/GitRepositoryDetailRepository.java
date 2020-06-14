package es.marcmauri.gitdroid.github.gitrepositorydetail;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public interface GitRepositoryDetailRepository {

    Observable<UserApi> getGitUserDetails(String username);
    Observable<RepositoryApi> getGitRepositoryDetail(final String user, final String repositoryName);
}
