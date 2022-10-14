package com.virgosol.tatilBudur.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.virgosol.tatilBudur.model.ElementInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

  public class ElementHelper {
  private static final String DEFAULT_DIRECTORY_PATH = "/elements-values";
  private static Map<String, ElementInfo> elementMapList;

  static {
   initMap(getFileList());
  }
  private ElementHelper() {
    //no instance
  }

  public static By getElementInfoToBy(ElementInfo elementInfo) {
    By by = null;
    if (elementInfo.getType().equals("css")) {
      by = By.cssSelector(elementInfo.getValue());
    } else if (elementInfo.getType().equals("id")) {
      by = By.id(elementInfo.getValue());
    } else if (elementInfo.getType().equals("xpath")) {
      by = By.xpath(elementInfo.getValue());
    }
    return by;
  }

  public static ElementInfo getElementInfo(String key) {
    return elementMapList.get(key);
  }
  public static By getElementInfoToBy(String key) {
    return getElementInfoToBy(getElementInfo(key));
  }
  private static File[] getFileList() {
    File[] fileList = new File(
            ElementHelper.class.getResource(DEFAULT_DIRECTORY_PATH).getFile())
            .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));

    if (fileList == null) {
      throw new NullPointerException();
    }
    return fileList;
  }
  private static void initMap(File[] fileList) {
    elementMapList = new HashMap<>();
    Type elementType = new TypeToken<List<ElementInfo>>() {
    }.getType();
    Gson gson = new Gson();
    List<ElementInfo> elementInfoList = null;
    for (File file : fileList) {
      try {
        elementInfoList = gson
                .fromJson(new FileReader(file), elementType);
        elementInfoList.stream()
                .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}