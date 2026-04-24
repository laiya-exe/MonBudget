package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(tableName = "depenses",
        foreignKeys = {
                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.RESTRICT), // Empêche de supprimer une catégorie utilisée
        })
public class Rubrique {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categorie_id")
    private int categorieId;

    private String description;

    public Rubrique(int id, int categorieId, String description) {
        this.id = id;
        this.categorieId = categorieId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}