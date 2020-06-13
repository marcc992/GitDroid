package es.marcmauri.gitdroid.github.gitrepositorydetail;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;

public interface GitRepositoryDetailRepository {

    Observable<RepositoryApi> getGitRepositoryDetail(final String user, final String repositoryName);
}
