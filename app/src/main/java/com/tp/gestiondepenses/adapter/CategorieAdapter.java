package com.tp.gestiondepenses.adapter;

import android.content.Context;
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
import com.tp.gestiondepenses.model.CategorieWithRubriques;

import java.util.ArrayList;
import java.util.List;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder> {

    private List<CategorieWithRubriques> categories = new ArrayList<>();
    private OnCategorieClickListener listener;

    public interface OnCategorieClickListener {
        void onEditClick(Categorie categorie);
        void onDeleteClick(Categorie categorie);
        void onManageRubriquesClick(Categorie categorie);
    }

    public void setOnCategorieClickListener(OnCategorieClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<CategorieWithRubriques> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategorieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorie_gestion, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorieViewHolder holder, int position) {
        CategorieWithRubriques item = categories.get(position);
        Categorie current = item.categorie;
        
        holder.tvNom.setText(current.getNom());
        
        int color = Color.GRAY;
        try {
            if (current.getCouleur() != null && !current.getCouleur().isEmpty()) {
                color = Color.parseColor(current.getCouleur());
            }
        } catch (Exception ignored) {}


        int alphaColor = Color.argb(30, Color.red(color), Color.green(color), Color.blue(color));
        holder.cardIconBg.setCardBackgroundColor(ColorStateList.valueOf(alphaColor));
        
        holder.ivCategoryIcon.setImageResource(getIconResId(current.getIcone(), holder.itemView.getContext()));
        holder.ivCategoryIcon.setImageTintList(ColorStateList.valueOf(color));

        if (current.isEstDefaut()) {
            holder.tvBadgePerso.setVisibility(View.GONE);
        } else {
            holder.tvBadgePerso.setVisibility(View.VISIBLE);
        }

        // Cacher le divider pour le dernier élément si nécessaire (géré par le parent en général ou laisser tel quel)
        holder.divider.setVisibility(position == categories.size() - 1 ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                if (current.isEstDefaut()) {
                    listener.onManageRubriquesClick(current);
                } else {
                    listener.onEditClick(current);
                }
            }
        });
    }

    private int getIconResId(String iconName, Context context) {
        if (iconName == null || iconName.isEmpty()) return R.drawable.ic_category;
        
        switch (iconName) {
            case "local_dining": return R.drawable.ic_restaurant;
            case "directions_car": return R.drawable.ic_directions_car;
            case "apartment": return R.drawable.ic_home;
            case "favorite": return R.drawable.ic_health;
            case "school": return R.drawable.ic_school;
            case "sports_bar": return R.drawable.ic_sports_soccer;
            case "shopping_cart": return R.drawable.ic_shopping_cart;
            default:
                int resId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
                return resId != 0 ? resId : R.drawable.ic_category;
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategorieViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNom, tvBadgePerso;
        private MaterialCardView cardIconBg;
        private ImageView ivCategoryIcon;
        private View divider;

        public CategorieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvBadgePerso = itemView.findViewById(R.id.tvBadgePerso);
            cardIconBg = itemView.findViewById(R.id.cardIconBg);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
