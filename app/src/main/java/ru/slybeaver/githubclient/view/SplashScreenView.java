package ru.slybeaver.githubclient.view;

/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface SplashScreenView extends BaseView {

    void authorizationComplete();

    void authorizationFailed();

}
