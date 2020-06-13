package es.marcmauri.gitdroid.github.gituserselection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.marcmauri.gitdroid.http.GitHubApiService;

@Module
public class GitUserSelectionModule {

    @Provides
    public GitUserSelectionMVP.Presenter provideGitSearchUserPresenter(GitUserSelectionMVP.Model model) {
        return new GitUserSelectionPresenter(model);
    }

    @Provides
    public GitUserSelectionMVP.Model provideGitSearchUserModel(GitUserSelectionRepository gitUserSelectionRepository) {
        return new GitUserSelectionModel(gitUserSelectionRepository);
    }

    @Singleton
    @Provides
    public GitUserSelectionRepository provideGitSearchUserRepository(GitHubApiService gitHubApiService) {
        return new GitUserSelectionRepositoryFromGithub(gitHubApiService);
    }
}
