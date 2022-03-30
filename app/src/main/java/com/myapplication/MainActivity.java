package com.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.myapplication.data.LoggedTime;
import com.myapplication.database.AppDatabase;
import com.myapplication.databinding.ActivityMainBinding;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatabase();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main
        );
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public AppDatabase getDatabase() {
        return this.database;
    }

    private void initDatabase() {
        this.database = Room.databaseBuilder(
                getApplicationContext(), AppDatabase.class, "database"
        ).allowMainThreadQueries().build();

//        LoggedTime loggedTime = new LoggedTime();
//        loggedTime.setDate(LocalDate.now());
//        loggedTime.setWork(8);
//        loggedTime.setOvertime(8);
//        loggedTime.setSickLeave(8);
//        loggedTime.setUnpaidTimeOff(8);
//        loggedTime.setTimeOff(8);
//        this.database.loggedTimeDao().saveLoggedTime(loggedTime);
    }

}