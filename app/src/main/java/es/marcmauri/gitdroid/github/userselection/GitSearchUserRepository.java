package es.marcmauri.gitdroid.github.userselection;

import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public interface GitSearchUserRepository {

    Observable<UserApi> getGitUserDetails(String username);
}
