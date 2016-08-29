package com.ihandy.a2014011367.wtfnews;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihandy.a2014011367.wtfnews.databinding.FragmentSettingsBinding;
import com.ihandy.a2014011367.wtfnews.viewmodels.SettingsViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends Fragment {

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, null, false);
        binding.setSettingsViewModel(new SettingsViewModel());
        return binding.getRoot();
    }
}
