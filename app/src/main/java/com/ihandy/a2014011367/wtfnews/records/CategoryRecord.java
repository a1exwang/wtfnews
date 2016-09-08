package com.ihandy.a2014011367.wtfnews.records;

import com.ihandy.a2014011367.wtfnews.models.Category;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

public class CategoryRecord extends SugarRecord {
    @Unique
    String key;
    String name;
    int priority = 0;

    public CategoryRecord() {

    }
    public CategoryRecord(String key, String name) {
        this.key = key;
        this.name = name;
        this.priority = 0;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return this.key;
    }
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int p) {
        this.priority = p;
    }

    public Category toCategory() {
        Category category = new Category(key, name);
        category.setPriority(priority);
        return category;
    }

    static public CategoryRecord findOrCreateByKey(String key, String name) {
        return findOrCreateByKey(key, name, new boolean[1]);
    }
    static public CategoryRecord findOrCreateByKey(String key, String name, boolean[] created) {
        List<CategoryRecord> result = CategoryRecord.find(CategoryRecord.class, "key = ?", key);
        if (result.size() == 0) {
            CategoryRecord record = new CategoryRecord(key, name);
            record.save();
            created[0] = true;
            return record;
        }
        else {
            CategoryRecord cr = result.get(0);
            cr.setName(name);
            cr.save();
            created[0] = false;
            return cr;
        }
    }
}
