package com.s23010459.thilina;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TempAlert extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;

    private float threshold = 59.0f; // SID last digits
    private MediaPlayer mediaPlayer;
    private TextView alertTitle;
    private TextView alertMsg;
    private View Background;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_alert);

        // Initialize media player
        mediaPlayer = MediaPlayer.create(this,R.raw.beep_warning_sound);

        // Acquire SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Acquire temperature sensor
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (temperatureSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float temperature = event.values[0 ];

        if (temperature > threshold) {
            // Play alarm or audio
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                alertTitle = findViewById(R.id.alertText);
                alertMsg = findViewById(R.id.alertMsgText);
                //change texts
                alertTitle.setText("Alert!");
                alertMsg.setText("Temperature is High");
                //change text colors
                alertTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
                alertMsg.setTextColor(ContextCompat.getColor(this, R.color.white));
                //change background color
                Background = findViewById(R.id.mainConstrainLayout);
                Background.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}