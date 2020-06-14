package es.marcmauri.gitdroid.github.gitmain;

import android.content.Intent;

public interface GitMainMVP {

    interface View {
        void showSnackBar(String message);

        void navigateToNextActivity(Intent i);
    }

    interface Presenter {
        void goToPublicRepositories();

        void setView(GitMainMVP.View view);
    }

    interface Model {
        // This component does not need any model
    }
}
