package es.marcmauri.gitdroid.github.gitrepositories;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.junit.rules.RxImmediateSchedulerRule;

import static org.mockito.Mockito.only;
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
    public void shouldNavigateToNextActivityWhenRepositoryCardIsClickedTest() {
        // Preparar comportamiento
        GitRepositoryBasicModel repo =
                new GitRepositoryBasicModel(1, "val", "val", "val", "val");
        // Preparar metodo
        presenter.loadRepositoryDetails(repo);
        // Verificar comportamiento
        verify(view, only()).goToGitRepositoryDetail(repo, ExtraTags.EXTRA_GIT_REPOSITORY_BASIC);
    }
}