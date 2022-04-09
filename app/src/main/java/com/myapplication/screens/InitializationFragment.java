package com.myapplication.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.myapplication.R;
import com.myapplication.config.PreferenceManager;
import com.myapplication.databinding.FragmentInitializationBinding;

public class InitializationFragment extends BaseFragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentInitializationBinding binding = FragmentInitializationBinding.inflate(
                inflater, container, false
        );
        return binding.getRoot();
    }

    @Override
    void initFragment() {
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        boolean hasUser = preferenceManager.hasUser();

        NavController navController = NavHostFragment.findNavController(this);
        NavOptions.Builder builder = new NavOptions.Builder();
        builder.setPopUpTo(R.id.action_initialize, true);

        if (hasUser) {
            navController.navigate(R.id.action_main, null, builder.build());
        } else {
            navController.navigate(R.id.action_setup, null, builder.build());
        }
    }

}
