package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ClassManager {
    private static final Hashtable<String, List<ClassManager>> classObjects = new Hashtable<>();
    private static int currentIndex = 0;

    @Expose(serialize = false, deserialize = false)
    public int id;

    // Constructor
    public ClassManager() {
        id = -1;
    }

    // Starts tracking the given object
    public static void trackObject(ClassManager object) {
        String className = object.getClass().getSimpleName();
        List<ClassManager> objectList;
        if (classObjects.containsKey(className)) {
            objectList = classObjects.get(className);
        } else {
            objectList = new ArrayList<>();
            classObjects.put(className, objectList);
        }
        assert objectList != null;
        objectList.add(object);
        object.id = currentIndex;
        currentIndex++;
    }
    public static void untrackObject(ClassManager object) {
        String className = object.getClass().getSimpleName();
        if (object.id == -1) { return; }
        if (!classObjects.containsKey(className)) { return; }
        List<ClassManager> objectList = classObjects.get(className);
        for (int i = object.id + 1; i < objectList.size(); i++) {
            objectList.get(i).id = i - 1;
        }
        objectList.remove(object.id);
    }

    // Gets a hashtable of all the objects for a class
    public static List<ClassManager> getObjects(Class<?> classRef) {
        String className = classRef.getSimpleName();
        return classObjects.get(className);
    }

    // Gets an object given its class and uuid
    public static ClassManager getObject(Class<?> classRef, int id) {
        List<ClassManager> objectList = getObjects(classRef);
        if (objectList == null) { return null; }
        return objectList.get(id);
    }

    // Converts an object to Json String
    public String toJson() {
        //GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapterFactory(new ObservableFieldTypeAdapter());
        //Gson gson = builder.create();
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T> T fromJson(String json, Class<T> c) {
        //GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapterFactory(new ObservableFieldTypeAdapter());
        //Gson gson = builder.create();
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    /*
    private static class ObservableFieldTypeAdapter implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() != ObservableField.class) { return null; }
            Type param = ((ParameterizedType) type.getType()).getActualTypeArguments()[0];
            return (TypeAdapter<T>) new TypeAdapter<ObservableField<?>>() {

                @Override
                public void write(JsonWriter out, ObservableField<?> value) throws IOException {
                    gson.toJson(value.get(), param, out);
                }

                @Override
                public ObservableField<?> read(JsonReader in) {
                    return new ObservableField<>(gson.fromJson(in, param));
                }
            };
        }
    }
    */
}
