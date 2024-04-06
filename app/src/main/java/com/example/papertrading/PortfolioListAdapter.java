package com.example.papertrading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PortfolioListAdapter extends ArrayAdapter<PortfolioListData> {

    private TextView codeTV, priceTV, changeTV;
    private TextView myChangeNoTV, myPriceTV, myAmountTV, myChangePercentTV;

    public PortfolioListAdapter(@NonNull Context context, ArrayList<PortfolioListData> portfolioListData) {
        super(context, R.layout.portfolio_list, portfolioListData);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PortfolioListData portfolioListData = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.portfolio_list, parent, false);
        }

        myPriceTV = convertView.findViewById(R.id.buyPricePortfolioListTextView);
        codeTV = convertView.findViewById(R.id.codePortfolioListTextView);
        myAmountTV = convertView.findViewById(R.id.buyAmountPortfolioListTextView);
        priceTV = convertView.findViewById(R.id.ltpPortfolioListTextView);
        myChangeNoTV = convertView.findViewById(R.id.myChangeNoPortfolioListTextView);
        myChangePercentTV = convertView.findViewById(R.id.myChangePercentPortfolioListTextView);
        changeTV = convertView.findViewById(R.id.changePortfolioListTextView);

        double buy_price = Double.parseDouble(portfolioListData.getBuyPrice());
        double ltp_price = Double.parseDouble(portfolioListData.getPrice().replace(",",""));
        double buy_quantity = Integer.parseInt(portfolioListData.getBuyQuantity());
        double amt = buy_price * buy_quantity;
        double my_change_percent = ((ltp_price-buy_price)/buy_price)*100;
        double my_change_no = (ltp_price-buy_price)*buy_quantity;

        codeTV.setText(portfolioListData.getCode());
        priceTV.setText("LTP: "+ portfolioListData.getPrice());
        changeTV.setText(portfolioListData.getChangePercent());
        myPriceTV.setText(portfolioListData.getBuyPrice() + " x " + portfolioListData.getBuyQuantity());
        myAmountTV.setText(String.format("Invested: %.2f", amt));
        myChangeNoTV.setText(String.format("%.2f", my_change_no));
        myChangePercentTV.setText(String.format("%.2f", my_change_percent) + "%  ");

        return convertView;
    }
}
