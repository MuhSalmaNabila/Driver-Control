package com.ditrol.tugasakhir;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ditrol.tugasakhir.utils.JSONParser;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;

public class SpeedActivity extends AppCompatActivity {


    SessionManagement session;
    String idMotor;
    Double[] speedValue = new Double[24];
    int[] average = new int[24];
    MaterialBetterSpinner tanggalSpinner;
    private String tanggal_kecepatan;

    String[] SPINNERLIST = {"Hari ini", "H-1", "H-2", "H-3", "H-4", "H-5", "H-6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        // Initiate array speed
        resetArray();
        // Update speed
        //updateSpeedTask us = new updateSpeedTask();
        //us.execute();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // no plat motor digunakan sebagai id
        String userPlatMotor = user.get(SessionManagement.KEY_KODE_USER);
        idMotor = userPlatMotor;

        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");


        // Hari ini
        Calendar c = Calendar.getInstance();
        String tanggal1 = format1.format(c.getTime());

        // 6 hari terakhir
        // H-1
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.DAY_OF_YEAR, -1);
        String tanggal2 = format1.format(cal1.getTime());

        // H-2
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
        cal2.add(Calendar.DAY_OF_YEAR, -2);
        String tanggal3 = format1.format(cal2.getTime());

        // H-3
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(new Date());
        cal3.add(Calendar.DAY_OF_YEAR, -3);
        String tanggal4 = format1.format(cal3.getTime());

        // H-4
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(new Date());
        cal4.add(Calendar.DAY_OF_YEAR, -4);
        String tanggal5 = format1.format(cal4.getTime());

        // H-5
        Calendar cal5 = Calendar.getInstance();
        cal5.setTime(new Date());
        cal5.add(Calendar.DAY_OF_YEAR, -5);
        String tanggal6 = format1.format(cal5.getTime());

        // H-6
        Calendar cal6 = Calendar.getInstance();
        cal6.setTime(new Date());
        cal6.add(Calendar.DAY_OF_YEAR, -6);
        String tanggal7 = format1.format(cal6.getTime());

        SPINNERLIST[0] = tanggal1;
        SPINNERLIST[1] = tanggal2;
        SPINNERLIST[2] = tanggal3;
        SPINNERLIST[3] = tanggal4;
        SPINNERLIST[4] = tanggal5;
        SPINNERLIST[5] = tanggal6;
        SPINNERLIST[6] = tanggal7;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        tanggalSpinner = (MaterialBetterSpinner)
                findViewById(R.id.tanggal_kecepatan);
        tanggalSpinner.setAdapter(arrayAdapter);
    }

    public void resetArray(){
        // Initiate array for speed
        speedValue[0]=0.0;
        speedValue[1]=0.0;
        speedValue[2]=0.0;
        speedValue[3]=0.0;
        speedValue[4]=0.0;
        speedValue[5]=0.0;
        speedValue[6]=0.0;
        speedValue[7]=0.0;
        speedValue[8]=0.0;
        speedValue[9]=0.0;
        speedValue[10]=0.0;
        speedValue[11]=0.0;
        speedValue[12]=0.0;
        speedValue[13]=0.0;
        speedValue[14]=0.0;
        speedValue[15]=0.0;
        speedValue[16]=0.0;
        speedValue[17]=0.0;
        speedValue[18]=0.0;
        speedValue[19]=0.0;
        speedValue[20]=0.0;
        speedValue[21]=0.0;
        speedValue[22]=0.0;
        speedValue[23]=0.0;

        // Initiate array for average
        average[0] = 0;
        average[1] = 0;
        average[2] = 0;
        average[3] = 0;
        average[4] = 0;
        average[5] = 0;
        average[6] = 0;
        average[7] = 0;
        average[8] = 0;
        average[9] = 0;
        average[10] = 0;
        average[11] = 0;
        average[12] = 0;
        average[13] = 0;
        average[14] = 0;
        average[15] = 0;
        average[16] = 0;
        average[17] = 0;
        average[18] = 0;
        average[19] = 0;
        average[20] = 0;
        average[21] = 0;
        average[22] = 0;
        average[23] = 0;

    }

    public void updateSpeed(View view){
        resetArray();
        tanggal_kecepatan = tanggalSpinner.getText().toString();
        Log.v("Test", ""+ tanggal_kecepatan);
        updateSpeedTask us = new updateSpeedTask();
        us.execute();
    }

    public Double[] getSpeed(){
        return speedValue;
    }

    public int[] getAverage(){
        return average;
    }

    /**
     * A fragment containing a column chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final int DEFAULT_DATA = 0;
        private static final int SUBCOLUMNS_DATA = 1;
        private static final int STACKED_DATA = 2;
        private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
        private static final int NEGATIVE_STACKED_DATA = 4;

        private ColumnChartView chart;
        private ColumnChartData data;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLabels = false;
        private boolean hasLabelForSelected = false;
        private int dataType = DEFAULT_DATA;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_column_chart, container, false);

            chart = (ColumnChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            generateData();

            return rootView;
        }


        private void reset() {
            hasAxes = true;
            hasAxesNames = true;
            hasLabels = false;
            hasLabelForSelected = false;
            dataType = DEFAULT_DATA;
            chart.setValueSelectionEnabled(hasLabelForSelected);

        }

        private void generateDefaultData() {
            int numSubcolumns = 1;
            int numColumns = 24;
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;

            SpeedActivity speedActivity = (SpeedActivity)getActivity();
            Double[] speed = speedActivity.getSpeed();
            Double speedValue = 0.0;
            int[] average = speedActivity.getAverage();
            int averageValue = 0;

            for (int i = 0; i < numColumns; ++i) {
                values = new ArrayList<SubcolumnValue>();
                averageValue = average[i];
                if(averageValue != 0){
                    speedValue = speed[i]/average[i];
                }else{
                    speedValue = speed[i];
                }
                //float speedFloat = speedValue/(1.0278-(.0278*reps));
                float speedFloat = Float.valueOf(String.valueOf(speedValue));
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(speedFloat, ChartUtils.pickColor()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Jam"); // X
                    axisY.setName("Frekuensi Kecepatan"); // Y
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);

        }

        /**
         * Generates columns with subcolumns, columns have larger separation than subcolumns.
         */
        private void generateSubcolumnsData() {
            int numSubcolumns = 4;
            int numColumns = 4;
            // Column can have many subcolumns, here I use 4 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);

        }

        /**
         * Generates columns with stacked subcolumns.
         */
        private void generateStackedData() {
            int numSubcolumns = 4;
            int numColumns = 8;
            // Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 20f + 5, ChartUtils.pickColor()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            // Set stacked flag.
            data.setStacked(true);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);
        }

        private void generateNegativeSubcolumnsData() {

            int numSubcolumns = 4;
            int numColumns = 4;
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    int sign = getSign();
                    values.add(new SubcolumnValue((float) Math.random() * 50f * sign + 5 * sign, ChartUtils.pickColor
                            ()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);
        }

        private void generateNegativeStackedData() {

            int numSubcolumns = 4;
            int numColumns = 8;
            // Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    int sign = getSign();
                    values.add(new SubcolumnValue((float) Math.random() * 20f * sign + 5 * sign, ChartUtils.pickColor()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            // Set stacked flag.
            data.setStacked(true);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);
        }

        private int getSign() {
            int[] sign = new int[]{-1, 1};
            return sign[Math.round((float) Math.random())];
        }

        private void generateData() {
            switch (dataType) {
                case DEFAULT_DATA:
                    generateDefaultData();
                    break;
                case SUBCOLUMNS_DATA:
                    generateSubcolumnsData();
                    break;
                case STACKED_DATA:
                    generateStackedData();
                    break;
                case NEGATIVE_SUBCOLUMNS_DATA:
                    generateNegativeSubcolumnsData();
                    break;
                case NEGATIVE_STACKED_DATA:
                    generateNegativeStackedData();
                    break;
                default:
                    generateDefaultData();
                    break;
            }
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;
            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()).
         */
        private void prepareDataAnimation() {
            for (Column column : data.getColumns()) {
                for (SubcolumnValue value : column.getValues()) {
                    value.setTarget((float) Math.random() * 100);
                }
            }
        }

        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                Toast.makeText(getActivity(), "Kecepatan: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }

    }

    class updateSpeedTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        JSONParser jParser = new JSONParser();
        JSONArray posts = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SpeedActivity.this);
            pDialog.setMessage("Proses...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getSpeed();
            return returnResult;

        }

        public String getSpeed()
        {
            tanggal_kecepatan = tanggalSpinner.getText().toString();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("tanggal", tanggal_kecepatan)); // id motor menggunakan no plat motor

            // param
            final String KECEPATAN = "kecepatan";
            final String TANGGAL = "tanggal";
            final String JAM = "jam";

            try {
                String url_all_posts = "http://drivercontrol.info/read_speed.php" ;

                JSONObject json = jParser.makeHttpRequest(url_all_posts,"POST", parameter);

                JSONArray jsonArray = json.getJSONArray("speed");
                int length = jsonArray.length();

                Log.v("Kecepatan", "Panjang Array: " + length);

                for(int n=0; n < length; n++){
                    JSONObject speed = jsonArray.getJSONObject(n);
                    int kecepatan = speed.getInt(KECEPATAN);
                    String jam = speed.getString(JAM);
                    String[] timeArray = jam.split("");

                    String sHour;
                    if(!timeArray[2].equals(":")){
                        sHour = timeArray[1]+timeArray[2];
                    }else{
                        sHour = timeArray[1];
                    }

                    int hour = Integer.valueOf(sHour);

                    switch (hour){
                        case 0:
                            speedValue[0] = speedValue[0]+kecepatan;
                            average[0] = average[0]+1;
                            break;
                        case 1:
                            speedValue[1] = speedValue[1]+kecepatan;
                            average[1] = average[1]+1;
                            break;
                        case 2:
                            speedValue[2] = speedValue[2]+kecepatan;
                            average[2] = average[2]+1;
                            break;
                        case 3:
                            speedValue[3] = speedValue[3]+kecepatan;
                            average[3] = average[3]+1;
                            break;
                        case 4:
                            speedValue[4] = speedValue[4]+kecepatan;
                            average[4] = average[4]+1;
                            break;
                        case 5:
                            speedValue[5] = speedValue[5]+kecepatan;
                            average[5] = average[5]+1;
                            break;
                        case 6:
                            speedValue[6] = speedValue[6]+kecepatan;
                            average[6] = average[6]+1;
                            break;
                        case 7:
                            speedValue[7] = speedValue[7]+kecepatan;
                            average[7] = average[7]+1;
                            break;
                        case 8:
                            speedValue[8] = speedValue[8]+kecepatan;
                            average[8] = average[8]+1;
                            break;
                        case 9:
                            speedValue[9] = speedValue[9]+kecepatan;
                            average[9] = average[9]+1;
                            break;
                        case 10:
                            speedValue[10] = speedValue[10]+kecepatan;
                            average[10] = average[10]+1;
                            break;
                        case 11:
                            speedValue[11] = speedValue[11]+kecepatan;
                            average[11] = average[11]+1;
                            break;
                        case 12:
                            speedValue[12] = speedValue[12]+kecepatan;
                            average[12] = average[12]+1;
                            break;
                        case 13:
                            speedValue[13] = speedValue[13]+kecepatan;
                            average[13] = average[13]+1;
                            break;
                        case 14:
                            speedValue[14] = speedValue[14]+kecepatan;
                            average[14] = average[14]+1;
                            break;
                        case 15:
                            speedValue[15] = speedValue[15]+kecepatan;
                            average[15] = average[15]+1;
                            break;
                        case 16:
                            speedValue[16] = speedValue[16]+kecepatan;
                            average[16] = average[16]+1;
                            break;
                        case 17:
                            speedValue[17] = speedValue[17]+kecepatan;
                            average[17] = average[17]+1;
                            break;
                        case 18:
                            speedValue[18] = speedValue[18]+kecepatan;
                            average[18] = average[18]+1;
                            break;
                        case 19:
                            speedValue[19] = speedValue[19]+kecepatan;
                            average[19] = average[19]+1;
                            break;
                        case 20:
                            speedValue[20] = speedValue[20]+kecepatan;
                            average[20] = average[20]+1;
                            break;
                        case 21:
                            speedValue[21] = speedValue[21]+kecepatan;
                            average[21] = average[21]+1;
                            break;
                        case 22:
                            speedValue[22] = speedValue[22]+kecepatan;
                            average[22] = average[22]+1;
                            break;
                        case 23:
                            speedValue[23] = speedValue[23]+kecepatan;
                            average[23] = average[23]+1;
                            break;
                    }

                    Log.v("Hasil", "Ke-"+ n + " : " + kecepatan + " - " + jam);


                }

                int success = json.getInt("success");

                if (success == 1){
                    return "OK";
                }else{
                    return "fail";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.container)).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
            if(result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(SpeedActivity.this, "Data kecepatan tidak ditemukan.", Toast.LENGTH_LONG).show();;
            } else if(result.equalsIgnoreCase("fail")) {
                Toast.makeText(SpeedActivity.this, "Tidak dapat mendapatkan data kecepatan. Silahkan ulangi kembali!", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(SpeedActivity.this, "Berhasil mendapatkan kecepatan.", Toast.LENGTH_LONG).show();
            }
        }

    }

}
