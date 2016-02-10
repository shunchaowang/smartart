package com.swang.smartart.manage.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by swang on 3/29/2015.
 * Wrapper class for Gson converter.
 * Single factory of Gson instance.
 */
public class JsonUtil {

    private static Gson gson = null;

    private JsonUtil() {
        gson = new GsonBuilder()
                // my null string converter does not work right
                // so just use serializeNulls from gson library
                //.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<String>())
                .serializeNulls()
                .setPrettyPrinting().create();
    }

    /**
     * Call this method to convert java object to json string.
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        synchronized (JsonUtil.class) {
            if (gson == null) {
                new JsonUtil();
            }
            return gson.toJson(object);
        }
    }

    /**
     * Call this method to obtain a Gson instance.
     *
     * @return
     */
    public static Gson gson() {
        synchronized (JsonUtil.class) {
            if (gson == null) {
                new JsonUtil();
            }
            return gson;
        }
    }


    @SuppressWarnings("unchecked")
    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    /**
     * Adapter to convert null string to empty.
     */
    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}
