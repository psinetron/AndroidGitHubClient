package ru.slybeaver.githubclient.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.model.dto.RepositoryDTO;
import ru.slybeaver.githubclient.presenter.RepositoriesPresenter;
import java.util.List;

/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class RepositoryListAdapter extends RecyclerView.Adapter<RepositoryListAdapter.ViewHolder> {

    private List<RepositoryDTO> repositories = null;
    private Context context = null;
    private RepositoriesPresenter presenter;

    public RepositoryListAdapter(Context context, List<RepositoryDTO> repositories, RepositoriesPresenter presenter) {
        this.context = context;
        this.repositories = repositories;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_repository, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (repositories == null) {
            return;
        }
        final RepositoryDTO repository = repositories.get(position);
        holder.tvRepoName.setText(repository.getName());
        holder.tvRepoDescription.setText(repository.getDescription());
        holder.tvRepoInfo.setText(context.getString(R.string.repo_info_line,repository.getLanguage(), repository.getStargazersCount(), repository.getOwner().getLogin()));
        Picasso.with(context).load(repository.getOwner().getAvatarUrl()).resize(80, 80).into(holder.ivBuilderAva);
        holder.itemView.setOnClickListener(v -> presenter.clickRepo(repository));
    }

    @Override
    public int getItemCount() {
        if (repositories == null) {
            return 0;
        }
        return repositories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRepoName;
        private TextView tvRepoDescription;
        private TextView tvRepoInfo;
        private ImageView ivBuilderAva;

        ViewHolder(View itemView) {
            super(itemView);
            tvRepoName = (TextView) itemView.findViewById(R.id.tvRepoName);
            tvRepoDescription = (TextView) itemView.findViewById(R.id.tvRepoDescription);
            tvRepoInfo = (TextView) itemView.findViewById(R.id.tvRepoInfo);
            ivBuilderAva = (ImageView) itemView.findViewById(R.id.ivBuilderAva);
        }
    }
}
