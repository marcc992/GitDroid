package es.marcmauri.gitdroid.root;

import javax.inject.Singleton;

import dagger.Component;
import es.marcmauri.gitdroid.github.gitmain.GitMainActivity;
import es.marcmauri.gitdroid.github.gitmain.GitMainModule;
import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesActivity;
import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesModule;
import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailActivity;
import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        GitMainModule.class,
        GitRepositoriesModule.class,
        GitRepositoryDetailModule.class,
        GitHubApiModule.class
})
public interface ApplicationComponent {

    void inject(GitMainActivity target);

    void inject(GitRepositoriesActivity target);

    void inject(GitRepositoryDetailActivity target);
}
