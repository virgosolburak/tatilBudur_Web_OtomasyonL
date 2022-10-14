package com.virgosol.tatilBudur.helper;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class StoreHelper {

  private static Map<String, Object> valueMapList;
  public static StoreHelper INSTANCE;
  static {
    initValueInfo();
  }

  private StoreHelper() {
    //no instance
  }

  private static void initValueInfo() {
    Gson gson = new Gson();
    Map<String, String> elementInfoList;
    elementInfoList = (Map<String, String>) gson
        .fromJson(
            new InputStreamReader(StoreHelper.class.getResourceAsStream("/values.json")),
            Object.class);
    if (valueMapList == null) {
      valueMapList = new HashMap<>();
    }
    if (elementInfoList != null) {
      valueMapList.putAll(elementInfoList);
    }
  }

  public static void saveValue(String key, String value) {
    valueMapList.put(key, value);
  }

  public static String getValue(String key) {
    return valueMapList.get(key).toString();
  }

}
