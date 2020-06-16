package es.marcmauri.gitdroid.github.gitmain;

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
