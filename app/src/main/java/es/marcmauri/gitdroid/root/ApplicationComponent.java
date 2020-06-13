package es.marcmauri.gitdroid.root;

import javax.inject.Singleton;

import dagger.Component;
import es.marcmauri.gitdroid.github.userselection.GitSearchUserActivity;
import es.marcmauri.gitdroid.github.userselection.GitSearchUserModule;
import es.marcmauri.gitdroid.github.repositorylist.GitRepositoriesActivity;
import es.marcmauri.gitdroid.github.repositorylist.GitRepositoriesModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        GitSearchUserModule.class,
        GitRepositoriesModule.class,
        GitHubApiModule.class
})
public interface ApplicationComponent {

    void inject(GitSearchUserActivity target);

    void inject(GitRepositoriesActivity target);
}
