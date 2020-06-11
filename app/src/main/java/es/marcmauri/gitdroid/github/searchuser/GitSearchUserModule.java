package es.marcmauri.gitdroid.github.searchuser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.marcmauri.gitdroid.http.GitHubApiService;

@Module
public class GitSearchUserModule {

    @Provides
    public GitSearchUserMVP.Presenter provideGitSearchUserPresenter(GitSearchUserMVP.Model model) {
        return new GitSearchUserPresenter(model);
    }

    @Provides
    public GitSearchUserMVP.Model provideGitSearchUserModel(GitSearchUserRepository gitSearchUserRepository) {
        return new GitSearchUserModel(gitSearchUserRepository);
    }

    @Singleton
    @Provides
    public GitSearchUserRepository provideGitSearchUserRepository(GitHubApiService gitHubApiService) {
        return new GitSearchUserRepositoryFromGithub(gitHubApiService);
    }
}
