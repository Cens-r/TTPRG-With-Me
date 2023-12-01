package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.UUID;

public class ClassManager {
    private static Hashtable<String, Hashtable<String, ClassManager>> classObjects = new Hashtable<>();
    public UUID uuid;

    // Constructor
    public ClassManager() {
        uuid = UUID.randomUUID();
    }

    // Starts tracking the given object
    protected static void trackObject(ClassManager object) {
        String className = object.getClass().getSimpleName();
        Hashtable<String, ClassManager> objectList;
        if (classObjects.containsKey(className)) {
            objectList = classObjects.get(className);
        } else {
            objectList = new Hashtable<>();
            classObjects.put(className, objectList);
        }
        objectList.put(object.uuid.toString(), object);
    }

    // Gets a hashtable of all the objects for a class
    public static Hashtable<String, ClassManager> getObjects(Class classRef) {
        String className = classRef.getSimpleName();
        return classObjects.get(className);
    }

    // Gets an object given its class and uuid
    public static ClassManager getObject(Class classRef, String uuid) {
        Hashtable<String, ClassManager> objectList = getObjects(classRef);
        if (objectList == null) { return null; }
        return objectList.get(uuid);
    }

    // Converts an object to Json String
    public String toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new ObservableFieldTypeAdapter());
        Gson gson = builder.create();
        return gson.toJson(this);
    }

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
}
