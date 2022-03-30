package com.myapplication.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myapplication.MainActivity;
import com.myapplication.database.AppDatabase;
import com.myapplication.database.LoggedTimeDao;

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

    protected LoggedTimeDao getLoggedTimeDao() {
        assert getActivity() != null;
        return ((MainActivity) getActivity()).getDatabase().loggedTimeDao();
    }

}
