package com.example.papertrading.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.papertrading.CompanyInfo;
import com.example.papertrading.Search;
import com.example.papertrading.StockListAdapter;
import com.example.papertrading.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private ListView watchListView;
    private EditText searchBtn;
    private FragmentNotificationsBinding binding;
    private final String TAG = "Watchlist fragment in";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        watchListView = binding.ordersListView;
        StockListAdapter stockListAdapter = new StockListAdapter(getActivity(), notificationsViewModel.getStockListData());
        watchListView.setAdapter(stockListAdapter);
        watchListView.setClickable(true);
        watchListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.d(TAG, "Item Clicked");
                Intent intent = new Intent(getActivity(), CompanyInfo.class);
                intent.putExtra("code", notificationsViewModel.getStockDataCode(position));
                Toast.makeText(getActivity(), notificationsViewModel.getStockDataCode(position), Toast.LENGTH_SHORT).show();
                try{
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "Intent activity not started");
                }
            }
        });
        
//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        searchBtn = binding.searchButton;
        searchBtn.setKeyListener(null);
        searchBtn.setOnFocusChangeListener((view, b) ->{
            if(b) searchBtn.performClick();
        });
        searchBtn.setOnClickListener(view -> {
            Intent searchIntent = new Intent(getActivity(), Search.class);
            startActivity(searchIntent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}