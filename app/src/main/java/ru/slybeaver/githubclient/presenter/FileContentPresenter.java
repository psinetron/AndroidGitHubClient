package ru.slybeaver.githubclient.presenter;

import android.content.Intent;
import android.os.Bundle;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.slybeaver.githubclient.managers.CacheManager;
import ru.slybeaver.githubclient.view.BaseView;
import ru.slybeaver.githubclient.view.FileContentView;

/**
 * Created by psinetron on 13.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class FileContentPresenter extends BasePresenter {

    private FileContentView view;
    private Bundle lastBundle = null;

    public FileContentPresenter(FileContentView view) {
        this.view = view;
    }

    @Override
    protected BaseView getView() {
        return view;
    }

    public void onCreate(Intent intent) {
        lastBundle = new Bundle();
        if (intent != null) {
            lastBundle.putString("url", intent.getExtras().getString("url"));
            view.setToolBarTitle(intent.getExtras().getString("url"));
        }
        loadFile(true);
    }

    private void loadFile(boolean fromCache) {
        view.showLoading();
        compositeDisposable.clear();
        Disposable repos = model.getFileContent(lastBundle.getString("url"), fromCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            view.hideLoading();
                            String result = next.string();
                            view.showCode(result);
                            CacheManager.getInstance().setCache("getFileContent", lastBundle.getString("url"), result);
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        },
                        () -> {
                        });
        addDisposable(repos);
    }

    public void onRefresh() {
        loadFile(false);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("savedBundle", lastBundle);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        lastBundle = savedInstanceState.getBundle("savedBundle");
    }
}
