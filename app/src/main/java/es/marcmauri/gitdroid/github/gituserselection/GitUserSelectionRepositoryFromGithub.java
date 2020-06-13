package es.marcmauri.gitdroid.github.gituserselection;

import android.util.Log;

import es.marcmauri.gitdroid.http.GitHubApiService;
import es.marcmauri.gitdroid.http.apimodel.github.UserApi;
import io.reactivex.Observable;

public class GitUserSelectionRepositoryFromGithub implements GitUserSelectionRepository {

    private static final String TAG = GitUserSelectionRepositoryFromGithub.class.getName();
    private GitHubApiService gitHubApiService;

    public GitUserSelectionRepositoryFromGithub(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    @Override
    public Observable<UserApi> getGitUserDetails(String username) {
        Log.i(TAG, "Called getGitUserDetails(username=" + username + ")");
        return gitHubApiService.getUserInfoObservable(username);
    }
}
