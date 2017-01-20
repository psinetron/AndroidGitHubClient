package ru.slybeaver.githubclient.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.presenter.Presenter;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract Presenter getPresenter();

    @Override
    public void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().onStop();

        }
    }

    void showPreloader(View imageRadialPreloader, View recyclerView, Context context) {
        if (imageRadialPreloader == null || recyclerView == null || context == null) {
            return;
        }
        recyclerView.setVisibility(View.GONE);
        imageRadialPreloader.setVisibility(View.VISIBLE);
        Animation loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.preloader_radial);
        imageRadialPreloader.startAnimation(loadingAnimation);
    }

    void hidePreloader(View imageRadialPreloader, View recyclerView) {
        if (imageRadialPreloader != null) {
            imageRadialPreloader.setVisibility(View.INVISIBLE);
            imageRadialPreloader.clearAnimation();
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
