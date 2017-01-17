package ru.slybeaver.githubclient.view;

import android.os.Bundle;
import ru.slybeaver.githubclient.model.dto.RepositoryDTO;
import java.util.List;

/**
 * Created by psinetron on 11.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface RepositoriesView extends BaseView {

    void setToolBarTitle(String title);

    void openRepoContent(Bundle args);

    void showRepositoriest(List<RepositoryDTO> listRepos);

}
