package com.ihandy.a2014011367.wtfnews.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.ihandy.a2014011367.wtfnews.models.Category;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CategoryJSONDeserializer implements JsonDeserializer<Collection<Category>> {
    @Override
    public Collection<Category> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ArrayList<Category> ret = new ArrayList<>();
        JsonObject categories = jsonObject.get("data").getAsJsonObject().get("categories").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : categories.entrySet()) {
            JsonObject jo = new JsonObject();
            String key = entry.getKey();
            String name = entry.getValue().getAsString();
            jo.add("key", new JsonPrimitive(key));
            jo.add("name", new JsonPrimitive(name));

            context.deserialize(jo, typeOfT);
            Category c = new Category(key, name); ret.add(c);
            ret.add(c);
        }
        return ret;
    }
    public static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(Category.class, new CategoryJSONDeserializer()).create();
    }

    public static class CategoryItemJSONDeserializer implements JsonDeserializer<Category> {
        @Override
        public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            return null;
        }
    }
}
