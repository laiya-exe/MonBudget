package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "categories",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("user_id")})
public class Categorie {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    @ColumnInfo(name = "user_id")
    public Integer userId; // Null pour les catégories par défaut

    public String nom;
    public String icone;
    public String couleur;
    
    @ColumnInfo(name = "est_defaut")
    public boolean estDefaut;

    public Categorie() {}

    public Categorie(String nom, String icone, String couleur, boolean estDefaut) {
        this.nom = nom;
        this.icone = icone;
        this.couleur = couleur;
        this.estDefaut = estDefaut;
        this.userId = null;
    }

    public Categorie(Integer userId, String nom, String icone, String couleur, boolean estDefaut) {
        this.userId = userId;
        this.nom = nom;
        this.icone = icone;
        this.couleur = couleur;
        this.estDefaut = estDefaut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public boolean isEstDefaut() { return estDefaut; }
    public void setEstDefaut(boolean estDefaut) { this.estDefaut = estDefaut; }
}
