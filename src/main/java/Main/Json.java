package Main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Robot.Robot;
import Main.gson.Gson;
import Main.gson.GsonBuilder;
import Main.gson.JsonElement;
import Main.gson.JsonObject;

public class Json {

    public static void writeToJson(String filename, List<JsonObject> jsonObjects) {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (JsonObject i : jsonObjects) {
                gson.toJson(i, fileWriter);
            }
            fileWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error Writing " + filename + ": " + e.getMessage());
        }
    }

    public static void writer(String filename, List<Map<String, Object>> clientData) {
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Map<String, Object> i : clientData) {
            JsonObject jsonObject = new JsonObject();
            for(Map.Entry<String, Object> j : i.entrySet()) {
                if (j.getKey().equals("robot")) {
                    Object object = i.get("robot");
                    if (!object.equals("")) {
                        Robot robot = (Robot) object;
                        jsonObject.addProperty("name", robot.getName());
                        jsonObject.addProperty("ammo", robot.getAmmo());
                        jsonObject.addProperty("status", robot.getStatus());
                        jsonObject.addProperty("health", robot.getHealth());
                        jsonObject.addProperty("position" ,robot.getPosition().toString());
                        jsonObject.addProperty("direction" ,robot.getDirection().toString());
                        jsonObject.addProperty("speciality", robot.getSpeciality().toString());
                    }
                    else
                        jsonObject.addProperty("robot","");
                }
                else
                    jsonObject.addProperty(j.getKey(), j.getValue().toString());
            }
            jsonObjects.add(jsonObject);
        }
        writeToJson(filename, jsonObjects);
    }

    public static Map<String, Object> reader(String fileName, String jsonString) {
        Gson gson = new Gson();
        List<JsonObject> jsonObjects = new ArrayList<>();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        jsonObjects.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            Object javaObject = gson.fromJson(value, Object.class);
            map.put(key, javaObject);
        }
        writeToJson(fileName, jsonObjects);
        return map;
    }
}