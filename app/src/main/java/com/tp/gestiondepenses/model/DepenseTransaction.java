package com.tp.gestiondepenses.model;

import androidx.room.Embedded;
import androidx.room.ColumnInfo;

/**
 * Objet de transfert pour joindre une dépense et sa catégorie
 */
public class DepenseTransaction implements Transaction {
    
    @Embedded
    public Depense depense;

    @ColumnInfo(name = "categoryName")
    public String categoryName;

    @ColumnInfo(name = "categoryIcon")
    public String categoryIcon;

    @Override
    public double getMontant() {
        return depense != null ? depense.getMontant() : 0;
    }

    @Override
    public long getDate() {
        return depense != null ? depense.getDate() : 0;
    }

    @Override
    public String getDescription() {
        return depense != null ? depense.getDescription() : "";
    }

    @Override
    public String getType() {
        return "DEPENSE";
    }

    @Override
    public String getIconName() {
        return (categoryIcon != null && !categoryIcon.isEmpty()) ? categoryIcon : "ic_category";
    }

    @Override
    public String getCategoryName() {
        return (categoryName != null && !categoryName.isEmpty()) ? categoryName : "Dépense";
    }
}
