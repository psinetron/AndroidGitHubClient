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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.presenter.AuthorizationPresenter;
import ru.slybeaver.githubclient.presenter.Presenter;


public class AuthorizationActivity extends BaseActivity implements AuthorizationView {

    private AuthorizationPresenter presenter = new AuthorizationPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        Button signInButton = (Button) findViewById(R.id.signInButton);
        if (signInButton!=null){
            signInButton.setOnClickListener(v->{if (presenter!=null){presenter.authorizationClick();}});
        }
    }


    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void authorizationComplete() {
        ImageView imageLogo = (ImageView) findViewById(R.id.imageLogo);
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
        ImageView imageLogo = (ImageView) findViewById(R.id.imageLogo);
        if (imageLogo!=null){imageLogo.startAnimation(negativeAnimation);}
        EditText userPasswordTxt = (EditText) findViewById(R.id.userPasswordTxt);
        if (userPasswordTxt != null) {userPasswordTxt.setText("");}
    }

    @Override
    public void failVibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    @Override
    public String getUserLogin() {
        TextView userNameTxt = (TextView) findViewById(R.id.userNameTxt);
        if (userNameTxt!=null){return userNameTxt.getText().toString();}
        return null;
    }

    @Override
    public String getUserPassword() {
        TextView userPasswordTxt = (TextView) findViewById(R.id.userPasswordTxt);
        if (userPasswordTxt!=null){return userPasswordTxt.getText().toString();}
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
        ImageView imageRadialPreloader = (ImageView) findViewById(R.id.imageRadialPreloader);
        if (imageRadialPreloader != null) {
            imageRadialPreloader.setVisibility(View.VISIBLE);
            Animation loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.preloader_radial);
            imageRadialPreloader.startAnimation(loadingAnimation);
        }
    }

    @Override
    public void hideLoading() {
        ImageView imageRadialPreloader = (ImageView) findViewById(R.id.imageRadialPreloader);
        if (imageRadialPreloader != null) {
            imageRadialPreloader.setVisibility(View.INVISIBLE);
            imageRadialPreloader.clearAnimation();
        }
    }
}
