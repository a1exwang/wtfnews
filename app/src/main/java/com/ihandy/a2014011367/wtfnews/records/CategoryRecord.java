package com.ihandy.a2014011367.wtfnews.records;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class CategoryRecord extends SugarRecord {
    @Unique
    String key;
    String name;

    public CategoryRecord() {

    }
    public CategoryRecord(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public String getKey() {
        return this.key;
    }
}
