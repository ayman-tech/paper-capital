package com.example.papertrading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class CartSell extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    private static final int SWIPE_THRESHOLD = 500, SWIPE_VELOCITY_THRESHOLD = 200;
    private EditText quantET;
    private TextView codeTV, priceTV, changeTV, availQuantTV, amtTV;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ArrayList<String> orderCode, orderPrice, orderQuant, orderDate;
    private String code, price, changeNo, changePercent;
    private Double myPrice, funds;
    private Integer myQuant, availQuant;
    Date date;
    SimpleDateFormat dateFormat;
    private final String TAG = "Sell Cart";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_sell);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Log.d(TAG, "Sell Cart Activity in");
        gestureDetector = new GestureDetector(this, this);
        availQuantTV = findViewById(R.id.availableQuant);
        quantET = findViewById(R.id.sellQuantity);
        codeTV = findViewById(R.id.sellCode);
        priceTV = findViewById(R.id.sellPrice);
        changeTV = findViewById(R.id.sellChange);
        amtTV = findViewById(R.id.sellAmount);

        code = getIntent().getStringExtra("code");
        price = getIntent().getStringExtra("price");
        changeNo = getIntent().getStringExtra("changeNo");
        changePercent = getIntent().getStringExtra("changePercent");
        codeTV.setText(code);
        priceTV.setText(price);
        changeTV.setText(changeNo + " " + changePercent);

        sharedPref = getSharedPreferences("paper",MODE_PRIVATE);
        editor = sharedPref.edit();
        orderCode = new ArrayList<>(sharedPref.getStringSet("orderCode", new HashSet<>()));
        orderPrice = new ArrayList<>(sharedPref.getStringSet("orderPrice", new HashSet<>()));
        orderQuant = new ArrayList<>(sharedPref.getStringSet("orderQuant", new HashSet<>()));
        orderDate = new ArrayList<>(sharedPref.getStringSet("orderDate", new HashSet<>()));
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = new Date();
        String strDate = dateFormat.format(date);
        funds = (double) sharedPref.getFloat("funds", 1000000.00F);
        int in=-1;
        if(orderCode.contains(code)) {
            in = orderCode.indexOf(code);
            availQuant = Integer.parseInt(orderQuant.get(in));
        }
        else availQuant=0;
        availQuantTV.setText(""+availQuant);
        myPrice = Double.parseDouble(priceTV.getText().toString().replace(",", ""));
        quantET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tmpStr = charSequence.toString();
                if(tmpStr.equals(""))
                    return;
                int tmpQuant = Integer.parseInt(tmpStr);
                Double tmpAmt = tmpQuant*myPrice;
                amtTV.setText(String.format("%.2f", tmpAmt));
            }
            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });
    }

    private int getDateDiff(Date date1, Date date2){
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        int dateDiff = Math.toIntExact(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));
        return dateDiff;
    }

    private void sellStock(){
        myQuant = Integer.parseInt(quantET.getText().toString());
        if(myPrice==0 || myQuant==0){
            Toast.makeText(CartSell.this, "Please enter price and quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        if(availQuant==0){
            Toast.makeText(CartSell.this, "You dont own shares", Toast.LENGTH_SHORT).show();
            return;
        }
        int index = orderCode.indexOf(code);
        int quant = Integer.parseInt(quantET.getText().toString());
        Date date2 = null;
        Log.d(TAG, "OrderDate size : "+orderDate.size());
        Log.d(TAG, "order date : "+orderDate.get(index));
        try {
            date2 = dateFormat.parse(orderDate.get(index));
        } catch (ParseException e) {
            Log.d(TAG, "Couldnt convert str to Date");
            throw new RuntimeException(e);
        }
        int dateDiff = getDateDiff(date, date2);
        Log.d(TAG, "Days : " + dateDiff);
        if((quant < myQuant) /*|| (dateDiff == 0)*/){
            Toast.makeText(CartSell.this, "You dont have enough shares", Toast.LENGTH_SHORT).show();
            return;
        }
        Double buyPrice = Double.parseDouble(orderPrice.get(index));
        //int availQuant = Integer.parseInt(orderQuant.get(index));
        Double gain = (myPrice*myQuant) - (buyPrice*availQuant);
        Double gainPercent = (100.00f*gain)/(buyPrice*availQuant);
        Double annualGain = (gainPercent*365.0)/dateDiff;
        String toastMsg = "Gain : "+gain+"\nGain Percent : "+gainPercent+"\nAnnual Gain : "+annualGain;
        Log.d(TAG, toastMsg);
        if(myQuant != quant) {
            quant -= myQuant;
            orderQuant.set(index, "" + quant);
            Log.d(TAG, "Data updated from portfolio");
        }else {
            orderCode.remove(index);
            orderPrice.remove(index);
            orderQuant.remove(index);
            orderDate.remove(index);
            editor.putStringSet("orderCode", new HashSet<>(orderCode));
            editor.putStringSet("orderPrice", new HashSet<>(orderPrice));
            Log.d(TAG, "Data removed from portfolio");
        }
        editor.putStringSet("orderQuant", new HashSet<>(orderQuant));
        Log.d(TAG, "Order placed; New order size : " + orderCode.size()+orderPrice.size()+orderQuant.size()+orderDate.size());
        funds += myPrice*myQuant;
        editor.putFloat("funds", funds.floatValue());
        editor.apply();
        Intent intent = new Intent(CartSell.this, OrderComplete.class);
        intent.putExtra("gain", String.format("%.2f",gain));
        intent.putExtra("gainPercent", String.format("%.2f",gainPercent));
        intent.putExtra("annualGain", String.format("%.2f",annualGain));
        intent.putExtra("days", ""+dateDiff);
        intent.putExtra("type", "sell");
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event) || gestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {}
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {}
    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float vx, float vy) {
        //float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        Log.d(TAG, "Fling");
//        if (Math.abs(diffX) > Math.abs(diffY)) {
//            // Horizontal swipe
//            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(vx) > SWIPE_VELOCITY_THRESHOLD) {
//                if (diffX > 0) { onSwipeRight(); }
//                else { onSwipeLeft(); }
//            }
//        } else { // Vertical swipe
        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(vy) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY > 0) {
                Toast.makeText(CartSell.this, "Swipe up to confirm transaction", Toast.LENGTH_SHORT).show();
            } else {
                sellStock();
            }
        }
//    }
        return true;
    }
}