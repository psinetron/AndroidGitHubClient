package ru.slybeaver.githubclient.model;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import ru.slybeaver.githubclient.model.dto.*;
import java.util.List;

/**
 * Created by psinetron on 09.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface BaseModel {

    Observable<MainUserDTO> signIn(String autorization);

    Observable<OAuthclientDTO> getAutorization(String client_id, String authorization, RequestBody body);

    Observable<MainUserDTO> confirmAuthorization(String authorization);

    Observable<List<RepositoryDTO>> getMyRepos(String authorization, boolean fromCache);

    Observable<SearchReposResultDTO> searchRepos(String body, boolean fromCache);

    Observable<List<RepositoryContentDTO>> getReposContent(String detailPath, String authorization, boolean fromCache);

    Observable<ResponseBody> getFileContent(String url, boolean fromCache);
}
