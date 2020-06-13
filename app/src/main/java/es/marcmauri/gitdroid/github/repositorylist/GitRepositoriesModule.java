package es.marcmauri.gitdroid.github.repositorylist;

import dagger.Module;
import dagger.Provides;
import es.marcmauri.gitdroid.http.GitHubApiService;

@Module
public class GitRepositoriesModule {

    @Provides
    public GitRepositoriesMVP.Presenter provideGitListReposPresenter(GitRepositoriesMVP.Model model) {
        return new GitRepositoriesPresenter(model);
    }

    @Provides
    public GitRepositoriesMVP.Model provideGitListReposModel(GitRepositoriesRepository gitRepositoriesRepository) {
        return new GitRepositoriesModel(gitRepositoriesRepository);
    }

    @Provides
    public GitRepositoriesRepository provideGitListReposRepository(GitHubApiService gitHubApiService) {
        return new GitRepositoriesRepositoryFromGithub(gitHubApiService);
    }
}
