package capstoneproject.jatransit.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import capstoneproject.jatransit.MainActivity;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class Check extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", false);
        if (firstRun == false)//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
            Intent i = new Intent(this, SplashScreen.class);
            startActivity(i);
            finish();
        } else {

            Intent a = new Intent(this, MainActivity.class);
            startActivity(a);
            finish();
        }
    }
}

