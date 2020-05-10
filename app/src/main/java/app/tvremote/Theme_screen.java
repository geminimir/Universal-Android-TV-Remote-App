package app.tvremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class Theme_screen extends AppCompatActivity {
    SharedPreferences prefs;
    AdView mAdView;
    Vibrator vibrator;
    ImageButton black, white;
    public static final String MyPREFERENCES = "Themeprefs" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.toolbar_theme));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2269857695949235/9554859504");

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        black = (ImageButton)findViewById(R.id.black_theme);
        white = (ImageButton)findViewById(R.id.white_theme);

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("theme", "black");
                editor.apply();
                vibrator.vibrate(100);
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("theme", "white");
                editor.apply();
                vibrator.vibrate(100);
                Toast.makeText(Theme_screen.this, "White theme selected", Toast.LENGTH_SHORT).show();
            }
        });
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home :
                Theme_screen.this.finish();
                Intent i = new Intent(Theme_screen.this, Main_screen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
