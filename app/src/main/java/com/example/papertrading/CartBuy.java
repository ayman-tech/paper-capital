package com.example.papertrading;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CartBuy extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    private static final int SWIPE_THRESHOLD = 500, SWIPE_VELOCITY_THRESHOLD = 200;
    private EditText amtET;
    private TextView codeTV, priceTV, changeTV, fundsTV, quantTV;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ArrayList<String> orderCode, orderPrice, orderQuant, orderDate;
    SimpleDateFormat dateFormat;
    String strDate;
    private String code, price, changeNo, changePercent;
    private Double myPrice, funds;
    private Integer myQuant;
    private final String TAG = "Buy Cart";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Buy Cart Activity in");
        setContentView(R.layout.activity_cart_buy);
        gestureDetector = new GestureDetector(this, this);

        amtET = findViewById(R.id.buyAmt);
        quantTV = findViewById(R.id.buyQuantity);
        //sellBT = findViewById(R.id.sellBtn);
        codeTV = findViewById(R.id.buyCode);
        priceTV = findViewById(R.id.buyPrice);
        changeTV = findViewById(R.id.buyChange);
        fundsTV = findViewById(R.id.buyFunds);

        code = getIntent().getStringExtra("code");
        price = getIntent().getStringExtra("price");
        changeNo = getIntent().getStringExtra("changeNo");
        changePercent = getIntent().getStringExtra("changePercent");
        codeTV.setText(code);
        priceTV.setText(price);
        changeTV.setText(changeNo + " " + changePercent);

        sharedPref = getSharedPreferences("paper", MODE_PRIVATE);
        editor = sharedPref.edit();
        orderCode = new ArrayList<>(sharedPref.getStringSet("orderCode", new HashSet<>()));
        orderPrice = new ArrayList<>(sharedPref.getStringSet("orderPrice", new HashSet<>()));
        orderQuant = new ArrayList<>(sharedPref.getStringSet("orderQuant", new HashSet<>()));
        orderDate = new ArrayList<>(sharedPref.getStringSet("orderDate", new HashSet<>()));
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        strDate = dateFormat.format(date);
        funds = (double) sharedPref.getFloat("funds", 1000000.00F);
        fundsTV.setText(String.format("Rs. %.2f", funds));

        amtET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tmpStr = charSequence.toString();
                if (tmpStr.equals(""))
                    return;
                Double tmp = Double.parseDouble(tmpStr.replace(",", ""));
                Double tmpPrice = Double.parseDouble(price.replaceAll(",", ""));
                int tmpInt = (int) (tmp / tmpPrice);
                quantTV.setText("" + tmpInt);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event) || gestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) { return false; }
    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {}
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) { return false; }
    @Override
    public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) { return false; }
    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {
        Log.d(TAG, ""+motionEvent.getY());
    }
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
                Toast.makeText(CartBuy.this, "Swipe up to confirm transaction", Toast.LENGTH_SHORT).show();
            } else {
                buyStock();
            }
        }
//    }
        return true;
    }
    private void buyStock(){
        myPrice = Double.parseDouble(price.replaceAll(",", ""));
        myQuant = Integer.parseInt(quantTV.getText().toString());
        if (myPrice == 0 || myQuant == 0) {
            Toast.makeText(CartBuy.this, "Please enter price and quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        Double fundsUsed = myPrice * myQuant;
        if (fundsUsed > funds) {
            Toast.makeText(CartBuy.this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
            return;
        }
        if (orderCode.contains(code)) {
            int index = orderCode.indexOf(code);
            int quant = Integer.parseInt(orderQuant.get(index));
            int dateDiff = 0;
            Date date1 = null;
            try {
                date1 = dateFormat.parse(orderDate.get(index));
            } catch (ParseException e) {
                Log.d(TAG, "Couldnt convert str to Date");
                throw new RuntimeException(e);
            }
            double avgPrice = Double.parseDouble(orderPrice.get(index));
            avgPrice = ((avgPrice * quant) + (myPrice * myQuant)) / (quant + myQuant);
            dateDiff = (dateDiff * quant) / (quant + myQuant);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, dateDiff);
            quant += myQuant;

            orderPrice.set(index, "" + avgPrice);
            orderQuant.set(index, "" + quant);
            orderDate.set(index, dateFormat.format(calendar.getTime()));
            Log.d(TAG, "New date" + dateFormat.format(calendar.getTime()));
            Log.d(TAG, "OrderCode updated Entry");
        } else {
            orderCode.add(code);
            orderPrice.add("" + myPrice);
            orderQuant.add("" + myQuant);
            orderDate.add(strDate);
            editor.putStringSet("orderCode", new HashSet<>(orderCode));
            Log.d(TAG, "New Date " + strDate);
            Log.d(TAG, "OrderCode new Entry");
        }
        Double unrealInvest = (double) sharedPref.getFloat("unrealInvest", 0.00f);
        unrealInvest += myPrice*myQuant;
        editor.putFloat("unrealInvest", unrealInvest.floatValue());
        Log.d(TAG, "home data updated");

        editor.putStringSet("orderPrice", new HashSet<>(orderPrice));
        editor.putStringSet("orderQuant", new HashSet<>(orderQuant));
        editor.putStringSet("orderDate", new HashSet<>(orderDate));
        funds -= fundsUsed;
        editor.putFloat("funds", funds.floatValue());
        Log.d(TAG, "Order placed; New order size : " + orderCode.size() + orderPrice.size() + orderQuant.size() + orderDate.size());
        editor.apply();
        Intent in = new Intent(CartBuy.this, OrderComplete.class);
        in.putExtra("type", "buy");
        startActivity(in);
    }
}