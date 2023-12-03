package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import java.util.Hashtable;
import java.util.UUID;

public class ClassArchetype extends ClassManager {
    public static Hashtable<String, Integer> ClassMap = new Hashtable<>();

    String uuid;
    String name;

    public ClassArchetype(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
        AddMapValue(uuid, this.id);
    }
    public ClassArchetype uuid(String uuid) {
        this.uuid = uuid;
        AddMapValue(uuid, this.id);
        return this;
    }

    public static void AddMapValue(String uuid, Integer id) {
        ClassMap.put(uuid, id);
    }
    public static void RemoveMapValue(String uuid) {
        ClassMap.remove(uuid);
    }
    public static Integer GetMapValue(String uuid) {
        if (ClassMap == null) { ClassMap = new Hashtable<>(); }
        return ClassMap.get(uuid);
    }
    public static boolean CheckClassExists(String uuid) {
        if (ClassMap == null) { ClassMap = new Hashtable<>(); }
        return ClassMap.containsKey(uuid);
    }
}
