package es.marcmauri.gitdroid.root;

import android.app.Application;

import es.marcmauri.gitdroid.github.userselection.GitSearchUserModule;
import es.marcmauri.gitdroid.github.repositorylist.GitRepositoriesModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .gitSearchUserModule(new GitSearchUserModule())
                .gitRepositoriesModule(new GitRepositoriesModule())
                .gitHubApiModule(new GitHubApiModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
