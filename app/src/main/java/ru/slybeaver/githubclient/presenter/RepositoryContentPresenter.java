package ru.slybeaver.githubclient.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.slybeaver.githubclient.model.dto.RepositoryContentDTO;
import ru.slybeaver.githubclient.managers.CacheManager;
import ru.slybeaver.githubclient.managers.StorageManager;
import ru.slybeaver.githubclient.view.BaseView;
import ru.slybeaver.githubclient.view.FileContentActivity;
import ru.slybeaver.githubclient.view.RepositoryContentActivity;
import ru.slybeaver.githubclient.view.RepositoryContentView;
import ru.slybeaver.githubclient.view.comporators.FileComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by psinetron on 12.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class RepositoryContentPresenter extends BasePresenter {

    private RepositoryContentView view;
    private Bundle lastBundle = null;

    public RepositoryContentPresenter(RepositoryContentView view) {
        this.view = view;
    }

    @Override
    protected BaseView getView() {
        return view;
    }

    public void onCreate(Intent intent) {
        lastBundle = new Bundle();

        if (StorageManager.getInstance().getCurrentPath() != null) {
            lastBundle.putString("path", StorageManager.getInstance().getCurrentPath());
            view.setToolBarTitle(StorageManager.getInstance().getCurrentPath());
        } else {
            if (intent.getExtras() != null) {
                lastBundle.putString("path", intent.getExtras().getString("path", ""));
                view.setToolBarTitle(intent.getExtras().getString("path", ""));
            }
        }
        loadRepositoryContent(true);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("savedBundle", lastBundle);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        lastBundle = savedInstanceState.getBundle("savedBundle");
    }

    public void onNewIntent(Intent intent) {
        lastBundle.putString("path", intent.getExtras().getString("path", null));
        view.setToolBarTitle(lastBundle.getString("path", ""));
        loadRepositoryContent(true);
    }

    public void onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
    }

    public void onBackPressed() {
        String backPath = StorageManager.getInstance().getBackPath();
        if (backPath != null && backPath.equalsIgnoreCase(lastBundle.getString("path", ""))) {
            backPath = StorageManager.getInstance().getBackPath();
        }
        if (backPath != null) {
            Bundle args = new Bundle();
            args.putString("path", backPath);
            view.startActivity(args);
        } else {
            view.backPress();
        }
    }

    private void loadRepositoryContent(boolean fromCache) {
        compositeDisposable.clear();
        view.showLoading();
        Disposable repositoryContent = model.getReposContent(lastBundle.getString("path", ""), StorageManager.getInstance().getToken(), fromCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            Collections.sort(next, new FileComparator());
                            view.hideLoading();
                            view.showContent((ArrayList<RepositoryContentDTO>) next, lastBundle.getString("path", ""));
                            CacheManager.getInstance().setCache("getFileContent", lastBundle.getString("path", ""), next);
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        },
                        () -> {
                        });
        addDisposable(repositoryContent);
    }

    public void clickRepositoryContent(String path) {
        Bundle args = new Bundle();
        args.putString("path", path);
        StorageManager.getInstance().addNextPath(path);
        view.openContent(args, RepositoryContentActivity.class);
    }

    public void clickFile(String path) {
        Bundle args = new Bundle();
        args.putString("url", path);
        view.openContent(args, FileContentActivity.class);
    }

    public void onRefresh() {
        loadRepositoryContent(false);
    }

}
