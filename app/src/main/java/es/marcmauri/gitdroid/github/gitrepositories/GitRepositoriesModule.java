package es.marcmauri.gitdroid.github.gitrepositories;

import dagger.Module;
import dagger.Provides;
import es.marcmauri.gitdroid.http.GitHubApiService;

@Module
public class GitRepositoriesModule {

    @Provides
    public GitRepositoriesMVP.Presenter provideGitRepositoriesPresenter(GitRepositoriesMVP.Model model) {
        return new GitRepositoriesPresenter(model);
    }

    @Provides
    public GitRepositoriesMVP.Model provideGitRepositoriesModel(GitRepositoriesRepository gitRepositoriesRepository) {
        return new GitRepositoriesModel(gitRepositoriesRepository);
    }

    @Provides
    public GitRepositoriesRepository provideGitRepositoriesRepository(GitHubApiService gitHubApiService) {
        return new GitRepositoriesRepositoryFromGithub(gitHubApiService);
    }
}
