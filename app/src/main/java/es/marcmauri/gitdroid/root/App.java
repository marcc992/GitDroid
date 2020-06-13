package es.marcmauri.gitdroid.root;

import android.app.Application;

import es.marcmauri.gitdroid.github.gitrepositorydetail.GitRepositoryDetailModule;
import es.marcmauri.gitdroid.github.gituserselection.GitUserSelectionModule;
import es.marcmauri.gitdroid.github.gitrepositories.GitRepositoriesModule;
import es.marcmauri.gitdroid.http.GitHubApiModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .gitUserSelectionModule(new GitUserSelectionModule())
                .gitRepositoriesModule(new GitRepositoriesModule())
                .gitRepositoryDetailModule(new GitRepositoryDetailModule())
                .gitHubApiModule(new GitHubApiModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
