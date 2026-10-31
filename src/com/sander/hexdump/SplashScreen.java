package com.sander.hexdump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setTextColor(Color.parseColor("#00FF00"));
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextSize(16f);
        tv.setPadding(50, 50, 50, 50);
        tv.setText("root@sanderhexdump:~# boot\n\n");
        tv.setBackgroundColor(Color.BLACK);
        setContentView(tv);

        tv.append("[*] Initializing SanderCore...\n");
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 1500); // Jeda 1.5 detik, langsung masuk
    }
}
