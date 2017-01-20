package ru.slybeaver.githubclient.model;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import ru.slybeaver.githubclient.model.api.ApiInterface;
import ru.slybeaver.githubclient.model.api.ApiModule;
import ru.slybeaver.githubclient.model.dto.*;
import ru.slybeaver.githubclient.managers.CacheManager;
import ru.slybeaver.githubclient.other.Const;
import java.util.List;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class BaseModelImpl implements BaseModel {
    private ApiInterface apiInterface = ApiModule.getApiInterface(Const.GIT_BASE_URL);

    public BaseModelImpl() {
    }

    @Override
    public Observable<MainUserDTO> signIn(String autorization) {
        return apiInterface.signIn(autorization);
    }

    @Override
    public Observable<OAuthclientDTO> getAutorization(String client_id, String authorization, RequestBody body) {
        return apiInterface.getAutorization(client_id, authorization, body);
    }

    @Override
    public Observable<MainUserDTO> confirmAuthorization(String authorization) {
        return apiInterface.confirmAuthorization(authorization);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<RepositoryDTO>> getMyRepos(String authorization, boolean fromCache) {
        if (fromCache) {
            try {
                List<RepositoryDTO> cachedData = (List<RepositoryDTO>) CacheManager.getInstance().getCache("getMyRepos", authorization);
                if (cachedData != null) {
                    return Observable.just(cachedData);
                }
            } catch (Exception ignored) {
            }
        }
        return apiInterface.getMyRepos(authorization);
    }

    @Override
    public Observable<SearchReposResultDTO> searchRepos(String body, boolean fromCache) {
        if (fromCache) {
            try {
                SearchReposResultDTO cachedData = (SearchReposResultDTO) CacheManager.getInstance().getCache("searchRepos", body);
                if (cachedData != null) {
                    return Observable.just(cachedData);
                }
            } catch (Exception ignored) {
            }
        }
        return apiInterface.searchRepos(body);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<RepositoryContentDTO>> getReposContent(String detailPath, String authorization, boolean fromCache) {
        if (fromCache) {
            try {
                List<RepositoryContentDTO> cachedData = (List<RepositoryContentDTO>) CacheManager.getInstance().getCache("getReposContent", detailPath + authorization);
                if (cachedData != null) {
                    return Observable.just(cachedData);
                }
            } catch (Exception ignored) {
            }
        }
        return apiInterface.getReposContent(detailPath, authorization);
    }

    @Override
    public Observable<ResponseBody> getFileContent(String url, boolean fromCache) {
        if (fromCache) {
            try {
                ResponseBody cachedData = (ResponseBody) CacheManager.getInstance().getCache("getFileContent", url);
                if (cachedData != null) {
                    return Observable.just(cachedData);
                }
            } catch (Exception ignored) {
            }
        }
        return apiInterface.getFileContent(url);
    }

}
