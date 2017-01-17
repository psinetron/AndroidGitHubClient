package ru.slybeaver.githubclient.presenter;


import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.managers.StorageManager;
import ru.slybeaver.githubclient.view.BaseView;
import ru.slybeaver.githubclient.view.SplashScreenView;
import java.io.IOException;


/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class SplashScreenPresenter extends BasePresenter {

    private SplashScreenView view;

    public SplashScreenPresenter(SplashScreenView view) {
        this.view = view;
    }

    @Override
    protected BaseView getView() {
        return view;
    }

    public void onStart() {
        if (view != null) {
            view.showLoading();
        }
        if (StorageManager.getInstance().getOAuthId() > 0) {
            confirmAuthorization();
        } else {
            view.authorizationFailed();
        }
    }

    public void confirmAuthorization() {
        Disposable authConfirm = model.confirmAuthorization(StorageManager.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> view.authorizationComplete(),
                        error -> {
                            if (error instanceof IOException) {
                                view.showError(R.string.no_internet_connection);
                            } else if (error instanceof HttpException) {
                                view.authorizationFailed();
                            }
                        },
                        () -> {
                        });
        addDisposable(authConfirm);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (view != null) {
            view.hideLoading();
        }
    }
}
