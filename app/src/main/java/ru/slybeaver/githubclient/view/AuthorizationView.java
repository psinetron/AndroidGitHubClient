package ru.slybeaver.githubclient.view;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface AuthorizationView extends BaseView {

    void authorizationComplete();

    void authorizationFailed();

    void failVibrate();

    String getUserLogin();

    String getUserPassword();

}
