package es.marcmauri.gitdroid.http;

import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApiService {

    @GET("users/{username}")
    Observable<UserApi> getUserInfo(
            @Path("username") String username
    );
}
