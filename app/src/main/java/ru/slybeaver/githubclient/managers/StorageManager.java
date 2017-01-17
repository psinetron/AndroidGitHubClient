package ru.slybeaver.githubclient.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Stack;

/**
 * Created by psinetron on 10.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class StorageManager {

    private static SharedPreferences sPref = null;
    private static SharedPreferences.Editor editor = null;
    private Stack<String> pathQueye = new Stack<>();

    public String getToken() {
        if (sPref == null) {
            return "token ";
        }
        return "token " + sPref.getString("TOKEN", "");
    }

    public void saveToken(String token) {
        if (sPref == null) {
            return;
        }
        editor.putString("TOKEN", token);
        editor.commit();
    }

    public int getOAuthId() {
        if (sPref == null) {
            return 0;
        }
        return sPref.getInt("OAUTH", 0);
    }

    public void saveOAuthId(int oAuthId) {
        if (sPref == null) {
            return;
        }
        editor.putInt("OAUTH", oAuthId);
        editor.commit();
    }


    public synchronized void setStartPath(String startPath) {
        pathQueye = new Stack<>();
        pathQueye.push(startPath);
    }

    public synchronized String getBackPath() {
        if (pathQueye.size() == 0) {
            return null;
        }
        return pathQueye.pop();
    }

    public synchronized String getCurrentPath() {
        if (pathQueye.size() == 0) {
            return null;
        }
        return pathQueye.lastElement();
    }

    public synchronized void addNextPath(String nextPath) {
        pathQueye.push(nextPath);
    }

    /**
     * SingleTone pattern
     */
    private StorageManager(Context context) {
        if (context == null) {
            return;
        }
        sPref = context.getSharedPreferences("ProjectOptions", Context.MODE_PRIVATE);
        editor = sPref.edit();
    }

    private static StorageManager instance = null;

    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new StorageManager(context);
        }
    }

    public static StorageManager getInstance() {
        if (instance == null) {
            initInstance(null);
        }
        return instance;
    }

}
