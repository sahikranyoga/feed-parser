package com.sahikran.model;

import java.util.List;

public interface Item {
    String getTitle();
    String getDescription();
    String getLink();
    List<String> getCreator();
    String getPubDate();
    String getGuid();
}
