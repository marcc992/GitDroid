package es.marcmauri.gitdroid.github.gitmain;

import dagger.Module;
import dagger.Provides;

@Module
public class GitMainModule {

    @Provides
    public GitMainMVP.Presenter provideGitSearchUserPresenter() {
        return new GitMainPresenter();
    }
}
