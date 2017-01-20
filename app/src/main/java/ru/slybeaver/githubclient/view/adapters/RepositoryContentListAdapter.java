package ru.slybeaver.githubclient.view.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ru.slybeaver.githubclient.gitclientrxjava.R;
import ru.slybeaver.githubclient.model.dto.RepositoryContentDTO;
import ru.slybeaver.githubclient.presenter.RepositoryContentPresenter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by psinetron on 12.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class RepositoryContentListAdapter extends RecyclerView.Adapter<RepositoryContentListAdapter.ViewHolder> {

    private List<RepositoryContentDTO> reposContent = null;
    private Context context = null;
    private String currentPath = "";
    private RepositoryContentPresenter presenter;


    public RepositoryContentListAdapter(Context context, List<RepositoryContentDTO> repositories, String currentPath, RepositoryContentPresenter presenter) {
        this.context = context;
        this.reposContent = repositories;
        this.currentPath = currentPath;
        this.presenter = presenter;
    }

    @Override
    public RepositoryContentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_repositorycontent, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (reposContent == null) {
            return;
        }
        holder.tvFileName.setText(reposContent.get(position).getName());
        if (reposContent.get(position).getSize() > 0) {
            holder.tvSize.setText(sizeToBytes(reposContent.get(position).getSize()));
        }
        if (reposContent.get(position).getType().equalsIgnoreCase("dir")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ivType.setImageDrawable(context.getDrawable(R.drawable.dir_ico));
            } else {
                holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.dir_ico));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ivType.setImageDrawable(context.getDrawable(R.drawable.file_ico));
            } else {
                holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.file_ico));
            }
        }
        final RepositoryContentDTO repository = reposContent.get(position);
        if (repository.getType().equalsIgnoreCase("dir")) {
            holder.itemView.setOnClickListener(v -> presenter.clickRepositoryContent(currentPath + "/" + repository.getName()));
        } else if (repository.getDownloadUrl() != null) {
            holder.itemView.setOnClickListener(v -> presenter.clickFile(repository.getDownloadUrl()));
        }
    }

    @Override
    public int getItemCount() {
        if (reposContent == null) {
            return 0;
        }
        return reposContent.size();
    }

    /**
     * Get KBytes, MBytes etc from bytes
     *
     * @param inBytes size in bytes
     * @return String converted size
     */
    private String sizeToBytes(double inBytes) {
        ArrayList<String> sizesArray = new ArrayList<>(6);
        sizesArray.add(context.getString(R.string.bytes));
        sizesArray.add(context.getString(R.string.kbytes));
        sizesArray.add(context.getString(R.string.mbytes));
        sizesArray.add(context.getString(R.string.gbytes));
        sizesArray.add(context.getString(R.string.tbytes));
        sizesArray.add(context.getString(R.string.pbytes));

        int sizeLen = 0;
        while (inBytes / 1024 >= 1) {
            inBytes /= 1024;
            sizeLen++;
        }
        if (sizeLen > sizesArray.size()) {
            return "";
        }
        if (sizeLen > 0) {
            return new DecimalFormat("#0.00").format(inBytes) + " " + sizesArray.get(sizeLen);
        }
        return inBytes + " " + sizesArray.get(sizeLen);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;
        private TextView tvSize;
        private ImageView ivType;

        ViewHolder(View itemView) {
            super(itemView);
            tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
        }
    }
}
