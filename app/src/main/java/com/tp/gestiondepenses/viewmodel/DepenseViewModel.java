package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.CategoryTotal;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.repository.DepenseRepository;
import com.tp.gestiondepenses.repository.CategorieRepository;
import com.tp.gestiondepenses.utils.SessionManager;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DepenseViewModel extends AndroidViewModel {
    private final DepenseRepository repository;
    private final CategorieRepository categorieRepository;
    private final int userId;
    
    public enum Periode { AUJOURDHUI, SEMAINE, MOIS, TOUT }
    private final MutableLiveData<Periode> filtrePeriode = new MutableLiveData<>(Periode.MOIS);

    private final LiveData<List<Depense>> listeDepenses;
    private final LiveData<Double> totalDepensesMois;
    private final LiveData<String> plusGrosPoste;
    private final LiveData<String> evolutionVsHier;

    public DepenseViewModel(@NonNull Application application) {
        super(application);
        repository = new DepenseRepository(application);
        categorieRepository = new CategorieRepository(application);
        userId = SessionManager.getInstance(application).getUserId();

        listeDepenses = Transformations.switchMap(filtrePeriode, periode -> {
            Calendar cal = Calendar.getInstance();
            long fin = getEndOfDay(0);
            
            switch (periode) {
                case AUJOURDHUI:
                    return repository.getDepensesByDay(userId, getStartOfDay(0), fin);
                case SEMAINE:
                    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                    return repository.getDepensesByDay(userId, getStartOfDayForCalendar(cal), fin);
                case MOIS:
                    int mois = cal.get(Calendar.MONTH) + 1;
                    int annee = cal.get(Calendar.YEAR);
                    return repository.getDepensesByMois(userId, mois, annee);
                case TOUT:
                default:
                    return repository.getAllDepenses(userId);
            }
        });

        Calendar cal = Calendar.getInstance();
        int currentMois = cal.get(Calendar.MONTH) + 1;
        int currentAnnee = cal.get(Calendar.YEAR);
        
        totalDepensesMois = repository.getTotalDepensesParMois(userId, currentMois, currentAnnee);

        plusGrosPoste = Transformations.switchMap(repository.getTotalsByCategory(userId, currentMois, currentAnnee), totals -> {
            if (totals == null || totals.isEmpty()) {
                MutableLiveData<String> noData = new MutableLiveData<>();
                noData.setValue("Aucun");
                return noData;
            }
            CategoryTotal max = totals.get(0);
            for (CategoryTotal t : totals) {
                if (t.total > max.total) max = t;
            }
            final int catId = max.categorie_id;
            return Transformations.map(categorieRepository.getAllCategories(userId), categories -> {
                for (Categorie c : categories) {
                    if (c.getId() == catId) return c.getNom();
                }
                return "Inconnu";
            });
        });

        LiveData<Double> todayTotal = repository.getTotalDepensesByRange(userId, getStartOfDay(0), getEndOfDay(0));
        LiveData<Double> yesterdayTotal = repository.getTotalDepensesByRange(userId, getStartOfDay(-1), getEndOfDay(-1));

        androidx.lifecycle.MediatorLiveData<String> evolution = new androidx.lifecycle.MediatorLiveData<>();
        evolution.addSource(todayTotal, t -> updateEvolution(evolution, t, yesterdayTotal.getValue()));
        evolution.addSource(yesterdayTotal, y -> updateEvolution(evolution, todayTotal.getValue(), y));
        evolutionVsHier = evolution;
    }

    private void updateEvolution(androidx.lifecycle.MediatorLiveData<String> target, Double today, Double yesterday) {
        double t = (today != null) ? today : 0.0;
        double y = (yesterday != null) ? yesterday : 0.0;
        
        if (y == 0) {
            target.setValue(t > 0 ? "+100%" : "0%");
            return;
        }
        double diff = ((t - y) / y) * 100;
        target.setValue(String.format(Locale.FRANCE, "%+.1f%%", diff));
    }

    private long getStartOfDay(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, offset);
        return getStartOfDayForCalendar(cal);
    }

    private long getStartOfDayForCalendar(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private long getEndOfDay(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, offset);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    public LiveData<List<Depense>> getListeDepenses() { return listeDepenses; }
    public LiveData<Double> getTotalDepensesMois() { return totalDepensesMois; }
    public LiveData<String> getPlusGrosPoste() { return plusGrosPoste; }
    public LiveData<String> getEvolutionVsHier() { return evolutionVsHier; }
    public LiveData<Depense> getDepenseById(int id) { return repository.getDepenseById(id); }

    public void setFiltre(Periode periode) {
        filtrePeriode.setValue(periode);
    }

    public void insert(Depense depense) { 
        depense.setUserId(userId);
        repository.insert(depense); 
    }
    public void update(Depense depense) { 
        depense.setUserId(userId);
        repository.update(depense); 
    }
    public void delete(Depense depense) { repository.delete(depense); }
}
