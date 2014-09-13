package com.example.mikhail.light;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Date;
import java.util.List;

import static android.os.SystemClock.sleep;


public class MainActivity extends Activity {
    private Switch mySwitch;
    private Button but;
    private ToggleButton tg;
    private boolean flicker = false;
    private volatile Thread myThread;
    Camera camera = Camera.open();
    Parameters param = camera.getParameters();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        but = (Button)findViewById(R.id.button4);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Light+")
                        .setMessage(
                                    "Programmer: Shvarev Mikhail\n" +
                                    "http://vk.com/shvarev_michael")
                        .setIcon(R.drawable.logo)
                        .setCancelable(false)
                        .setNegativeButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        tg = (ToggleButton)findViewById(R.id.toggleButton);
        tg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    flicker = true;
                else
                    flicker = false;
            }
        });
        mySwitch =(Switch)findViewById(R.id.switch2);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              //  myThread.start();
                if(isChecked)
                {tg.setEnabled(false);
                    if(flicker){
                            myThread = new Thread( // создаём новый поток
                                    new Runnable() { // описываем объект Runnable в конструкторе
                                        public void run() {
                                            int i = 0;
                                            while(!Thread.interrupted()){
                                                sleep(100);
                                                if(i%2 == 0)
                                                {
                                                    param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                                    camera.setParameters(param);
                                                }
                                                else
                                                {
                                                    param.setFlashMode(Parameters.FLASH_MODE_OFF);
                                                    camera.setParameters(param);
                                                }
                                                i++;
                                            }
                                            param.setFlashMode(Parameters.FLASH_MODE_OFF);
                                            camera.setParameters(param);
                                        }

                                    }
                            );
                            myThread.start();
                    }
                    else
                    {
                        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(param);
                    }
                }
                else
                {tg.setEnabled(true);
                     if(flicker)
                       myThread.interrupt();
                        else{
                        param.setFlashMode(Parameters.FLASH_MODE_OFF);
                        camera.setParameters(param);
                    }

                }
            }
        });

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
