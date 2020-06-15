package es.marcmauri.gitdroid.github.gitmain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * Created by marcmauriruiz on 15/06/20
 */
@RunWith(MockitoJUnitRunner.class)
public class GitMainPresenterTest {

    //1.- Hacer Mock de Clases o Interfaces
    @Mock
    private GitMainMVP.View view;

    private GitMainPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new GitMainPresenter();
        presenter.setView(view);
    }

    @Test
    public void shouldNavigateToNextActivityWhenGetRepositoriesButtonClickedTest() {
        //2.- Nos permite programar un comportamiento
        // nothing_to_do
        //3.- Ejecutamos un metodo
        presenter.loadPublicRepositories();
        //4.- Verificar que la vista ejecuta el metodo goToGitRepositories(), una unica vez
        verify(view, only()).goToGitRepositories();
    }
}