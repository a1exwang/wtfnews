package com.ihandy.a2014011367.wtfnews.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable {
    public User(String f, String l) {
        this.firstName = f;
        this.lastName = l;
    }
    private String firstName;
    private String lastName;
    @Bindable
    public String getFirstName() {
        return this.firstName;
    }
    @Bindable
    public String getLastName() {
        return this.lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(com.ihandy.a2014011367.wtfnews.BR.firstName);
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(com.ihandy.a2014011367.wtfnews.BR.lastName);
    }
}
