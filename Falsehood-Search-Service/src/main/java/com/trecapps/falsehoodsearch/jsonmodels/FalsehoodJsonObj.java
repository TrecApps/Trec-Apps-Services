package com.trecapps.falsehoodsearch.jsonmodels;

import org.json.JSONException;
import org.json.JSONObject;

public interface FalsehoodJsonObj
{
    JSONObject toJsonObject();

    void initializeFromJson(JSONObject obj) throws JSONException;
}
