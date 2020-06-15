package es.marcmauri.gitdroid.github.gitmain;

import android.content.Intent;

public interface GitMainMVP {

    interface View {
        void goToGitRepositories();
    }

    interface Presenter {
        void loadPublicRepositories();

        void setView(GitMainMVP.View view);
    }

    interface Model {
        // This component does not need any model
    }
}
