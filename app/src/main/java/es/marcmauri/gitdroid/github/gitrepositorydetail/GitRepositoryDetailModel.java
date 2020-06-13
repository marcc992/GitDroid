package es.marcmauri.gitdroid.github.gitrepositorydetail;

import android.util.Log;

import es.marcmauri.gitdroid.github.viewmodel.GitLicenseModel;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import es.marcmauri.gitdroid.http.apimodel.github.RepositoryApi;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GitRepositoryDetailModel implements GitRepositoryDetailMVP.Model {

    private static final String TAG = GitRepositoryDetailModel.class.getName();

    private GitRepositoryDetailRepository repository;


    public GitRepositoryDetailModel(GitRepositoryDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<GitRepositoryDetailedModel> getGitRepositoryDetail(String user, String repositoryName) {
        return repository.getGitRepositoryDetail(user, repositoryName)
                .flatMap(new Function<RepositoryApi, Observable<GitRepositoryDetailedModel>>() {
                    @Override
                    public Observable<GitRepositoryDetailedModel> apply(RepositoryApi repositoryApi) {
                        GitLicenseModel gitLicenseModel = null;
                        if (repositoryApi.getLicense() != null) {
                            gitLicenseModel = new GitLicenseModel(
                                    repositoryApi.getLicense().getKey(),
                                    repositoryApi.getLicense().getName(),
                                    repositoryApi.getLicense().getUrl());
                        } else {
                            Log.i(TAG, "The repository " + repositoryApi.getName() + " has no License");
                        }

                        GitRepositoryDetailedModel gitRepositoryDetailedModel =
                                new GitRepositoryDetailedModel(
                                        repositoryApi.getId(),
                                        repositoryApi.getNodeId(),
                                        repositoryApi.getName(),
                                        repositoryApi.getFullName(),
                                        repositoryApi.getDescription(),
                                        repositoryApi.getLanguage(),
                                        repositoryApi.getDefaultBranch(),
                                        repositoryApi.getCreatedAt(),
                                        repositoryApi.getUpdatedAt(),
                                        repositoryApi.getSize(),
                                        repositoryApi.getOpenIssues(),
                                        repositoryApi.getWatchers(),
                                        repositoryApi.getHtmlUrl(),
                                        repositoryApi.getGitUrl(),
                                        repositoryApi.getSshUrl(),
                                        repositoryApi.getCloneUrl(),
                                        repositoryApi.getHomepage(),
                                        gitLicenseModel);


                        return Observable.just(gitRepositoryDetailedModel);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                });
    }
}
