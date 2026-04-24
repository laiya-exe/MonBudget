package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(tableName = "depenses")
public class Categorie {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nom;

    public String icone;

    public String couleur;

    public boolean est_default;

    public Categorie(int id, String nom, String icone, String couleur, boolean est_default) {
        this.id = id;
        this.nom = nom;
        this.icone = icone;
        this.couleur = couleur;
        this.est_default = est_default;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public boolean isEst_default() {
        return est_default;
    }

    public void setEst_default(boolean est_default) {
        this.est_default = est_default;
    }
}