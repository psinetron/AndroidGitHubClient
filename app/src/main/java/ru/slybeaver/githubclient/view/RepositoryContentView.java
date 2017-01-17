package ru.slybeaver.githubclient.view;

import android.os.Bundle;
import ru.slybeaver.githubclient.model.dto.RepositoryContentDTO;
import java.util.ArrayList;

/**
 * Created by psinetron on 12.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface RepositoryContentView extends BaseView {

    void backPress();

    void startActivity(Bundle args);

    void setToolBarTitle(String title);

    void showContent(ArrayList<RepositoryContentDTO> content, String path);

    void openContent(Bundle args, Class<?> cls);
}
