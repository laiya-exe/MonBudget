package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.adapter.CategorieAdapter;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Rubrique;
import com.tp.gestiondepenses.viewmodel.CategorieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCategoriesActivity extends AppCompatActivity {

    private CategorieViewModel viewModel;
    private CategorieAdapter adapterDefault, adapterCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_categories);

        viewModel = new ViewModelProvider(this).get(CategorieViewModel.class);
        initViews();
        observeData();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
        
        RecyclerView rvDefault = findViewById(R.id.rvDefaultCategories);
        rvDefault.setLayoutManager(new LinearLayoutManager(this));
        adapterDefault = new CategorieAdapter();
        rvDefault.setAdapter(adapterDefault);
        
        RecyclerView rvCustom = findViewById(R.id.rvCustomCategories);
        rvCustom.setLayoutManager(new LinearLayoutManager(this));
        adapterCustom = new CategorieAdapter();
        rvCustom.setAdapter(adapterCustom);
        
        setupAdapterListeners(adapterDefault);
        setupAdapterListeners(adapterCustom);

        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            startActivity(new Intent(this, FormulaireCategorieActivity.class));
        });
    }

    private void setupAdapterListeners(CategorieAdapter adapter) {
        adapter.setOnCategorieClickListener(new CategorieAdapter.OnCategorieClickListener() {
            @Override
            public void onEditClick(Categorie categorie) {
                Intent intent = new Intent(GestionCategoriesActivity.this, FormulaireCategorieActivity.class);
                intent.putExtra("CATEGORY_ID", categorie.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Categorie categorie) {
                new AlertDialog.Builder(GestionCategoriesActivity.this)
                        .setTitle(R.string.delete_confirmation_title)
                        .setMessage("Supprimer cette catégorie personnalisée ?")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            viewModel.delete(categorie, (success, message) -> {
                                runOnUiThread(() -> Toast.makeText(GestionCategoriesActivity.this, message, Toast.LENGTH_SHORT).show());
                            });
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }

            @Override
            public void onManageRubriquesClick(Categorie categorie) {
                showManageRubriquesDialog(categorie);
            }
        });
    }

    private void showManageRubriquesDialog(Categorie categorie) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_manage_rubriques, null);
        EditText etNewRubrique = view.findViewById(R.id.etNewRubrique);
        ListView lvRubriques = view.findViewById(R.id.lvRubriques);
        
        List<String> namesList = new ArrayList<>();
        ArrayAdapter<String> lvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namesList);
        lvRubriques.setAdapter(lvAdapter);

        viewModel.getRubriquesForCategorie(categorie.getId()).observe(this, rubriques -> {
            if (rubriques != null) {
                namesList.clear();
                for (Rubrique r : rubriques) {
                    namesList.add(r.getNom());
                }
                lvAdapter.notifyDataSetChanged();
                
                lvRubriques.setOnItemLongClickListener((parent, v, position, id) -> {
                    new AlertDialog.Builder(GestionCategoriesActivity.this)
                            .setMessage("Supprimer la rubrique '" + namesList.get(position) + "' ?")
                            .setPositiveButton("Oui", (d, w) -> {
                                viewModel.deleteRubrique(rubriques.get(position));
                            })
                            .setNegativeButton("Non", null)
                            .show();
                    return true;
                });
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Rubriques : " + categorie.getNom())
                .setView(view)
                .setPositiveButton("Fermer", null)
                .create();

        view.findViewById(R.id.btnAddRubrique).setOnClickListener(v -> {
            String name = etNewRubrique.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.insertRubrique(new Rubrique(categorie.getId(), name));
                etNewRubrique.setText("");
            } else {
                Toast.makeText(this, "Saisissez un nom", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void observeData() {
        viewModel.getCategoriesWithRubriques().observe(this, list -> {
            if (list != null) {
                adapterDefault.setCategories(list.stream()
                        .filter(c -> c.categorie.isEstDefaut())
                        .collect(Collectors.toList()));
                
                adapterCustom.setCategories(list.stream()
                        .filter(c -> !c.categorie.isEstDefaut())
                        .collect(Collectors.toList()));
            }
        });
    }
}