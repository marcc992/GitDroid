package es.marcmauri.gitdroid.github.gitrepositorydetail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import es.marcmauri.gitdroid.common.ExtraTags;
import es.marcmauri.gitdroid.github.viewmodel.GitLicenseModel;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryDetailedModel;
import es.marcmauri.gitdroid.github.viewmodel.GitUserModel;
import es.marcmauri.gitdroid.junit.rules.RxImmediateSchedulerRule;
import io.reactivex.Observable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcmauriruiz on 15/06/20
 */
@RunWith(MockitoJUnitRunner.class)
public class GitRepositoryDetailPresenterTest {

    //1.- Hacer Mock de Clases o Interfaces
    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Mock
    private GitRepositoryDetailMVP.View view;

    @Mock
    private GitRepositoryDetailMVP.Model model;

    private GitRepositoryDetailPresenter presenter;

    @Before
    public void setUp() {
        presenter = new GitRepositoryDetailPresenter(model);
        presenter.setView(view);
    }

    @After
    public void tearDown() {
        presenter.rxJavaUnsubscribe();
    }

    @Test
    public void shouldLoadUserOwnerWhenUserIsValidInRepositoryBasicModelOnBundleTest() {
        //2.- Programamos el comportamiento
        GitRepositoryBasicModel repository =
                new GitRepositoryBasicModel(
                        1,
                        "name",
                        "fullName",
                        "description",
                        "marcc992");
        when(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC)).thenReturn(repository);
        Assert.assertEquals(repository, view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC));

        GitUserModel user = new GitUserModel(1, repository.getOwnerName(), "url");
        Observable<GitUserModel> userModelObservable = Observable.just(user);
        doReturn(userModelObservable).when(model).getGitUserDetails(repository.getOwnerName());

        //3.- Programamos el metodo
        presenter.loadOwnerDetails();

        //4.- Verificamos comportamiento
        verify(model).getGitUserDetails(repository.getOwnerName());
    }

    @Test
    public void shouldNotLoadUserOwnerWhenUserIsNotValidInRepositoryBasicModelOnBundleTest() {
        //2.- Programamos el comportamiento
        GitRepositoryBasicModel repository =
                new GitRepositoryBasicModel(
                        1,
                        "name",
                        "fullName",
                        "description",
                        "");
        when(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC)).thenReturn(repository);
        Assert.assertEquals(repository, view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC));

        Observable<GitUserModel> userModelObservable = Observable.error(new Throwable());
        doReturn(userModelObservable).when(model).getGitUserDetails(repository.getOwnerName());

        //3.- Programamos el metodo
        presenter.loadOwnerDetails();

        //4.- Verificamos comportamiento
        verify(view, times(1)).showUserError();
    }

    @Test
    public void shouldNotLoadUserOwnerWhenRepositoryBasicModelOnBundleIsNotValidTest() {
        //2.- Programamos el comportamiento
        when(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC)).thenReturn(null);
        Assert.assertNull(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC));

        //3.- Programamos el metodo
        presenter.loadOwnerDetails();

        //4.- Verificamos comportamiento
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showUserError();
    }

    @Test
    public void shouldLoadRepositoryDetailsWhenRepositoryBasicModelOnBundleIsValidTest() {
        //2.- Programamos el comportamiento
        GitRepositoryBasicModel bundleRepo =
                new GitRepositoryBasicModel(
                        1,
                        "name",
                        "fullName",
                        "description",
                        "marcc992");
        when(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC)).thenReturn(bundleRepo);
        Assert.assertEquals(bundleRepo, view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC));

        GitRepositoryDetailedModel detailedRepo =
                new GitRepositoryDetailedModel(
                        bundleRepo.getId(), "nodeid", bundleRepo.getName(),
                        bundleRepo.getFullName(), bundleRepo.getDescription(),
                        "contentLanguage", "defaultBranch", new Date(),
                        new Date(), 0, 0, 0, "htmlUrl", "gitUrl",
                        "sshUrl", "cloneUrl", "homepage",
                        new GitLicenseModel("key", "name", "url"));
        Observable<GitRepositoryDetailedModel> gitRepoObservable = Observable.just(detailedRepo);
        doReturn(gitRepoObservable).when(model).getGitRepositoryDetail(bundleRepo.getOwnerName(), bundleRepo.getName());

        //3.- Programamos el metodo
        presenter.loadRepositoryDetails();

        //4.- Verificamos comportamiento
        verify(model).getGitRepositoryDetail(bundleRepo.getOwnerName(), bundleRepo.getName());
    }

    @Test
    public void shouldNotLoadRepositoryDetailsWhenRepositoryIsInvalidOnRepositoryBasicModelOnBundleTest() {
        //2.- Programamos el comportamiento
        GitRepositoryBasicModel bundleRepo =
                new GitRepositoryBasicModel(
                        1,
                        "name",
                        "fullName",
                        "description",
                        "marcc992");
        when(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC)).thenReturn(bundleRepo);
        Assert.assertEquals(bundleRepo, view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC));

        Observable<GitRepositoryDetailedModel> gitRepoObservable = Observable.error(new Throwable());
        doReturn(gitRepoObservable).when(model).getGitRepositoryDetail(bundleRepo.getOwnerName(), bundleRepo.getName());

        //3.- Programamos el metodo
        presenter.loadRepositoryDetails();

        //4.- Verificamos comportamiento
        verify(view, times(1)).showRepositoryError();
    }

    @Test
    public void shouldNotLoadRepositoryDetailsWhenRepositoryBasicModelOnBundleIsNotValidTest() {
        //2.- Programamos el comportamiento
        when(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC)).thenReturn(null);
        Assert.assertNull(view.getRepositoryBasicModelFromExtras(ExtraTags.EXTRA_GIT_REPOSITORY_BASIC));

        //3.- Programamos el metodo
        presenter.loadRepositoryDetails();

        //4.- Verificamos comportamiento
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showRepositoryError();
    }
}