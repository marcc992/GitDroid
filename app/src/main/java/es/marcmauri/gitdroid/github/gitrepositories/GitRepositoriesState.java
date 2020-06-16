package es.marcmauri.gitdroid.github.gitrepositories;

import java.util.ArrayList;

import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;

public class GitRepositoriesState implements GitRepositoriesMVP.State {

    private ArrayList<GitRepositoryBasicModel> repositoryItems;
    private int repositoryPosition;
    private int currentPage;
    private long repositoryLastSeenId;
    private boolean allPagesRetrieved;
    private boolean searchingReposByName;
    private String searchQuery;


    public GitRepositoriesState(ArrayList<GitRepositoryBasicModel> repositoryItems,
                                int repositoryPosition, int currentPage, long repositoryLastSeenId,
                                boolean allPagesRetrieved, boolean searchingReposByName,
                                String searchQuery) {
        this.repositoryItems = repositoryItems;
        this.repositoryPosition = repositoryPosition;
        this.currentPage = currentPage;
        this.repositoryLastSeenId = repositoryLastSeenId;
        this.allPagesRetrieved = allPagesRetrieved;
        this.searchingReposByName = searchingReposByName;
        this.searchQuery = searchQuery;
    }

    @Override
    public ArrayList<GitRepositoryBasicModel> getRepositoryItems() {
        return this.repositoryItems;
    }

    @Override
    public int getRepositoryPosition() {
        return this.repositoryPosition;
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public long getRepositoryLastSeenId() {
        return this.repositoryLastSeenId;
    }

    @Override
    public boolean getAllPagesRetrieved() {
        return this.allPagesRetrieved;
    }

    @Override
    public boolean getSearchingReposByName() {
        return this.searchingReposByName;
    }

    @Override
    public String getSearchQuery() {
        return this.searchQuery;
    }
}
