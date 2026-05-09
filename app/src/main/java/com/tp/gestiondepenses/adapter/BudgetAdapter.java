package com.tp.gestiondepenses.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.BudgetAvecProgression;
import com.tp.gestiondepenses.ui.activities.DetailBudgetActivity;
import com.tp.gestiondepenses.viewmodel.BudgetViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<BudgetAvecProgression> budgets = new ArrayList<>();
    private final BudgetViewModel viewModel;

    public BudgetAdapter(BudgetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setBudgets(List<BudgetAvecProgression> budgets) {
        this.budgets = budgets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetAvecProgression budget = budgets.get(position);
        holder.bind(budget, viewModel);
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailBudgetActivity.class);
            intent.putExtra("BUDGET_ID", budget.budget.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvCategorie, tvPlafond, tvConsomme, tvPourcentage;
        LinearProgressIndicator pbBudget;
        ImageView ivIcon;
        MaterialCardView cardIcon;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategorie = itemView.findViewById(R.id.tvCategorieNom);
            tvPlafond = itemView.findViewById(R.id.tvMontantPlafond);
            tvConsomme = itemView.findViewById(R.id.tvConsomme);
            tvPourcentage = itemView.findViewById(R.id.tvPourcentage);
            pbBudget = itemView.findViewById(R.id.pbBudget);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
            cardIcon = itemView.findViewById(R.id.cardIcon);
        }

        public void bind(BudgetAvecProgression item, BudgetViewModel viewModel) {
            tvCategorie.setText(item.nomCategorie);
            
            // Formatage des montants
            tvPlafond.setText(String.format(Locale.FRANCE, "/ %.0f FCFA", item.budget.getMontantPlafond()));
            tvConsomme.setText(String.format(Locale.FRANCE, "%.0f FCFA", item.montantDepense));
            tvPourcentage.setText(String.format(Locale.FRANCE, "%.0f%%", item.pourcentage));

            // Icône et couleur de fond de l'icône
            try {
                String colorStr = item.couleurCategorie != null ? item.couleurCategorie : "#E8EAF6";
                int color = Color.parseColor(colorStr);
                // On utilise une version claire de la couleur pour le fond si possible
                cardIcon.setCardBackgroundColor(ColorStateList.valueOf(color).withAlpha(40));
                
                int iconResId = itemView.getContext().getResources().getIdentifier(
                        item.iconeCategorie, "drawable", itemView.getContext().getPackageName());
                ivIcon.setImageResource(iconResId != 0 ? iconResId : R.drawable.ic_category);
                ivIcon.setImageTintList(ColorStateList.valueOf(color));
            } catch (Exception e) {
                cardIcon.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#F3F4F6")));
            }

            // Couleur de progression (Vert -> Orange -> Rouge)
            int statusColor = Color.parseColor(viewModel.getColorCode(item.pourcentage));
            pbBudget.setProgress((int) Math.min(item.pourcentage, 100));
            pbBudget.setIndicatorColor(statusColor);
            tvPourcentage.setTextColor(statusColor);
        }
    }
}
