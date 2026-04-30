package com.tp.gestiondepenses.model;

public class BudgetInfo {
    private String nom;
    private double pourcentage;
    private double restant;
    private double plafond;

    public BudgetInfo(String nom, double pourcentage, double restant, double plafond) {
        this.nom = nom;
        this.pourcentage = pourcentage;
        this.restant = restant;
        this.plafond = plafond;
    }

    public BudgetInfo(){}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public double getRestant() {
        return restant;
    }

    public void setRestant(double restant) {
        this.restant = restant;
    }

    public double getPlafond() {
        return plafond;
    }

    public void setPlafond(double plafond) {
        this.plafond = plafond;
    }
}