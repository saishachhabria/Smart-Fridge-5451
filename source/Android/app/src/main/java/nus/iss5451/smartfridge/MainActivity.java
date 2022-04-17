package nus.iss5451.smartfridge;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import nus.iss5451.smartfridge.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final int tempThreshold = 4;

    private AppBarConfiguration appBarConfiguration;

    private DataFetchingService dataFetchingService = null;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private Map<String,Object> recent_log = null;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                dataFetchingService = ((DataFetchingService.LocalBinder)iBinder).getService();
                DataFetchingService.MyCallback itemCallback = new DataFetchingService.MyCallback() {
                    @Override
                    public void onDataUpdate(Object data) {
                        itemArrayList = (ArrayList<Item>) data;
                        ListAdapter listAdapter = new ListAdapter(MainActivity.this,itemArrayList);
                        binding.listview.setAdapter(listAdapter);
                        binding.listview.setClickable(true);
                        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = listAdapter.getItem(position);

                                Calendar calendar = Calendar.getInstance();
                                if(!item.expiredDate.equals("")){
                                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    try {
                                        Date ed = ft.parse(item.expiredDate);
                                        assert ed != null;
                                        calendar.setTime(ed);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        String month = (i1 + 1) < 10?"0"+(i1+1):(i1+1)+"";
                                        String day = i2 < 10?"0"+i2:i2+"";
                                        item.expiredDate = i +"-"+month+"-"+day + " 00:00:00";
                                        listAdapter.notifyDataSetChanged();
                                        dataFetchingService.updateItem(itemArrayList, true, (error, ref) -> Log.d("[MainActivity]","update Complete!"));
                                    }
                                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                                dialog.show();

                            }
                        });

                    }

                    @Override
                    public void onDataCanceled(DatabaseError error) {

                    }
                };
                dataFetchingService.setItemCallback(itemCallback);

                DataFetchingService.MyCallback historyCallback = new DataFetchingService.MyCallback() {
                    @Override
                    public void onDataUpdate(Object data) {
                        recent_log = (Map<String, Object>) data;
                        double temp,humid;
                        temp = (double) recent_log.get("temperature");
                        humid = (double) recent_log.get("humidity");

                        if (temp > tempThreshold){
                            binding.TemperatureValue.setTextColor(Color.RED);
                        }
                        if (humid > 70 || humid < 20){
                            binding.HumidityValue.setTextColor(Color.RED);
                        }


                        SetDataDisplay(temp, 0, binding.TemperatureValue, "℃");
                        SetDataDisplay(humid, 1,binding.HumidityValue, "%");
                    }

                    private void SetDataDisplay(double value, int decimalPlace, TextView view, String suffix){
                        view.setText(String.format("%." + Integer.toString(decimalPlace) + "f", value) + suffix);
                    }


                    @Override
                    public void onDataCanceled(DatabaseError error) {

                    }
                };
                dataFetchingService.setLogCallback(historyCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                dataFetchingService = null;
            }
        };
        bindService(new Intent(this,DataFetchingService.class),serviceConnection,BIND_AUTO_CREATE);

        setSupportActionBar(binding.toolbar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}