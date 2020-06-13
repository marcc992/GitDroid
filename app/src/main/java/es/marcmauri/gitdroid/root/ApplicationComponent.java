package es.marcmauri.gitdroid.root;

import javax.inject.Singleton;

import dagger.Component;
import es.marcmauri.gitdroid.github.gituserselection.GitUserSelectionActivity;
import es.marcmauri.gitdroid.github.gituserselection.GitUserSelectionModule;
import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesActivity;
import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        GitUserSelectionModule.class,
        GitRepositoriesModule.class,
        GitHubApiModule.class
})
public interface ApplicationComponent {

    void inject(GitUserSelectionActivity target);

    void inject(GitRepositoriesActivity target);
}
