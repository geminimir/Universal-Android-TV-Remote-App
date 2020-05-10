package app.tvremote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by user on 04/11/2016.
 */
public class Main_screen extends AppCompatActivity implements View.OnClickListener{

    Button mytv, brands, about, theme;
    SharedPreferences prefs;
    public static final String MyPREFERENCES = "Themeprefs" ;


    ConsumerIrManager irManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        try {
            irManager = (ConsumerIrManager)getSystemService(CONSUMER_IR_SERVICE);
            irManager.hasIrEmitter();

        }catch (NullPointerException e) {
            Toast.makeText(this, "Device does not support IR transmission", Toast.LENGTH_LONG).show();

        }

        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mytv = (Button)findViewById(R.id.mytv);
        theme = (Button)findViewById(R.id.theme);
        brands = (Button)findViewById(R.id.brands);
        about = (Button)findViewById(R.id.about);

        mytv.setOnClickListener(this);
        brands.setOnClickListener(this);
        theme.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.about:
                Intent i = new Intent(Main_screen.this, About_screen.class);
                startActivity(i);
                break;
            case R.id.mytv:
                Intent m = new Intent(Main_screen.this, MyTV_screen.class);
                startActivity(m);
                break;
            case R.id.theme:
                Intent t = new Intent(Main_screen.this, Theme_screen.class);
                startActivity(t);
                break;
            case R.id.brands:
                Intent b = new Intent(Main_screen.this, List_screen.class);
                startActivity(b);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Rate Us");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        alertDialogBuilder.setNegativeButton("No, Thanks",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Main_screen.this.finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(Main_screen.this, TimerService.class);
        stopService(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent i = new Intent(Main_screen.this, TimerService.class);
        stopService(i);
    }
}
