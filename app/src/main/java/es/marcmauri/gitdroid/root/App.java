package es.marcmauri.gitdroid.root;

import android.app.Application;

import es.marcmauri.gitdroid.github.searchuser.GitSearchUserModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .gitSearchUserModule(new GitSearchUserModule())
                .gitHubApiModule(new GitHubApiModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
