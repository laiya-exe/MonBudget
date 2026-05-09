package com.tp.gestiondepenses.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.repository.RevenuRepository;
import java.util.Calendar;
import java.util.List;

public class RevenuViewModel extends AndroidViewModel {
    public enum FilterType { TODAY, WEEK, MONTH, YEAR, ALL }

    private final RevenuRepository repository;
    private final MutableLiveData<FilterType> currentFilter = new MutableLiveData<>(FilterType.MONTH);
    private final MutableLiveData<Long> refreshTrigger = new MutableLiveData<>(System.currentTimeMillis());

    private final LiveData<List<Revenu>> filteredRevenus;
    private final LiveData<Double> totalMensuel;
    private final LiveData<Double> totalMoisPrecedent;

    public RevenuViewModel(@NonNull Application application) {
        super(application);
        repository = new RevenuRepository(application);

        // Liste filtrée réactive
        filteredRevenus = Transformations.switchMap(currentFilter, filter -> {
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            
            switch (filter) {
                case TODAY:
                    return repository.getRevenusAujourdhui();
                case WEEK:
                    return repository.getRevenusCetteSemaine();
                case MONTH:
                    return repository.getRevenusParMois(month, year);
                case YEAR:
                    return repository.getRevenusCetteAnnee();
                case ALL:
                    return repository.getAllRevenus();
                default:
                    return repository.getRevenusParMois(month, year);
            }
        });

        // Totaux réactifs (se rafraîchissent au clic ou à l'insertion)
        totalMensuel = Transformations.switchMap(refreshTrigger, t -> {
            Calendar cal = Calendar.getInstance();
            return repository.getTotalRevenusParMois(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        });

        totalMoisPrecedent = Transformations.switchMap(refreshTrigger, t -> {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            return repository.getTotalRevenusParMois(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        });
    }

    public void setFilter(FilterType filter) {
        currentFilter.setValue(filter);
        refresh();
    }

    public void refresh() {
        refreshTrigger.setValue(System.currentTimeMillis());
    }

    public LiveData<List<Revenu>> getFilteredRevenus() {
        return filteredRevenus;
    }
    
    public LiveData<List<Revenu>> getAllRevenus() {
        return repository.getAllRevenus();
    }

    public LiveData<Double> getTotalMensuel() {
        return totalMensuel;
    }
    
    public LiveData<Double> getTotalMoisPrecedent() {
        return totalMoisPrecedent;
    }

    public LiveData<Revenu> getRevenuById(int id) {
        return repository.getRevenuById(id);
    }

    public void insert(Revenu revenu) {
        repository.insert(revenu);
        refresh();
    }

    public void update(Revenu revenu) {
        repository.update(revenu);
        refresh();
    }

    public void delete(Revenu revenu) {
        repository.delete(revenu);
        refresh();
    }
}