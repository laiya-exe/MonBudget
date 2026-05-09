package com.tp.gestiondepenses.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.BudgetAvecProgression;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.ViewHolder> {

    private List<BudgetAvecProgression> items = new ArrayList<>();
    private double maxAmount = 1.0;

    public void setItems(List<BudgetAvecProgression> items) {
        this.items = items;
        this.maxAmount = 0;
        for (BudgetAvecProgression item : items) {
            if (item.montantDepense > maxAmount) {
                maxAmount = item.montantDepense;
            }
        }
        if (maxAmount == 0) maxAmount = 1.0;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BudgetAvecProgression item = items.get(position);
        holder.tvCategoryName.setText(item.nomCategorie);
        holder.tvCategoryAmount.setText(String.format(Locale.FRANCE, "%,.0f FCFA", item.montantDepense));
        
        int progress = (int) ((item.montantDepense / maxAmount) * 100);
        holder.progressCategory.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return Math.min(items.size(), 5);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvCategoryAmount;
        LinearProgressIndicator progressCategory;

        ViewHolder(View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tvCategoryName);
            tvCategoryAmount = view.findViewById(R.id.tvCategoryAmount);
            progressCategory = view.findViewById(R.id.progressCategory);
        }
    }
}
