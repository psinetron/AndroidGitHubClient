package ru.slybeaver.githubclient.other;

import android.app.Application;
import android.content.Context;
import ru.slybeaver.githubclient.managers.CacheManager;
import ru.slybeaver.githubclient.managers.StorageManager;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initParams();
    }

    /**
     * Init main connectors, params and options of application
     */
    private void initParams() {
        StorageManager.initInstance(getApplicationContext()); //Init temporary storage
        CacheManager.initInstance();
    }

    public static Context getAppContext() {
        return context;
    }
}
