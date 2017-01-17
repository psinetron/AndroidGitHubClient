package ru.slybeaver.githubclient.presenter;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.slybeaver.githubclient.model.dto.RepositoryDTO;
import ru.slybeaver.githubclient.managers.CacheManager;
import ru.slybeaver.githubclient.managers.StorageManager;
import ru.slybeaver.githubclient.view.BaseView;
import ru.slybeaver.githubclient.view.RepositoriesView;

/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class RepositoriesPresenter extends BasePresenter {

    private RepositoriesView view;
    private boolean SEARCH_REQUEST = false;
    private String lastQuery = "";

    public RepositoriesPresenter(RepositoriesView view) {
        this.view = view;
    }

    @Override
    protected BaseView getView() {
        return view;
    }

    public void onCreate(Bundle savedInstanceState, Intent intent) {
        if (savedInstanceState != null) {
            SEARCH_REQUEST = savedInstanceState.getBoolean("SEARCH_REQUEST", false);
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            SEARCH_REQUEST = true;
        }
        if (SEARCH_REQUEST) {
            handleIntent(intent);
        } else {
            loadMyRepos(true);
        }
    }

    private void loadMyRepos(boolean fromCache) {
        view.showLoading();
        compositeDisposable.clear();
        Disposable repos = model.getMyRepos(StorageManager.getInstance().getToken(), fromCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            view.hideLoading();
                            view.showRepositoriest(next);
                            CacheManager.getInstance().setCache("getMyRepos", StorageManager.getInstance().getToken(), next);
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        },
                        () -> {
                        });
        addDisposable(repos);
    }

    private void searchRepos(String query, boolean fromCache) {
        view.showLoading();
        compositeDisposable.clear();
        Disposable searchedRepos = model.searchRepos(query, fromCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            view.hideLoading();
                            view.showRepositoriest(next.getItems());
                            CacheManager.getInstance().setCache("searchRepos", query, next);
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        },
                        () -> {
                        });
        addDisposable(searchedRepos);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("SEARCH_REQUEST", SEARCH_REQUEST);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        SEARCH_REQUEST = savedInstanceState.getBoolean("SEARCH_REQUEST", false);
    }

    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public void clickRepo(RepositoryDTO repository) {
        Bundle args = new Bundle();
        args.putString("path", repository.getOwner().getLogin() + "/" + repository.getName() + "/contents");
        StorageManager.getInstance().setStartPath(repository.getOwner().getLogin() + "/" + repository.getName() + "/contents");
        view.openRepoContent(args);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) { //We find repositories
            lastQuery = intent.getStringExtra(SearchManager.QUERY);
            view.setToolBarTitle(lastQuery);
            searchRepos(lastQuery, true);
        }
    }

    public void onRefresh() {
        if (SEARCH_REQUEST) {
            searchRepos(lastQuery, false);
        } else {
            loadMyRepos(false);
        }
    }

}
