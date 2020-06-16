package es.marcmauri.gitdroid.github.gitrepositories;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.junit.rules.RxImmediateSchedulerRule;
import io.reactivex.Observable;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by marcmauriruiz on 15/06/20
 */
@RunWith(MockitoJUnitRunner.class)
public class GitRepositoriesPresenterTest {

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Mock
    private GitRepositoriesMVP.View view;
    @Mock
    private GitRepositoriesMVP.State state;
    @Mock
    private GitRepositoriesMVP.Model model;
    private GitRepositoriesPresenter presenter;

    @Before
    public void setUp() {
        presenter = new GitRepositoriesPresenter(model);
        presenter.subscribe(view);
    }

    @After
    public void tearDown() {
        presenter.unsubscribe();
    }

    @Test
    public void shouldNavigateToNextActivityWhenRepositoryItemIsClickedTest() {
        // Preparar comportamiento
        GitRepositoryBasicModel repo =
                new GitRepositoryBasicModel(1, "val", "val", "val", "val");
        // Preparar metodo
        presenter.loadRepositoryDetails(repo);
        // Verificar comportamiento
        verify(view, only()).goToGitRepositoryDetail(repo, ExtraTags.EXTRA_GIT_REPOSITORY_BASIC);
    }

    @Test
    public void shouldLoadRepositoriesByNameWithSomeDataWhenResultFieldIsFilledAndFoundAtLeastOneRepoTest() {
        // Preparar comportamiento
        String query = "gitdroid";
        int page = 1;
        GitRepositoryBasicModel foundRepo =
                new GitRepositoryBasicModel(
                        1,
                        query,
                        "user/" + query,
                        "description",
                        "user");
        Observable<GitRepositoryBasicModel> observable = Observable.just(foundRepo);
        doReturn(observable).when(model).getGitPublicRepositoriesByName(query, page);
        // Preparar metodo
        presenter.onSearchFieldChanges(query);
        // Verificar comportamiento
        verify(model).getGitPublicRepositoriesByName(query, page);
        verify(view, atLeastOnce()).addRepositoryItem(foundRepo);
    }

    @Test
    public void shouldLoadRepositoriesByNameWithNoDataWhenResultFieldIsFilledButNotFoundAnyRepoTest() {
        // Preparar comportamiento
        String query = "no-repository-for-this-rare-query";
        int page = 1;
        Observable<GitRepositoryBasicModel> observable = Observable.empty();
        doReturn(observable).when(model).getGitPublicRepositoriesByName(query, page);
        // Preparar metodo
        presenter.onSearchFieldChanges(query);
        // Verificar comportamiento
        verify(model).getGitPublicRepositoriesByName(query, page);
        verify(view, times(1)).removeAllRepositories();
        verify(view, times(1)).showAllPagesRetrieved();
        verify(view, never()).showRepositoryPageFetched();
    }

    @Test
    public void shouldLoadRepositoriesByNameWithErrorWhenSomethingWentWrongOnTheModelCallTest() {
        // Preparar comportamiento
        String query = "no-repository-for-this-rare-query";
        int page = 1;
        Observable<GitRepositoryBasicModel> observable = Observable.error(new Throwable());
        doReturn(observable).when(model).getGitPublicRepositoriesByName(query, page);
        // Preparar metodo
        presenter.onSearchFieldChanges(query);
        // Verificar comportamiento
        verify(model).getGitPublicRepositoriesByName(query, page);
        verify(view, times(2)).removeAllRepositories();
        verify(view, times(1)).showPublicRepositoriesNotFound();
    }

    @Test
    public void shouldLoadRepositoriesWhenResultFielditHasJustBeenErasedTest() {
        // Preparar comportamiento
        String query = "";
        int page = 1;
        long repoLastSeenId = 0;
        GitRepositoryBasicModel someRepo =
                new GitRepositoryBasicModel(
                        1,
                        query,
                        "user/" + query,
                        "description",
                        "user");
        Observable<GitRepositoryBasicModel> observable = Observable.just(someRepo);
        doReturn(observable).when(model).getGitPublicRepositories(repoLastSeenId);
        // Preparar metodo
        presenter.onSearchFieldChanges(query);
        // Verificar comportamiento
        verify(model, times(1)).getGitPublicRepositories(repoLastSeenId);
        verify(model, never()).getGitPublicRepositoriesByName(query, page);
        verify(view, atLeastOnce()).addRepositoryItem(someRepo);
    }

    @Test
    public void shouldLoadRepositoriesWithSomeDataWhenResultNotFilledAndFoundAtLeastOneRepoTest() {
        // Preparar comportamiento
        long repoLastSeenId = 0;
        GitRepositoryBasicModel foundRepo =
                new GitRepositoryBasicModel(
                        1,
                        "repo",
                        "user/repo",
                        "description",
                        "user");
        Observable<GitRepositoryBasicModel> observable = Observable.just(foundRepo);
        doReturn(observable).when(model).getGitPublicRepositories(repoLastSeenId);
        // Preparar metodo
        presenter.loadPublicRepositories();
        // Verificar comportamiento
        verify(model).getGitPublicRepositories(repoLastSeenId);
        verify(view, atLeastOnce()).addRepositoryItem(foundRepo);
    }

    @Test
    public void shouldLoadRepositoriesWithNoDataWhenResultNotFilledButNotFoundAnyRepoTest() {
        // Preparar comportamiento
        long repoLastSeenId = 0;
        Observable<GitRepositoryBasicModel> observable = Observable.empty();
        doReturn(observable).when(model).getGitPublicRepositories(repoLastSeenId);
        // Preparar metodo
        presenter.loadPublicRepositories();
        // Verificar comportamiento
        verify(model).getGitPublicRepositories(repoLastSeenId);
        verify(view, times(1)).showAllPagesRetrieved();
        verify(view, never()).showRepositoryPageFetched();
    }

    @Test
    public void shouldLoadRepositoriesWithErrorWhenSomethingWentWrongOnTheModelCallTest() {
        // Preparar comportamiento
        long repoLastSeenId = 0;
        Observable<GitRepositoryBasicModel> observable = Observable.error(new Throwable());
        doReturn(observable).when(model).getGitPublicRepositories(repoLastSeenId);
        // Preparar metodo
        presenter.loadPublicRepositories();
        // Verificar comportamiento
        verify(model).getGitPublicRepositories(repoLastSeenId);
        verify(view, times(1)).removeAllRepositories();
        verify(view, times(1)).showPublicRepositoriesNotFound();
    }

    @Test
    public void shouldLoadNextGitRepositoryPageWhenRepositoryListIsScrolledToTheBottomListTest() {
        // Preparar comportamiento
        Observable<GitRepositoryBasicModel> dummyObservable = Observable.empty();
        doReturn(dummyObservable).when(model).getGitPublicRepositories(0);
        // Preparar metodo
        int visibleItemCount = 2;
        int totalItemCount = 6;
        int pastVisibleItems = 4;
        int dy = 1;
        presenter.onRecyclerViewScrolled(visibleItemCount, totalItemCount, pastVisibleItems, dy);
        // Verificar comportamiento
        verify(model, times(1)).getGitPublicRepositories(0);
    }

    @Test
    public void shouldNotLoadNextGitRepositoryPageWhenRepositoryListIsScrolledDownButNotToTheBottomTest() {
        // Preparar comportamiento
        // Preparar metodo
        int visibleItemCount = 2;
        int totalItemCount = 6;
        int pastVisibleItems = 3;
        int dy = 1;
        presenter.onRecyclerViewScrolled(visibleItemCount, totalItemCount, pastVisibleItems, dy);
        // Verificar comportamiento
        verify(model, never()).getGitPublicRepositories(0);
    }

    @Test
    public void shouldNotLoadNextGitRepositoryPageWhenRepositoryListIsScrolledUpTest() {
        // Preparar comportamiento
        // Preparar metodo
        int visibleItemCount = 2;
        int totalItemCount = 6;
        int pastVisibleItems = 4;
        int dy = -1;
        presenter.onRecyclerViewScrolled(visibleItemCount, totalItemCount, pastVisibleItems, dy);
        // Verificar comportamiento
        verify(model, never()).getGitPublicRepositories(0);
    }

    @Test
    public void shouldSetRepositoryItemsAndPositionToViewWhenSubscribeAndRestoreStateCalledTest() {
        // Preparar comportamiento
        // Preparar metodo
        ArrayList<GitRepositoryBasicModel> repositoryItems = new ArrayList<>();
        int repositoryPosition = 1;
        GitRepositoriesState state = new GitRepositoriesState(repositoryItems, repositoryPosition,
                0, 0, false, false, "");
        presenter.subscribeAndRestoreState(view, state);
        // Verificar comportamiento
        verify(view, times(1)).setRepositoryItems(repositoryItems);
        verify(view, times(1)).setRepositoryPosition(repositoryPosition);
    }
}