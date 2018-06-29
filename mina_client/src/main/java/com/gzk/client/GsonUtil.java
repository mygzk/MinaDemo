package com.gzk.client;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil {


    public static <T> T fromJson(String jsonStr, Type type) {
        if (jsonStr == null) {
            return null;
        }

        return new Gson().fromJson(jsonStr, type);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);

    }
}
