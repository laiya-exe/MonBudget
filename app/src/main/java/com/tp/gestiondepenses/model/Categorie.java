package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "categories")
public class Categorie {
    @PrimaryKey(autoGenerate = true)
    public int id;
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
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public boolean isEstDefaut() { return estDefaut; }
    public void setEstDefaut(boolean estDefaut) { this.estDefaut = estDefaut; }
}