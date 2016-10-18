package com.connollyed.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean timer_running = false;
    private CountDownTimer egg_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar seek_time = (SeekBar) findViewById(R.id.timeSeek);
        final TextView time_text = (TextView) findViewById(R.id.timeText);

        seek_time.setMax(10*60);    //Set max to 10 mins
        seek_time.setProgress(60);  //Set Progress to 1 min

        // Update Timer Text when seekbar changes
        seek_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int time = seek_time.getProgress();
                String str_time = secondsToMinutes(time);
                time_text.setText(str_time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void run(View view) {
        final Button timer_button = (Button) findViewById(R.id.timerButton);
        final SeekBar seek_time = (SeekBar) findViewById(R.id.timeSeek);
        final TextView time_text = (TextView) findViewById(R.id.timeText);

        if(timer_running == false) {
            timer_running = true;
            // Set UI Controls
            timer_button.setText("STOP");
            seek_time.setEnabled(false);

            int start_time = seek_time.getProgress();
            egg_timer = new CountDownTimer(start_time * 1000 + 100, 1000) {

                public void onTick(long msUntilFinsihed) {
                    //Update Timer Text
                    time_text.setText(secondsToMinutes((int) msUntilFinsihed / 1000));
                }

                public void onFinish() {
                    //Countdown Finished Sound Alarm
                    time_text.setText(secondsToMinutes(0));
                    MediaPlayer sound = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    sound.start();

                    resetTimer();
                }
            }.start();
        } else {
            resetTimer();
        }
    }

    /**
     * Converts time from seconds to equivalant minutes and seconds
     * @param seconds
     * @return A string of minutes and seconds
     */
    private String secondsToMinutes(int seconds){
        int min = seconds/60;
        int sec = seconds%60;

        //Get minutes and prepend 0 to minutes if needed
        String str_minutes = String.valueOf(min);

        //Get seconds and append 0 if needed
        String str_secs = String.valueOf(sec);
        if(str_secs.length() == 1)
            str_secs = "0" + String.valueOf(sec);

        String time = str_minutes + ":" + str_secs;
        return time;
    }
    public void resetTimer(){
        final SeekBar seek_time = (SeekBar) findViewById(R.id.timeSeek);
        final TextView time_text = (TextView) findViewById(R.id.timeText);
        final Button timer_button = (Button) findViewById(R.id.timerButton);

        // Set UI Controls
        seek_time.setProgress(60);  //Set Progress to 1 min
        time_text.setText("1:00");  //Set Time
        timer_button.setText("RUN TIMER");
        seek_time.setEnabled(true);
        if(egg_timer != null)
            egg_timer.cancel();
        timer_running = false;
    }
}
