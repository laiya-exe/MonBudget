package com.tp.gestiondepenses.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tp.gestiondepenses.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View topBar = findViewById(R.id.layout_top_bar);
        TextView tvTitle = findViewById(R.id.tv_title);
        View ivSettings = findViewById(R.id.iv_settings);
        View ivBack = findViewById(R.id.iv_back);

        ViewCompat.setOnApplyWindowInsetsListener(topBar, (view, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            view.setPadding(view.getPaddingLeft(), statusBarHeight, view.getPaddingRight(), view.getPaddingBottom());
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Navigation vers les paramètres
        ivSettings.setOnClickListener(v -> navController.navigate(R.id.navigation_settings));

        // Action du bouton retour
        ivBack.setOnClickListener(v -> navController.navigateUp());

        // Listener pour adapter la TopBar selon la destination
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_settings) {
                tvTitle.setText("Paramètres");
                ivSettings.setVisibility(View.GONE);
                ivBack.setVisibility(View.VISIBLE);
                bottomNav.setVisibility(View.GONE); // Masquer la barre du bas dans les réglages comme sur l'image
            } else {
                tvTitle.setText("Bonjour");
                ivSettings.setVisibility(View.VISIBLE);
                ivBack.setVisibility(View.GONE);
                bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }
}