package ru.slybeaver.githubclient.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kbiakov.codeview.CodeView;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.presenter.FileContentPresenter;
import ru.slybeaver.githubclient.presenter.Presenter;

/**
 * Created by psinetron on 13.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class FileContentActivity extends BaseActivity implements FileContentView {

    private FileContentPresenter presenter = new FileContentPresenter(this);

    @BindView(R.id.code_view)
    CodeView code_view;
    @BindView(R.id.contextCoordinatorLayout)
    CoordinatorLayout contextCoordinatorLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.imageRadialPreloader)
    ImageView imageRadialPreloader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filecontent);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (presenter != null) {
            presenter.onCreate(getIntent());
        }
    }


    @Override
    public void showError(String error) {
        if (contextCoordinatorLayout != null) {
            Snackbar.make(contextCoordinatorLayout, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError(int id) {
        if (contextCoordinatorLayout != null) {
            Snackbar.make(contextCoordinatorLayout, getString(id), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoading() {
        showPreloader(imageRadialPreloader, code_view, this);
    }

    @Override
    public void hideLoading() {
        hidePreloader(imageRadialPreloader, code_view);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showCode(String code) {
        code_view.setCode(code);
    }

    @Override
    public void setToolBarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }
}
