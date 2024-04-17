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
    private TextView realPercentTV, realInvestTV, realExitTV, fundsTV;
    private TextView unrealPercentTV, unrealInvestTV, unrealExitTV;
    private SharedPreferences sharedPref;
    private final String TAG = "Home Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPref = getActivity().getSharedPreferences("paper", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        fundsTV = binding.homeFundsText;
        realExitTV = binding.realExit;
        realInvestTV = binding.realInvested;
        realPercentTV = binding.realPercent;
        unrealExitTV = binding.unrealCurrent;
        unrealInvestTV = binding.unrealInvested;
        unrealPercentTV = binding.unrealPercent;

        Float funds = sharedPref.getFloat("funds", 1000000.00F);
        Double realInvest = (double) sharedPref.getFloat("realInvest", 0.00f);
        Double realExit = (double) sharedPref.getFloat("realExit", 0.00f);
        Double unrealInvest = (double) sharedPref.getFloat("unrealInvest", 0.00f);
        Double unrealCurrent = (double) sharedPref.getFloat("unrealCurrent", 0.00f);
        Double realPercent, unrealPercent;
        if(realExit.equals(0.00))
            realPercent=0.00;
        else
            realPercent= (realExit-realInvest)*100/realInvest;
        if(unrealCurrent.equals(0.00))
            unrealPercent=0.00;
        else
            unrealPercent= (unrealCurrent-unrealInvest)*100/unrealInvest;
        fundsTV.setText(String.format("%.2f", funds));
        realInvestTV.setText(String.format("Invested : %.2f", realInvest));
        realExitTV.setText(String.format("Exit : %.2f", realExit));
        realPercentTV.setText(String.format("%.2f", realPercent) + "%");
        unrealInvestTV.setText(String.format("Invested : %.2f", unrealInvest));
        unrealExitTV.setText(String.format("Current : %.2f", unrealCurrent));
        unrealPercentTV.setText(String.format("%.2f", unrealPercent) + "%");

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