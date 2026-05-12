package com.tp.gestiondepenses.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentLoginBinding;
import com.tp.gestiondepenses.utils.SessionManager;
import com.tp.gestiondepenses.viewmodel.UserViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Vérification si déjà connecté
        if (SessionManager.getInstance(requireContext()).isLoggedIn()) {
            Navigation.findNavController(view).navigate(R.id.action_login_to_dashboard);
            return;
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            userViewModel.login(email, password);
        });

        binding.tvRegisterLink.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_login_to_register)
        );

        userViewModel.getLoginSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Navigation.findNavController(requireView()).navigate(R.id.action_login_to_dashboard);
            }
        });

        userViewModel.getAuthError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                userViewModel.getAuthError().setValue(null); // Reset error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
