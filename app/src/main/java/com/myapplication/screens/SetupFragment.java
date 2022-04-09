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
import com.myapplication.Utils;
import com.myapplication.config.PreferenceManager;
import com.myapplication.data.User;
import com.myapplication.databinding.FragmentSetupBinding;

import org.apache.commons.lang3.StringUtils;

public class SetupFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSetupBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSetupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    void initFragment() {
        Utils.showKeyBoard(binding.inputSalaryLayout);
        binding.buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_continue) return;

        String salaryInput = binding.inputSalary.getText().toString();
        String ptoDaysInput = binding.inputPto.getText().toString();
        boolean hasSalary = StringUtils.isNoneBlank(salaryInput);
        boolean hasPtoDays = StringUtils.isNoneBlank(ptoDaysInput);

        if (!hasSalary) {
            String error = getString(R.string.error_input);
            binding.inputSalaryLayout.setError(error);
        }
        if (!hasPtoDays) {
            String error = getString(R.string.error_input);
            binding.inputPtoLayout.setError(error);
        }

        if (hasSalary && hasPtoDays) {
            createUser(salaryInput, ptoDaysInput);
            navigateToApp();
        }
    }

    private void createUser(String salaryStr, String ptoDaysStr) {
        int salary = Integer.parseInt(salaryStr);
        int ptoDays = Integer.parseInt(ptoDaysStr);

        User user = new User(salary, ptoDays);
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        preferenceManager.saveUser(user);
    }

    private void navigateToApp() {
        NavController navController = NavHostFragment.findNavController(this);

        NavOptions.Builder builder = new NavOptions.Builder();
        builder.setPopUpTo(R.id.action_setup, true);
        navController.navigate(R.id.action_main, null, builder.build());
    }

}
