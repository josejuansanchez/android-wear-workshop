package org.josejuansanchez.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "Wear-Activity";
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
            }
        });

        viewSensorList();
        //initGravitySensor();
    }

    private void viewSensorList() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for(int i=0; i<deviceSensors.size(); i++) {
            Log.d(TAG, "** Sensor: " + deviceSensors.get(i).toString());
        }
    }

    private void initGravitySensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        } else {
            Log.d(TAG, "Sorry, this sensor is not available in this device");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "timestamp: " + event.timestamp);
        Log.d(TAG, "values[0]: " + event.values[0]);
        Log.d(TAG, "values[1]: " + event.values[1]);
        Log.d(TAG, "values[2]: " + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
