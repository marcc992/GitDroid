package es.marcmauri.gitdroid.github.userselection;

import android.util.Log;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public class GitSearchUserRepositoryFromGithub implements GitSearchUserRepository {

    private static final String TAG = GitSearchUserRepositoryFromGithub.class.getName();
    private GitHubApiService gitHubApiService;

    public GitSearchUserRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    @Override
    public Observable<UserApi> getGitUserDetails(String username) {
        Log.i(TAG, "Called getGitUserDetails(username=" + username + ")");
        return gitHubApiService.getUserInfoObservable(username);
    }
}
