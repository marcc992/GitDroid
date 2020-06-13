package es.marcmauri.gitdroid.http;

import java.util.List;

import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApiService {

    @GET("users/{username}")
    Observable<UserApi> getUserInfoObservable(
            //@Header("Accept") String accept,
            @Path("username") String username
    );

    @GET("orgs/{organization}/repos")
    Observable<List<RepositoryApi>> getRepositoriesFromOrganization(
            @Path("organization") String organization,
            @Query("page") Integer page,
            @Query("per_page") Integer reposPerPage
    );

    @GET("users/{username}/repos")
    Observable<List<RepositoryApi>> getRepositoriesFromUser(
            @Path("username") String username,
            @Query("page") Integer page,
            @Query("per_page") Integer reposPerPage
    );

    @GET("repos/{user}/{repositoryName}")
    Observable<RepositoryApi> getRepositoryDetail(
            @Path("user") String user,
            @Path("repositoryName") String repositoryName
    );
}
