package com.tp.gestiondepenses.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.repository.CategorieRepository;
import com.tp.gestiondepenses.ui.activities.DetailDepenseActivity;
import com.tp.gestiondepenses.utils.CurrencyUtils;
import com.tp.gestiondepenses.viewmodel.DepenseViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.DepenseViewHolder> {

    private List<Depense> depenses;
    private final DepenseViewModel depenseViewModel;
    private final CategorieRepository catRepo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public DepenseAdapter(List<Depense> depenses, DepenseViewModel depenseViewModel) {
        this.depenses = depenses;
        this.depenseViewModel = depenseViewModel;
        this.catRepo = new CategorieRepository(depenseViewModel.getApplication());
    }

    public void setDepenses(List<Depense> depenses) {
        this.depenses = depenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_depense, parent, false);
        return new DepenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DepenseViewHolder holder, int position) {
        Depense depense = depenses.get(position);

        holder.tvMontant.setText(CurrencyUtils.formatAmount(holder.itemView.getContext(), depense.getMontant()));
        holder.tvDescription.setText(depense.getDescription());
        holder.tvDate.setText(sdf.format(depense.getDate()));

        executor.execute(() -> {
            Categorie cat = catRepo.getCategorieByIdSync(depense.getCategorieId());
            if (cat != null) {
                holder.itemView.post(() -> {
                    try {
                        int color = Color.parseColor(cat.getCouleur());
                        // Light background for icon circle
                        holder.cardIcon.setCardBackgroundColor(ColorStateList.valueOf(color).withAlpha(30));
                        holder.ivCategoryIcon.setImageTintList(ColorStateList.valueOf(color));
                        
                        int iconResId = holder.itemView.getContext().getResources().getIdentifier(
                                cat.getIcone(), "drawable", holder.itemView.getContext().getPackageName());
                        if (iconResId != 0) {
                            holder.ivCategoryIcon.setImageResource(iconResId);
                        } else {
                            holder.ivCategoryIcon.setImageResource(R.drawable.ic_category);
                        }
                    } catch (Exception ignored) {
                        holder.cardIcon.setCardBackgroundColor(ColorStateList.valueOf(Color.LTGRAY));
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailDepenseActivity.class);
            intent.putExtra("DEPENSE_ID", depense.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return depenses != null ? depenses.size() : 0;
    }

    public static class DepenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvMontant, tvDescription;
        ImageView ivCategoryIcon;
        MaterialCardView cardIcon;

        public DepenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMontant = itemView.findViewById(R.id.tvMontant);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            cardIcon = itemView.findViewById(R.id.cardIcon);
        }
    }
}
