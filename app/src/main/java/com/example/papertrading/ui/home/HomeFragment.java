package com.example.papertrading.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.papertrading.CompanyInfo;
import com.example.papertrading.StockListAdapter;
import com.example.papertrading.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView listView;
    private SharedPreferences sharedPref;
    private final String TAG = "Home Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView fundsTV;
        sharedPref = getActivity().getSharedPreferences("paper", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Float funds = sharedPref.getFloat("funds", 1000000.00F);
        fundsTV = binding.homeFundsText;
        fundsTV.setText(String.format("%.2f", funds));
        editor.putFloat("funds", funds);
        editor.apply();
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}