package ru.slybeaver.githubclient.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.presenter.Presenter;
import ru.slybeaver.githubclient.presenter.SplashScreenPresenter;

/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class SplashScreenActivity extends BaseActivity implements SplashScreenView {

    private SplashScreenPresenter presenter = new SplashScreenPresenter(this);

    private ImageView imageRadialPreloader = null;
    private ImageView imageLogo = null;
    private RelativeLayout SplashScreenRelativeLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageRadialPreloader = (ImageView) findViewById(R.id.imageRadialPreloader);
        imageLogo = (ImageView) findViewById(R.id.imageLogo);
        SplashScreenRelativeLayout = (RelativeLayout) findViewById(R.id.SplashScreenRelativeLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void showError(String error) {
        if (SplashScreenRelativeLayout==null){return;}
        Snackbar.make(SplashScreenRelativeLayout, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int id) {
        if (SplashScreenRelativeLayout==null){return;}
        Snackbar.make(SplashScreenRelativeLayout,
                getString(id),
                Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.retry_connection),
                view -> {
                    if (presenter != null) {
                        presenter.confirmAuthorization();
                    }
                }).show();
    }

    @Override
    public void showLoading() {
        if (imageRadialPreloader != null) {
            Animation loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.preloader_radial);
            imageRadialPreloader.startAnimation(loadingAnimation);
        }
    }

    @Override
    public void hideLoading() {
        if (imageRadialPreloader != null) {
            imageRadialPreloader.clearAnimation();
        }
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
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageLogo, "main_logo");
        Bundle bundle = options.toBundle();
        Intent intent = new Intent(this, AuthorizationActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
        finish();
    }


}
