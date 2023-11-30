package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import com.google.gson.Gson;

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
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
