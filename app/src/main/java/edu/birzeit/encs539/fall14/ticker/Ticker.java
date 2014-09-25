package edu.birzeit.encs539.fall14.ticker;

import android.os.Bundle;
import android.app.Activity;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ticker extends Activity {
    TextView start;
    TextView elapsed;
    long startMS;
    long endMS;
    Time joe = new Time();
    TimePasser timePasser;
    String startText;
    String stopText;
    FaceView faceView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);
        start = (TextView)findViewById(R.id.startTime);
        elapsed = (TextView)findViewById(R.id.elapsedTime);
        startText = getResources().getString(R.string.start);
        stopText = getResources().getString(R.string.stop);
        faceView = (FaceView)findViewById(R.id.imageView1);
        button =  (Button)findViewById(R.id.startStop);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(
                        startText)) { // starting
                    startMS = System.currentTimeMillis();
                    faceView.setStartTime(startMS);
                    elapsed.setText("");
                    joe.set(startMS);
                    int h = joe.hour;
                    int m = joe.minute;
                    int s = joe.second;
                    start.setText(" " + (h / 10) + (h % 10) + ":" + (m / 10) + (m % 10) + ":" + (s / 10) + (s % 10));
                    b.setText(stopText);
                } else {// finished timing
                    endMS = System.currentTimeMillis();
                    long ms = endMS - startMS;
                    String t = Long.toString(ms / 1000) + "."
                            + ((ms % 1000) / 100) // tenths digit
                            + ((ms % 100) / 10)  // hundredths digit
                            + ((ms % 10));        // thousandths of sec digit
                    elapsed.setText(t);
                    b.setText(startText);
                }

            }

        });

        faceView.setStartTime(System.currentTimeMillis());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticker, menu);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        timePasser.stopThread = true;
    }
    @Override
    public void onResume(){
        super.onResume();
        timePasser = new TimePasser(button,stopText, faceView, this);
        timePasser.stopThread = false;
        timePasser.start();

    }

}
class TimePasser extends Thread {
    Button b;
    String stopText;
    FaceView faceView;
    Ticker context;
    boolean stopThread = false;

    TimePasser(Button x, String s, FaceView v, Ticker c){
        b = x;
        stopText = s;
        faceView = v;
        context =c;
    }

    @Override
    public void run(){
        while (true) {
            try {
                sleep(1000);
            }catch(Exception e){e.printStackTrace();}

            if (b.getText().equals(stopText)) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        faceView.invalidate();
                    }
                });
            }
            if (stopThread) {
                context.timePasser = null;
                return;
            }
        }
    }
}
