package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "rubriques",
        foreignKeys = {
                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.CASCADE),
        },
        indices = {@Index("categorie_id")})
public class Rubrique {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categorie_id")
    private int categorieId;

    private String nom;

    public Rubrique() {}

    public Rubrique(int categorieId, String nom) {
        this.categorieId = categorieId;
        this.nom = nom;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}