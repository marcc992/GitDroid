package es.marcmauri.gitdroid.github.searchuser;

import es.marcmauri.gitdroid.github.GitUserViewModel;
import io.reactivex.Observable;

public class GitSearchUserModel implements GitSearchUserMVP.Model{

    private static final String TAG = GitSearchUserModel.class.getName();

    private GitSearchUserRepository githubRepository;

    public GitSearchUserModel(GitSearchUserRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    @Override
    public Observable<GitUserViewModel> getGitUserDetails(String username) {
        // Todo: El repo nos podria mandar un pojo, y aqui en el Modelo encargarnos de
        //       transformarlo al objeto ViewModel
        //return githubRepository.getGitUserDetails(username);
        return Observable.empty();
    }
}
