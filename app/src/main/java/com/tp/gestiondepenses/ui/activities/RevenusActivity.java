package com.tp.gestiondepenses.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.ui.fragments.RevenusFragment;

public class RevenusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenus);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.containerRevenus, new RevenusFragment());
            ft.commit();
        }
    }
}