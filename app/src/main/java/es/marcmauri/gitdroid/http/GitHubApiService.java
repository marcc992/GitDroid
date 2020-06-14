package es.marcmauri.gitdroid.http;

import java.util.List;

import es.marcmauri.gitdroid.http.apimodel.github.FoundRepositoriesApi;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApiService {

    @GET("users/{username}")
    Observable<UserApi> getUserInfoObservable(
            @Path("username") String username
    );

    @GET("repositories")
    Observable<List<RepositoryApi>> getPublicRepositories(
            @Query("since") long idLastRepoSeen
    );

    @GET("search/repositories")
    Observable<FoundRepositoriesApi> getPublicRepositoriesByName(
            @Query("q") String query,
            @Query("page") int page,
            @Query("per_page") int reposPerPage
    );

    @GET("repos/{user}/{repositoryName}")
    Observable<RepositoryApi> getRepositoryDetail(
            @Path("user") String user,
            @Path("repositoryName") String repositoryName
    );
}
