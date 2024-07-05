package com.example.teamtrack;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class settings extends Fragment {
    WidgetFactory mWidgetFactory;

    public settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mWidgetFactory = new WidgetFactory(view, getParentFragmentManager(), getContext(), getActivity() );
        mWidgetFactory.CreateSettingsWidgets();
        return view;
    }
}