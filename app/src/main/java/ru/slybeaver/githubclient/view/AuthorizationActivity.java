package ru.slybeaver.githubclient.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.presenter.AuthorizationPresenter;
import ru.slybeaver.githubclient.presenter.Presenter;

public class AuthorizationActivity extends BaseActivity implements AuthorizationView {

    private AuthorizationPresenter presenter = new AuthorizationPresenter(this);

    @BindView(R.id.imageLogo)
    ImageView imageLogo;
    @BindView(R.id.userPasswordTxt)
    EditText userPasswordTxt;
    @BindView(R.id.userNameTxt)
    EditText userNameTxt;
    @BindView(R.id.imageRadialPreloader)
    ImageView imageRadialPreloader;


    @OnClick(R.id.signInButton)
    void signIn() {
        if (presenter != null) {
            presenter.authorizationClick();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
    }


    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void authorizationComplete() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageLogo, "main_logo");
        Bundle bundle = options.toBundle();
        Intent intent = new Intent(this, RepositoriesActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void authorizationFailed() {
        Animation negativeAnimation = AnimationUtils.loadAnimation(this, R.anim.negative_action);
        if (imageLogo != null) {
            imageLogo.startAnimation(negativeAnimation);
        }
        if (userPasswordTxt != null) {
            userPasswordTxt.setText("");
        }
    }

    @Override
    public void failVibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    @Override
    public String getUserLogin() {
        if (userNameTxt != null) {
            return userNameTxt.getText().toString();
        }
        return null;
    }

    @Override
    public String getUserPassword() {
        if (userPasswordTxt != null) {
            return userPasswordTxt.getText().toString();
        }
        return null;
    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(R.id.autorizarionRelativeLayout), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int id) {
        Snackbar.make(findViewById(R.id.autorizarionRelativeLayout), getString(id), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        if (imageRadialPreloader != null) {
            imageRadialPreloader.setVisibility(View.VISIBLE);
            Animation loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.preloader_radial);
            imageRadialPreloader.startAnimation(loadingAnimation);
        }
    }

    @Override
    public void hideLoading() {
        if (imageRadialPreloader != null) {
            imageRadialPreloader.setVisibility(View.INVISIBLE);
            imageRadialPreloader.clearAnimation();
        }
    }
}
