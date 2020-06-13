package es.marcmauri.gitdroid.github.gituserselection;

import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public interface GitUserSelectionRepository {

    Observable<UserApi> getGitUserDetails(String username);
}
