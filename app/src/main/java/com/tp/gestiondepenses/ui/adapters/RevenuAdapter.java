package com.tp.gestiondepenses.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.model.Revenu;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RevenuAdapter extends RecyclerView.Adapter<RevenuAdapter.RevenuViewHolder> {

    private List<Revenu> revenus = new ArrayList<>();
    private final OnItemClickListener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

    public interface OnItemClickListener {
        void onItemClick(Revenu revenu);
    }

    public RevenuAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RevenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenu, parent, false);
        return new RevenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenuViewHolder holder, int position) {
        Revenu current = revenus.get(position);
        holder.tvSource.setText(current.getSource());
        holder.tvDate.setText(sdf.format(current.getDate()));
        holder.tvAmount.setText(String.format(Locale.FRENCH, "+%,.0f FCFA", current.getMontant()).replace(',', '.'));
        
        // Simuler le compte pour la démo
        holder.tvAccount.setText("COMPTE PRINCIPAL");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return revenus.size();
    }

    public void setRevenus(List<Revenu> revenus) {
        this.revenus = revenus;
        notifyDataSetChanged();
    }

    static class RevenuViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSource, tvDate, tvAmount, tvAccount;
        private final ImageView ivIcon;

        public RevenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvAccount = itemView.findViewById(R.id.tvAccount);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}