package ru.slybeaver.githubclient.view;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface BaseView {

    void showError(String error);

    void showError(int id);

    void showLoading();

    void hideLoading();
}
