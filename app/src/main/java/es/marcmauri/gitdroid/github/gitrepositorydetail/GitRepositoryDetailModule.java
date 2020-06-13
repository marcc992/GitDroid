package es.marcmauri.gitdroid.github.gitrepositorydetail;

import dagger.Module;
import dagger.Provides;
import es.marcmauri.gitdroid.http.GitHubApiService;

@Module
public class GitRepositoryDetailModule {

    @Provides
    public GitRepositoryDetailMVP.Presenter provideGitRepositoryDetailPresenter(GitRepositoryDetailMVP.Model model) {
        return new GitRepositoryDetailPresenter(model);
    }

    @Provides GitRepositoryDetailMVP.Model provideGitRepositoryDetailModel(GitRepositoryDetailRepository repository) {
        return new GitRepositoryDetailModel(repository);
    }

    @Provides GitRepositoryDetailRepository provideGitRepositoryDetailRepository(GitHubApiService gitHubApiService) {
        return new GitRepositoryDetailRepositoryFromGithub(gitHubApiService);
    }
}
