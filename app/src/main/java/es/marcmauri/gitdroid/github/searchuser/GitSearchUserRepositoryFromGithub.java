package es.marcmauri.gitdroid.github.searchuser;

import android.util.Log;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public class GitSearchUserRepositoryFromGithub implements GitSearchUserRepository {

    private static final String TAG = GitSearchUserRepositoryFromGithub.class.getName();
    private static final long CACHE_LIFETIME = 20 * 1000; // 20 seconds for cache

    private GitHubApiService gitHubApiService;

    //TODO: array para guardar la relacion de usuarios con sus datos, o Map, o lo que sea

    private long lastTimestamp;

    public GitSearchUserRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;

        this.lastTimestamp = System.currentTimeMillis();
    }

    @Override
    public Observable<UserApi> getGitUserDetailsFromNetwork(String username) {
        Log.i(TAG, "Called getGitUserDetailsFromNetwork(username=" + username + ")");
        return null;
    }

    @Override
    public Observable<UserApi> getGitUserDetailsFromCache(String username) {
        Log.i(TAG, "Called getGitUserDetailsFromCache(username=" + username + ")");
        return null;
    }

    @Override
    public Observable<UserApi> getGitUserDetails(String username) {
        Log.i(TAG, "Called getGitUserDetails(username=" + username + ")");
        return getGitUserDetailsFromCache(username)
                .switchIfEmpty(getGitUserDetailsFromNetwork(username));
    }
}
