package es.marcmauri.gitdroid.root;

import javax.inject.Singleton;

import dagger.Component;
import es.marcmauri.gitdroid.github.searchuser.GitSearchUserActivity;
import es.marcmauri.gitdroid.github.searchuser.GitSearchUserModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        GitSearchUserModule.class,
        GitHubApiModule.class
})
public interface ApplicationComponent {

    void inject(GitSearchUserActivity target);
}
