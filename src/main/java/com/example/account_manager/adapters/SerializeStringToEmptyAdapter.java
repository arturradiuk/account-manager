package com.example.account_manager.adapters;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;

public class SerializeStringToEmptyAdapter implements JsonbAdapter<String, JsonValue> {

    @Override
    public JsonValue adaptToJson(String s) throws Exception {
        return Json.createValue("");
    }

    @Override
    public String adaptFromJson(JsonValue jsonValue) throws Exception {
        return jsonValue.toString();
    }
}
