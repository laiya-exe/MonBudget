package com.tp.gestiondepenses.ui.activities;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.viewmodel.CategorieViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormulaireCategorieActivity extends AppCompatActivity {

    private CategorieViewModel viewModel;
    private EditText etNom;
    private RecyclerView rvIcons, rvColors, rvRubriques;
    private RubriqueAdapter rubricAdapter;
    private final List<String> rubriquesNames = new ArrayList<>();
    
    private String selectedColorStr = "#1C35C9";
    private String selectedIconName = "ic_category";
    
    private int categoryId = -1;
    private boolean isEditMode = false;
    private Categorie existingCategorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_categorie);

        viewModel = new ViewModelProvider(this).get(CategorieViewModel.class);
        
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        isEditMode = (categoryId != -1);

        initViews();
        setupPickers();
        
        if (isEditMode) {
            loadExistingData();
        }
    }

    private void initViews() {
        etNom = findViewById(R.id.etNom);
        rvIcons = findViewById(R.id.rvIcons);
        rvColors = findViewById(R.id.rvColors);
        rvRubriques = findViewById(R.id.rvRubriques);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (isEditMode) {
                toolbar.setTitle("Modifier catégorie");
            } else {
                toolbar.setTitle("Ajouter catégorie");
            }
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        
        rubricAdapter = new RubriqueAdapter(rubriquesNames);
        rvRubriques.setLayoutManager(new LinearLayoutManager(this));
        rvRubriques.setAdapter(rubricAdapter);

        findViewById(R.id.btnAddRubrique).setOnClickListener(v -> showAddRubriqueDialog());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveCategorie());
    }

    private void loadExistingData() {
        viewModel.getAllCategories().observe(this, categories -> {
            for (Categorie c : categories) {
                if (c.getId() == categoryId) {
                    existingCategorie = c;
                    etNom.setText(c.getNom());
                    selectedColorStr = c.getCouleur();
                    selectedIconName = c.getIcone();
                    
                    // Force refresh pickers selection
                    setupPickers();
                    break;
                }
            }
        });
        
        viewModel.getRubriquesForCategorie(categoryId).observe(this, rubriques -> {
            if (rubriques != null) {
                rubriquesNames.clear();
                for (com.tp.gestiondepenses.model.Rubrique r : rubriques) {
                    rubriquesNames.add(r.getNom());
                }
                rubricAdapter.notifyDataSetChanged();
            }
        });
    }

    private void saveCategorie() {
        String nom = etNom.getText().toString().trim();
        if (nom.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir un nom", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode && existingCategorie != null) {
            existingCategorie.setNom(nom);
            existingCategorie.setIcone(selectedIconName);
            existingCategorie.setCouleur(selectedColorStr);
            viewModel.update(existingCategorie);
            Toast.makeText(this, "Catégorie mise à jour", Toast.LENGTH_SHORT).show();
        } else {
            Categorie categorie = new Categorie(nom, selectedIconName, selectedColorStr, false);
            viewModel.insertWithRubriques(categorie, rubriquesNames);
            Toast.makeText(this, "Catégorie créée", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void setupPickers() {
        String[] iconNames = {
            "ic_category", "ic_restaurant", "ic_directions_car", "ic_shopping_cart", 
            "ic_sports_soccer", "ic_home", "ic_health", "ic_school", "ic_flight", "ic_card"
        };
        
        rvIcons.setLayoutManager(new GridLayoutManager(this, 5));
        IconPickerAdapter iconAdapter = new IconPickerAdapter(iconNames, selectedIconName, name -> {
            selectedIconName = name;
        });
        rvIcons.setAdapter(iconAdapter);

        String[] colors = {
            "#1C35C9", "#00695C", "#00BFA5", "#F57C00", "#C62828",
            "#AA00FF", "#F06292", "#37474F", "#795548", "#1E88E5"
        };
        rvColors.setLayoutManager(new GridLayoutManager(this, 5));
        ColorPickerAdapter colorAdapter = new ColorPickerAdapter(colors, selectedColorStr, color -> {
            selectedColorStr = color;
        });
        rvColors.setAdapter(colorAdapter);
    }

    private void showAddRubriqueDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_rubrique, null);
        EditText input = dialogView.findViewById(R.id.etRubriqueName);
        
        new AlertDialog.Builder(this)
                .setTitle("Ajouter une rubrique")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        rubriquesNames.add(name);
                        rubricAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // --- Adapters ---

    class IconPickerAdapter extends RecyclerView.Adapter<IconPickerAdapter.ViewHolder> {
        private final String[] icons;
        private String selected;
        private final OnIconSelected listener;

        IconPickerAdapter(String[] icons, String selected, OnIconSelected listener) {
            this.icons = icons;
            this.selected = selected;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_picker, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name = icons[position];
            int resId = getResources().getIdentifier(name, "drawable", getPackageName());
            holder.ivIcon.setImageResource(resId != 0 ? resId : R.drawable.ic_category);
            
            boolean isSelected = name.equals(selected);
            holder.card.setStrokeWidth(isSelected ? (int)(2 * getResources().getDisplayMetrics().density) : 0);
            holder.card.setCardBackgroundColor(ColorStateList.valueOf(isSelected ? Color.WHITE : Color.parseColor("#F0F2F7")));
            holder.ivIcon.setImageTintList(ColorStateList.valueOf(isSelected ? Color.parseColor("#1C35C9") : Color.parseColor("#616161")));

            holder.itemView.setOnClickListener(v -> {
                selected = name;
                notifyDataSetChanged();
                listener.onSelected(name);
            });
        }

        @Override
        public int getItemCount() { return icons.length; }

        class ViewHolder extends RecyclerView.ViewHolder {
            MaterialCardView card;
            ImageView ivIcon;
            ViewHolder(View v) {
                super(v);
                card = v.findViewById(R.id.cardIcon);
                ivIcon = v.findViewById(R.id.ivIcon);
            }
        }
    }

    interface OnIconSelected { void onSelected(String name); }

    class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
        private final String[] colors;
        private String selected;
        private final OnColorSelected listener;

        ColorPickerAdapter(String[] colors, String selected, OnColorSelected listener) {
            this.colors = colors;
            this.selected = selected;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_picker, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String color = colors[position];
            holder.card.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
            
            boolean isSelected = color.equalsIgnoreCase(selected);
            holder.ivCheck.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            holder.card.setStrokeWidth(isSelected ? (int)(2 * getResources().getDisplayMetrics().density) : 0);
            holder.card.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1C35C9")));

            holder.itemView.setOnClickListener(v -> {
                selected = color;
                notifyDataSetChanged();
                listener.onSelected(color);
            });
        }

        @Override
        public int getItemCount() { return colors.length; }

        class ViewHolder extends RecyclerView.ViewHolder {
            MaterialCardView card;
            ImageView ivCheck;
            ViewHolder(View v) {
                super(v);
                card = v.findViewById(R.id.cardColor);
                ivCheck = v.findViewById(R.id.ivCheck);
            }
        }
    }

    interface OnColorSelected { void onSelected(String color); }

    class RubriqueAdapter extends RecyclerView.Adapter<RubriqueAdapter.ViewHolder> {
        private final List<String> list;
        RubriqueAdapter(List<String> list) { this.list = list; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rubrique_form, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name = list.get(position);
            holder.tvName.setText(name);
            holder.btnDelete.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    list.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            ImageView btnDelete;
            ViewHolder(View v) {
                super(v);
                tvName = v.findViewById(R.id.tvRubriqueName);
                btnDelete = v.findViewById(R.id.btnDeleteRubrique);
            }
        }
    }
}