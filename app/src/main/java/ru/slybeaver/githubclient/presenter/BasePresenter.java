package ru.slybeaver.githubclient.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.slybeaver.githubclient.model.BaseModel;
import ru.slybeaver.githubclient.model.BaseModelImpl;
import ru.slybeaver.githubclient.view.BaseView;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
abstract class BasePresenter implements Presenter {

    protected BaseModel model = new BaseModelImpl();

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    void addDisposable(Disposable subscription) {
        compositeDisposable.add(subscription);
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
    }

    protected abstract BaseView getView();

    void showLoadingState() {
        getView().showLoading();
    }

    void hideLoadingState() {
        getView().hideLoading();
    }

    public void showError(String errorText) {
        getView().showError(errorText);
    }

    public void showError(int id) {
        getView().showError(id);
    }


}
