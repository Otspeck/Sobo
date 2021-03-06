package com.example.liebherr_365_gesundheitsapp.ModulDrink;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.liebherr_365_gesundheitsapp.Database.Data;
import com.example.liebherr_365_gesundheitsapp.Database.DataSourceData;
import com.github.lzyzsd.circleprogress.DonutProgress;


import com.example.liebherr_365_gesundheitsapp.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ModulDrink extends AppCompatActivity {
    //	______________________
    //	\   _  \__    ___/_   |
    //	/  /_\  \|    |   |   |
    //	\  \_/   \    |   |   |
    //	 \_____  /____|   |___|
    //	       \/
    //  	        PRODCUTIONS
    //         m.otec@gmx.de <3

    // new DataSourceData
    private DataSourceData dataSourceData;
    private static int glassCounter = 0;
    private float donutProgressCounter;
    static private float donutIncrement = 0f;
    static int maxGlasses;
    static int liter;

    public static void resetGlassCounter() {
        glassCounter = 0;
    }

    public static void setLiter(int liter) {
        ModulDrink.liter = liter;// call function calculateDonutIncrement
        calculateDonutIncrement();
    }

    @Override
    public void onResume() {
        super.onResume();

        donutProgressCounter = 0;
        countUpDonutProgressCounterLoop();
        setDonutProgress();
        setGlassesText();

        if (glassCounter == 0) {
            // disableButtons if glassCounter == 0
            disableMinusButton();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SavedSharedPrefrencesModulDrink.setSharedPreferences(this);

        liter = SavedSharedPrefrencesModulDrink.getLiter();

        // setContentView
        setContentView(R.layout.activity_modul_drink);

        // call function calculateDonutIncrement
        calculateDonutIncrement();

        if (entryAlreadyExisting()) {
            // entryExisting
            entryExists();
        }

        if (dataExisting()) {
            enableHistorieButton();
        } else {
            disableHistorieButton();
        }

        // disableMinusButton if glassCounter == 0
        if (glassCounter == 0) {
            disableMinusButton();
        } else {
            enableMinusButton();
        }

        // bind donutProgress to DonutProgress
        DonutProgress donutProgress = (DonutProgress) findViewById(R.id.donut_progress);

        // set donutProgess text
        donutProgress.setPrefixText("Gläser");
        donutProgress.setTextColor(Color.parseColor("#7f7d7f"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moduldrink, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivityModulDrink.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //function minusTrigger onklick @+id/minus
    public void minusTrigger(View view) {
        glassCounter--;
        if (glassCounter == 0) {
            // disableMinusButton if glassCounter == 0
            disableMinusButton();
            deleteData();
        } else {
            updateData();
        }
        if (glassCounter < maxGlasses) {
            countDownDonutProgressCounter();
            setDonutProgress();
        }

        // call function setGlassesText
        setGlassesText();

        if (dataExisting()) {
            enableHistorieButton();
        } else {
            disableHistorieButton();
        }
    }

    //function plusTrigger onklick @+id/plus
    public void plusTrigger(View view) {
        glassCounter++;
        if (glassCounter <= maxGlasses) {
            countUpDonutProgressCounter();
            setDonutProgress();
        }

        // call function enableMinusButton
        enableMinusButton();

        // call function setGlassesText
        setGlassesText();

        if (!entryAlreadyExisting()) {
            insertData();
        } else {
            updateData();
        }

        if (dataExisting()) {
            enableHistorieButton();
        } else {
            disableHistorieButton();
        }
    }


    // function enableButtons
    private void enableHistorieButton() {
        // bind historieButton to Button
        Button historieButton = (Button) findViewById(R.id.historie);

        // enable Buttons
        historieButton.setEnabled(true);

        // set color
        historieButton.setBackgroundResource(R.color.colorWhite);
        historieButton.setTextColor(Color.parseColor("#403f3f"));

        // change alpha
        historieButton.getBackground().setAlpha(255);
    }

    // function disableButtons
    private void disableHistorieButton() {
        // bind historieButton to Button
        Button historieButton = (Button) findViewById(R.id.historie);

        // disable Button
        historieButton.setEnabled(false);

        // set color
        historieButton.setBackgroundResource(R.color.colorWhite);
        historieButton.setTextColor(Color.parseColor("#e3dfe2"));

        // change alpha
        //historieButton.getBackground().setAlpha(45);
    }

    // function enableButtons
    private void enableMinusButton() {
        // bind minusButton to Button
        Button minusButton = (Button) findViewById(R.id.minus);

        // enable Buttons
        minusButton.setEnabled(true);

        // set color
        minusButton.setBackgroundResource(R.color.colorWhite);
        minusButton.setTextColor(Color.parseColor("#403f3f"));

        // change alpha
        //minusButton.getBackground().setAlpha(255);
    }

    // function disableButtons
    private void disableMinusButton() {
        // bind minusButton to Button
        Button minusButton = (Button) findViewById(R.id.minus);

        // disable Button
        minusButton.setEnabled(false);

        // set color
        minusButton.setBackgroundResource(R.color.colorWhite);
        minusButton.setTextColor(Color.parseColor("#e3dfe2"));

        // change alpha
        //minusButton.getBackground().setAlpha(45);
    }

    // function countUpDonutProgressCounter
    private void countUpDonutProgressCounter() {
        if (glassCounter >= maxGlasses) {
            donutProgressCounter = 100f;
        } else {
            donutProgressCounter += donutIncrement;
        }
    }

    // function countDownDonutProgressCounter
    private void countDownDonutProgressCounter() {
        if (glassCounter == 0) {
            donutProgressCounter = 0f;
        } else {
            donutProgressCounter -= donutIncrement;
        }
    }

    // function calculate donutIncrement
    private static void calculateDonutIncrement() {
        maxGlasses = liter * 1000 / 250;
        donutIncrement = 100 / maxGlasses;

        Log.d("Dount", String.valueOf(maxGlasses));

        Log.d("Dount", String.valueOf(donutIncrement));
    }

    // function setDonutProgress
    private void setDonutProgress() {
        // bind donutProgress to DonutProgress
        DonutProgress donutProgress = (DonutProgress) findViewById(R.id.donut_progress);

        // bind TextView to reached
        TextView reached = (TextView) findViewById(R.id.reached);

        // setDonutProgress
        donutProgress.setProgress(donutProgressCounter);

        if (donutProgressCounter == 100) {
            // set finished color green
            donutProgress.setFinishedStrokeColor(Color.parseColor("#7f7d7f"));
            reached.setText(R.string.target_reached);
        } else {
            // set finished color blue
            donutProgress.setFinishedStrokeColor(Color.parseColor("#6cc0ae"));
            reached.setText("");
        }
    }

    // function setGlassesText
    private void setGlassesText() {
        // bind textglasses to TextView
        TextView textGlasses = (TextView) findViewById(R.id.glasses);

        // set current glassCounter as text
        textGlasses.setText(String.valueOf(glassCounter));
    }

    // function setGlassCounter
    private void setGlassCounter() {
        dataSourceData = new DataSourceData(this);
        dataSourceData.open();

        //call function entryalreadyexisting
        glassCounter = (int) dataSourceData.getLatestEntry("ModulDrink");

        // close db connection
        dataSourceData.close();
    }

    // function insertData()
    private void insertData() {
        // createNewDataObject with current date
        Data data = createNewDataObject();

        dataSourceData = new DataSourceData(this);
        dataSourceData.open();

        //call function insertdata
        dataSourceData.insertdata(data);

        // close db connection
        dataSourceData.close();
    }

    //function updateData()
    private void updateData() {
        // createNewDataObject with current date
        Data data = createNewDataObject();

        dataSourceData = new DataSourceData(this);
        dataSourceData.open();

        //call function updatedata
        dataSourceData.updatedata(data);

        // close db connection
        dataSourceData.close();
    }

    //function deleteData()
    private void deleteData() {
        // createNewDataObject with current date
        Data data = createNewDataObject();

        dataSourceData = new DataSourceData(this);
        dataSourceData.open();

        //call function deletesingledata
        dataSourceData.deletesingledata(data);

        // close db connection
        dataSourceData.close();
    }

    // createNewDataObject
    private Data createNewDataObject() {
        // get actualdate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        String actualdate = dateFormat.format(new java.util.Date());

        // type declaration
        String type = "Gläser";

        // modul declaration
        String modulweight = "ModulDrink";

        // new weightdateobject with values
        return new Data(modulweight, actualdate, glassCounter, type);
    }

    // function entryExists
    private void entryExists() {
        // setGlassCounter
        setGlassCounter();

        // setGlassesText
        setGlassesText();

        // countUpDonutProgressCounter foreach glass
        for (int i = 1; i <= glassCounter; i++) {
            if (donutProgressCounter < 100) {
                countUpDonutProgressCounter();
            }
        }

        // setDonutProgress
        setDonutProgress();
    }

    // function countUpDonutProgressCounterLoop
    private void countUpDonutProgressCounterLoop() {
        for (int i = 1; i <= glassCounter; i++) {
            if (donutProgressCounter < 100) {
                countUpDonutProgressCounter();
            }
        }
    }

    // function entryAlreadyExisting
    private boolean entryAlreadyExisting() {
        // declare result
        boolean result = false;

        // new DBHelperDataSource
        dataSourceData = new DataSourceData(this);
        dataSourceData.open();

        //call function entryalreadyexisting
        result = dataSourceData.entryAlreadyExisting("ModulDrink");

        // close db connection
        dataSourceData.close();

        return result;
    }

    //function dataExisting
    private boolean dataExisting() {
        // declare result
        boolean result = false;

        // new DBHelperDataSource
        dataSourceData = new DataSourceData(this);
        dataSourceData.open();

        // getFirstWeight
        int glasses = (int) dataSourceData.getLatestEntry("ModulDrink");

        // handle empty db
        if (glasses != 0) {
            result = true;
        }

        // close db connection
        dataSourceData.close();

        return result;
    }

    //function viewgraph onklick @+id/historie
    public void historie(View view) {
        //Creatiing new intent, which navigates to HistorieModulDrink on call
        Intent intent = new Intent(ModulDrink.this, HistorieModulDrink.class);
        startActivity(intent);
    }

}
