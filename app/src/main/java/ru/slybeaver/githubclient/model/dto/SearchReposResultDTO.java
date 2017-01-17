package ru.slybeaver.githubclient.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class SearchReposResultDTO implements Serializable{
    @SerializedName("items")
    @Expose
    private List<RepositoryDTO> items = null;

    public void setItems(List<RepositoryDTO> items) {
        this.items = items;
    }

    public List<RepositoryDTO> getItems() {
        return items;
    }
}
