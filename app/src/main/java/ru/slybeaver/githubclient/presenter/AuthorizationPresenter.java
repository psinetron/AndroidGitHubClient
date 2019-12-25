package ru.slybeaver.githubclient.presenter;

import android.text.TextUtils;
import android.util.Base64;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.other.Const;
import ru.slybeaver.githubclient.managers.StorageManager;
import ru.slybeaver.githubclient.view.AuthorizationView;
import ru.slybeaver.githubclient.view.BaseView;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class AuthorizationPresenter extends BasePresenter {

    private AuthorizationView view;
    private boolean requestLock = false;

    public AuthorizationPresenter(AuthorizationView view) {
        this.view = view;
    }

    @Override
    protected BaseView getView() {
        return view;
    }

    public void authorizationClick() {
        if (requestLock) {
            return;
        }
        requestLock = true;
        String login = view.getUserLogin();
        String password = view.getUserPassword();
        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            view.showError(R.string.incorrect_login_or_password);
            view.failVibrate();
            return;
        }
        showLoadingState();
        byte[] data = new byte[0];
        try {
            data = (login + ":" + password).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        Disposable subscription = model.signIn("Basic " + base64)
                .subscribeOn(Schedulers.io())
                .flatMap(baseAuth -> model.getAutorization(Const.OAUTH_CLIENT_ID, "Basic " + base64, generateAuthBody()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            hideLoadingState();
                            StorageManager.getInstance().saveOAuthId(next.getId());
                            StorageManager.getInstance().saveToken(next.getToken());
                            requestLock = false;
                            view.authorizationComplete();
                        },
                        error -> {
                            hideLoadingState();
                            showError(R.string.incorrect_login_or_password);
                            view.failVibrate();
                            view.authorizationFailed();
                            requestLock = false;
                        },
                        () -> {
                            hideLoadingState();
                            requestLock = false;
                        });
        addDisposable(subscription);
    }

    private RequestBody generateAuthBody() {
        JSONObject body = new JSONObject();
        try {
            body.put("client_secret", Const.OAUTH_CLIENT_SECRET);
            body.put("note", "PClient");
            JSONArray scopes = new JSONArray();
            scopes.put("public_repo");
            scopes.put("user");
            scopes.put("repo");
            body.put("scopes", scopes);
            body.put("fingerprint", UUID.randomUUID().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RequestBody.create(MediaType.parse("application/json"), body.toString());
    }


}
