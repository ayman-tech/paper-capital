package com.example.papertrading;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    Context context;
    String fileName;
    String TAG = "CSV Reader";
    ArrayList<String[]> rows = new ArrayList<>();

    public CSVReader(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public ArrayList<String[]> readCSV() throws IOException {
//        InputStream is = context.getAssets().open(fileName);
        InputStream is = context.getResources().openRawResource(R.raw.ind_nifty500list);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String csvSplitBy = ",";

        int count=0;
        br.readLine();
        while ((line = br.readLine()) != null) {
            count++;
            String[] row = line.split(csvSplitBy);
            rows.add(row);
        }
        Log.d(TAG, "Read Lines "+ count);
        return rows;
    }
    public void writeCSV(String[] line){
        // code, buy date, buy price, sell price, sell date, gain, days, gain %, annual gain %
        // ToDo : Create a report
    }
}
