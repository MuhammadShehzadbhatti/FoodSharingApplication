package com.example.foodsharingapplication.authentication.AuthemnticationFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodsharingapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExtraProfileInfo extends Fragment {


    public AddExtraProfileInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_extra_profile_info, container, false);
    }

}
