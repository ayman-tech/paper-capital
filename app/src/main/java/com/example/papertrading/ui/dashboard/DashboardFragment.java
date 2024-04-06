package com.example.papertrading.ui.dashboard;

import android.content.Intent;
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
import com.example.papertrading.PortfolioListAdapter;
import com.example.papertrading.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private final String TAG = "Dashboard Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        //dashboardViewModel.clearData();     // To clear sharedPref order data
        dashboardViewModel.buildData();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final ListView dashboardLV = binding.dashboardListView;
        PortfolioListAdapter portfolioListAdapter = new PortfolioListAdapter(getActivity(), dashboardViewModel.getPortfolioListData());
        dashboardLV.setAdapter(portfolioListAdapter);
        dashboardLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Item Clicked");
                Intent intent = new Intent(getActivity(), CompanyInfo.class);
                intent.putExtra("code", dashboardViewModel.getPortfolioStockCode(i));
                Toast.makeText(getActivity(), dashboardViewModel.getPortfolioStockCode(i), Toast.LENGTH_SHORT).show();
                try{
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "Intent activity not started");
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}