package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ClassManager {
    private static final Hashtable<String, List<ClassManager>> classObjects = new Hashtable<>();
    private static final Hashtable<String, Integer> currentIndexes = new Hashtable<>();

    public transient int id;

    // Constructor
    public ClassManager() {
        String className = getClass().getSimpleName();
        Integer currentIndex = 0;
        if (currentIndexes.containsKey(className)) {
            currentIndex = currentIndexes.get(className);
        }
        currentIndex++;
        currentIndexes.put(className, currentIndex);
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
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T> T fromJson(String json, Class<T> c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }
}
