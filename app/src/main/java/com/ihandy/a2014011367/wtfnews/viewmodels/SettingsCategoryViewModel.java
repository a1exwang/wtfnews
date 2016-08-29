package com.ihandy.a2014011367.wtfnews.viewmodels;

import com.ihandy.a2014011367.wtfnews.models.Category;

public class SettingsCategoryViewModel {
    public final Category category;
    public boolean getVisible() {
        return category.getVisible();
    }
    public void setVisible(boolean visible) {
        category.setVisible(visible);
    }

    public SettingsCategoryViewModel(Category c) {
        this.category = c;
    }

}
