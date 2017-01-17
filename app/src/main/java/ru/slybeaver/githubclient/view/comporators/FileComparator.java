package ru.slybeaver.githubclient.view.comporators;

import ru.slybeaver.githubclient.model.dto.RepositoryContentDTO;

import java.util.Comparator;

/**
 * Created by psinetron on 12.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class FileComparator implements Comparator<RepositoryContentDTO> {
    @Override
    public int compare(RepositoryContentDTO lhs, RepositoryContentDTO rhs) {

        if (lhs.getType().equalsIgnoreCase("dir") && !rhs.getType().equalsIgnoreCase("dir")) {
            return -1;
        } else if (!lhs.getType().equalsIgnoreCase("dir") && rhs.getType().equalsIgnoreCase("dir")) {
            return 1;
        }
        return lhs.getName().compareToIgnoreCase(rhs.getName());
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
