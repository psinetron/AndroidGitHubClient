package ru.slybeaver.githubclient.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.SearchView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.model.dto.RepositoryDTO;
import ru.slybeaver.githubclient.presenter.Presenter;
import ru.slybeaver.githubclient.presenter.RepositoriesPresenter;
import ru.slybeaver.githubclient.view.adapters.RepositoryListAdapter;

import java.util.List;

/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class RepositoriesActivity extends BaseActivity implements RepositoriesView {

    private RepositoriesPresenter presenter = new RepositoriesPresenter(this);
    @BindView(R.id.reposCoordinatorLayout)
    CoordinatorLayout reposCoordinatorLayout;
    @BindView(R.id.ReposRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageRadialPreloader)
    ImageView imageRadialPreloader = null;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);
        ButterKnife.bind(this);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.repo_title));
        }
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
        }

        if (presenter != null) {
            presenter.onCreate(savedInstanceState, getIntent());
            swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            presenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (presenter != null) {
            presenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (menu.findItem(R.id.menu_search) != null) {
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (presenter != null) {
            presenter.onNewIntent(intent);
        }
    }


    @Override
    public void setToolBarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }

        if (appBar != null) {
            appBar.setExpanded(false);
        }
    }

    @Override
    public void openRepoContent(Bundle args) {
        Intent intent = new Intent(this, RepositoryContentActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void showRepositoriest(List<RepositoryDTO> listRepos) {
        recyclerView.setAdapter(new RepositoryListAdapter(this, listRepos, presenter));
    }


    @Override
    public void showError(String error) {
        if (reposCoordinatorLayout != null) {
            Snackbar.make(reposCoordinatorLayout, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError(int id) {
        if (reposCoordinatorLayout != null) {
            Snackbar.make(reposCoordinatorLayout, getString(id), Snackbar.LENGTH_LONG).show();
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
}
