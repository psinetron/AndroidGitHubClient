package ru.slybeaver.githubclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.model.dto.RepositoryContentDTO;
import ru.slybeaver.githubclient.presenter.Presenter;
import ru.slybeaver.githubclient.presenter.RepositoryContentPresenter;
import ru.slybeaver.githubclient.view.adapters.RepositoryContentListAdapter;

import java.util.ArrayList;

/**
 * Created by psinetron on 12.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class RepositoryContentActivity extends BaseActivity implements RepositoryContentView {

    private RepositoryContentPresenter presenter = new RepositoryContentPresenter(this);
    @BindView(R.id.contextCoordinatorLayout)
    CoordinatorLayout contextCoordinatorLayout;
    @BindView(R.id.contentRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.imageRadialPreloader)
    ImageView imageRadialPreloader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
        }
        if (presenter != null) {
            presenter.onCreate(getIntent());
            swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        }
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenter.onNewIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();

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
        showPreloader(imageRadialPreloader, recyclerView, this);
    }

    @Override
    public void hideLoading() {
        hidePreloader(imageRadialPreloader, recyclerView);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void backPress() {
        super.onBackPressed();
    }

    @Override
    public void startActivity(Bundle args) {
        Intent intent = new Intent(this, RepositoryContentActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void setToolBarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void showContent(ArrayList<RepositoryContentDTO> content, String path) {
        recyclerView.setAdapter(new RepositoryContentListAdapter(this, content, path, presenter));
    }

    @Override
    public void openContent(Bundle args, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(args);
        startActivity(intent);
    }


}
