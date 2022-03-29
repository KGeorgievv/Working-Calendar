package com.myapplication.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

abstract class BaseFragment extends Fragment {

    abstract void initFragment();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragment();
    }

    protected void setTitle(String title) {
        if (getActivity() != null)
            this.getActivity().setTitle(title);
    }

}
