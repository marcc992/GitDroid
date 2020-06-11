package es.marcmauri.gitdroid.github.searchuser;

import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public interface GitSearchUserRepository {

    Observable<UserApi> getGitUserDetailsFromNetwork(String username);
    Observable<UserApi> getGitUserDetailsFromCache(String username);
    Observable<UserApi> getGitUserDetails(String username);
}
