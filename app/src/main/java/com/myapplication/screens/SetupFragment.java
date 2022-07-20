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

/**
 * Първоначалния екран който се показва когато няма данни за потребител
 */
public class SetupFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSetupBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // генериране на изгледа на екрана
        binding = FragmentSetupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    void initFragment() {
        // показва се клавиатурата
        Utils.showKeyBoard(binding.inputSalaryLayout);
        // информиране на това кога бутона "Продължи" е натиснат
        binding.buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_continue) return;

        // взимане на това какво е въведенео в полето за заплата
        String salaryInput = binding.inputSalary.getText().toString();
        // взимане на това какво е въведенео в полето за отпуска
        String ptoDaysInput = binding.inputPto.getText().toString();
        // проверка дали въведените данни не са празни
        boolean hasSalary = StringUtils.isNoneBlank(salaryInput);
        boolean hasPtoDays = StringUtils.isNoneBlank(ptoDaysInput);

        // проверява дали има въведена заплата
        // ако няма показва грешка
        if (!hasSalary) {
            String error = getString(R.string.error_input);
            binding.inputSalaryLayout.setError(error);
        }
        // проверява дали има въведени дни отпуск
        // ако няма показва грешка
        if (!hasPtoDays) {
            String error = getString(R.string.error_input);
            binding.inputPtoLayout.setError(error);
        }

        // ако има въведена заплата и почивни дни
        // се създава потребител и се навигира към основният екран
        if (hasSalary && hasPtoDays) {
            createUser(salaryInput, ptoDaysInput);
            navigateToApp();
        }
    }

    // фунцкия която създава потребителя спрям въведените заплата и почивни дни
    private void createUser(String salaryStr, String ptoDaysStr) {
        // заплата
        int salary = Integer.parseInt(salaryStr);
        // почивни дни
        int ptoDays = Integer.parseInt(ptoDaysStr);

        // генериране на потребител
        User user = new User(salary, ptoDays);
        // създаване на обект който ще пази инфомрацията за заплата и почивни дни
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        // запазване на потребителя
        preferenceManager.saveUser(user);
    }

    // фунцкяи която навигира към оснивния екран
    private void navigateToApp() {
        // намиране на обкета който се грижа за смяната на екраните
        NavController navController = NavHostFragment.findNavController(this);

        // навигиране към основния екран като се премахва текущият
        NavOptions.Builder builder = new NavOptions.Builder();
        builder.setPopUpTo(R.id.action_setup, true);
        navController.navigate(R.id.action_main, null, builder.build());
    }

}
