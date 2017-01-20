package ru.slybeaver.githubclient.model.api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;
import ru.slybeaver.githubclient.model.dto.*;

import java.util.List;

/**
 * Created by psinetron on 09.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public interface ApiInterface {

    @GET("/user")
    @Headers({"Content-Type: application/json", "Accept: application/vnd.github.full+json"})
    Observable<MainUserDTO> signIn(@Header("Authorization") String autorization);

    @PUT("/authorizations/clients/{client_id}")
    @Headers({"Content-Type: application/json", "Accept: application/vnd.github.full+json"})
    Observable<OAuthclientDTO> getAutorization(@Path("client_id") String client_id, @Header("Authorization") String authorization, @Body RequestBody body);

    @GET("/user")
    @Headers({"Content-Type: application/json", "Accept: application/vnd.github.full+json"})
    Observable<MainUserDTO> confirmAuthorization(@Header("Authorization") String authorization);

    @GET("/user/repos")
    @Headers({"Content-Type: application/json", "Accept: application/vnd.github.full+json"})
    Observable<List<RepositoryDTO>> getMyRepos(@Header("Authorization") String authorization);

    @GET("/search/repositories")
    @Headers({"Content-Type: application/json", "Accept: application/vnd.github.full+json"})
    Observable<SearchReposResultDTO> searchRepos(@Query("q") String body);

    @GET("/repos/{detail_path}")
    @Headers({"Content-Type: application/json", "Accept: application/vnd.github.full+json"})
    Observable<List<RepositoryContentDTO>> getReposContent(@Path(value = "detail_path", encoded = true) String detailPath, @Header("Authorization") String authorization);

    @GET
    Observable<ResponseBody> getFileContent(@Url String url);
}
