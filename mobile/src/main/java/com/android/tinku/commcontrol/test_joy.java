package com.android.tinku.commcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class test_joy extends AppCompatActivity {

    public SharedPreferences Prefs;
    private int angle =  0;
    private int strength = 0;
    private int throttlePower = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_joy);

        Intent startUdpService = new Intent(this, UdpService.class);

        Prefs = getSharedPreferences(ApplicationConstants.ApplicationPrefs, Context.MODE_PRIVATE);
        Prefs.edit().putString(ApplicationConstants.DirectionKey,"0,0,0").commit();
        Prefs.edit().putBoolean(ApplicationConstants.ServiceStartKey,true).commit();
        this.startService(startUdpService);
        final SeekBar throttle = (SeekBar) findViewById(R.id.throttle);

        throttle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                SetData(angle, strength, throttle.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_left);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int _angle, int _strength)
            {
                angle = _angle;
                strength = _strength;
                if ( angle == 0 && strength == 0)
                {
                    SetData(angle, strength, throttle.getProgress());
                }
                else
                {
                    angle = angle + 90;
                    if (angle >= 360)
                        angle-= 360;
                    SetData(angle, strength, throttle.getProgress());
                }

            }
        });
    }

    private  void SetData(int angle, int strength, int height)
    {
        String data = String.format("%s,%s,%s",angle,strength,height);
        Intent startUdpService = new Intent(this, UdpService.class);
        Prefs.edit().putString(ApplicationConstants.DirectionKey,data).commit();
        Prefs.edit().putBoolean(ApplicationConstants.ServiceStartKey,true).commit();
        if (Prefs.getBoolean(ApplicationConstants.ServiceStartKey,true))
        {
            startService(startUdpService);
        }
    }
}
