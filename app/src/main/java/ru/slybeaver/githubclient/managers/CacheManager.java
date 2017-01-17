package ru.slybeaver.githubclient.managers;

import ru.slybeaver.githubclient.other.BaseApplication;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by psinetron on 12.01.2017.
 * http://slybeaver.ru
 * slybeaver@slybeaver.ru
 */
public class CacheManager {

    private static final int CACHE_LIFE = 6 * 60 * 60 * 1000; //Life time of cache files (6 hours)

    public void setCache(String apiName, String urlParams, Object object) {
        if (apiName == null && urlParams == null && object == null) {
            return;
        }
        String path = apiName.replaceAll("[^a-zA-Z0-9" + File.separator + "]", "-");
        String fileName = md5(urlParams) + ".pgh";
        File cacheDir = new File(BaseApplication.getAppContext().getCacheDir().toString() + File.separator + path);
        cacheDir.mkdirs();
        File cacheFile = new File(cacheDir, fileName);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(cacheFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getCache(String apiName, String urlParams) {
        String path = apiName.replaceAll("[^a-zA-Z0-9" + File.separator + "]", "-");
        String fileName = md5(urlParams) + ".pgh";
        File cacheDir = new File(BaseApplication.getAppContext().getCacheDir().toString() + File.separator + path);
        File cacheFile = new File(cacheDir, fileName);
        if (!cacheFile.exists()) {
            return null;
        }
        if (System.currentTimeMillis() - cacheFile.lastModified() >= CACHE_LIFE) {
            cacheFile.delete();
            return null;
        }
        if (!cacheFile.exists()) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream oin = null;
        try {
            fis = new FileInputStream(cacheFile);
            oin = new ObjectInputStream(fis);
            return oin.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (oin != null) {
                    oin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SingleTone pattern
     */
    private static CacheManager instance = null;

    public static void initInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

}
