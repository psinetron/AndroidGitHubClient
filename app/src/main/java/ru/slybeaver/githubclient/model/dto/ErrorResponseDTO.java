package ru.slybeaver.githubclient.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by psinetron on 09.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class ErrorResponseDTO {
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
