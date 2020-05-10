package app.tvremote;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    boolean enabled = true;
    TextView brandname;
    ImageView vol, prg;
    String power, brand, one, two, three, four, five, six, seven, eight, nine, zero, enter, chup, chdown, volplus, voldown, menu, up, down, left, right, mute;
    ImageButton powerbutton, plusbutton, mutebutton, volupbutton, voldownbutton, leftbutton, rightbutton, upbutton, downbutton, okbutton, infobutton, cancelbutton, progupbutton, progdownbutton;
    ConsumerIrManager irManager;
    Vibrator vibrator;
    Bundle extras;
    RelativeLayout digits_layout;

    SharedPreferences prefs;
    public static final String MyPREFERENCES = "Themeprefs" ;
    InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (prefs.getString("theme", "black").equals("black")) {
            setContentView(R.layout.activity_main);
        } else if (prefs.getString("theme", "black").equals("white")) {
            setContentView(R.layout.activity_main_white);
        }
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.interstitial_ad));
        AdRequest request = new AdRequest.Builder().build();

        interstitial.loadAd(request);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else {
                    //Begin Game, continue with app
                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        vol = (ImageView)findViewById(R.id.volimageView);
        prg = (ImageView)findViewById(R.id.prgimageView);

        digits_layout = (RelativeLayout)findViewById(R.id.digits);
        digits_layout.setVisibility(View.INVISIBLE);

        brandname = (TextView)findViewById(R.id.brandname);
        extras = getIntent().getExtras();
        if(extras != null) {
            brand = extras.getString("name");
            SplitFunctions(ReadFromFile("Tvs/" +  brand + ".txt"));
        }

        getSupportActionBar().setTitle(brand);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        irManager = (ConsumerIrManager)getSystemService(CONSUMER_IR_SERVICE);
        ButtonsDeclaration();
    }


    private void ButtonsDeclaration() {
        powerbutton = (ImageButton)findViewById(R.id.buttonpower);
        plusbutton = (ImageButton)findViewById(R.id.plusbutton);
        mutebutton = (ImageButton)findViewById(R.id.mutebutton);
        volupbutton = (ImageButton)findViewById(R.id.volup);
        voldownbutton = (ImageButton)findViewById(R.id.voldown);
        progdownbutton = (ImageButton)findViewById(R.id.chup);
        progupbutton = (ImageButton)findViewById(R.id.chdown);
        leftbutton = (ImageButton)findViewById(R.id.left);
        rightbutton = (ImageButton)findViewById(R.id.right);
        upbutton = (ImageButton)findViewById(R.id.up);
        downbutton = (ImageButton)findViewById(R.id.down);
        okbutton = (ImageButton)findViewById(R.id.Ok);
        infobutton = (ImageButton)findViewById(R.id.menu);
        cancelbutton = (ImageButton)findViewById(R.id.exit);

        powerbutton.setOnClickListener(this);
        plusbutton.setOnClickListener(this);
        mutebutton.setOnClickListener(this);
        voldownbutton.setOnClickListener(this);
        volupbutton.setOnClickListener(this);
        progupbutton.setOnClickListener(this);
        progdownbutton.setOnClickListener(this);
        leftbutton.setOnClickListener(this);
        rightbutton.setOnClickListener(this);
        upbutton.setOnClickListener(this);
        downbutton.setOnClickListener(this);
        okbutton.setOnClickListener(this);
        infobutton.setOnClickListener(this);
        cancelbutton.setOnClickListener(this);

    }
    MenuItem enableitem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        // Create your menu...
        enableitem = menu.findItem(R.id.enable);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            MainActivity.this.finish();
            return true;
        }
        else if(id == R.id.enable) {
                if(!enabled) {
                    enabled = true;
                    enableitem.setTitle("Disable vibration");
                }
                else {
                    enabled = false;
                    enableitem.setTitle("Enable vibration");
                }

        }

        return super.onOptionsItemSelected(item);
    }

    protected String hex2dec(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

    protected String count2duration(String countPattern) {
         String TAG = "app.tvremote";
        List<String> list = new ArrayList<String>(Arrays.asList(countPattern.split(",")));
        int frequency = Integer.parseInt(list.get(0));
        int pulses = 1000000/frequency;
        int count;
        int duration;

        list.remove(0);

        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i));
            duration = count * pulses;
            list.set(i, Integer.toString(duration));
        }

        String durationPattern = "";
        for (String s : list) {
            durationPattern += s + ",";
        }

     //   Log.d(TAG, "Frequency: " + frequency);
          Log.d(TAG, "Duration Pattern: " + durationPattern);

        return durationPattern;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void ConvertToIntArray(String irstring) {
        String individualValue;
        String[] splits =  irstring.split(",");
        int[] number = new int[splits.length];

        for (int i = 0; i < splits.length; i++) {

            individualValue = splits[i];
            int parsedValue = Integer.parseInt(individualValue);
            number[i] = parsedValue;
        }
        try {
            irManager.transmit(38400, number);
        } catch (UnsupportedOperationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String ReadFromFile(String filename) {

        String text;
        try {
            InputStream is = getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

             text = new String(buffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }
    private void SplitFunctions(String fileContent) {

        String[] splits = fileContent.split("\"");
        power = splits[1];
        one = splits[3];
        two = splits[5];
        three = splits[7];
        four = splits[9];
        five = splits[11];
        six = splits[13];
        seven = splits[15];
        eight = splits[17];
        nine = splits[19];
        zero = splits[21];
        enter = splits[23];
        chup = splits[25];
        chdown = splits[27];
        volplus = splits[29];
        voldown = splits[31];
        menu = splits[33];
        up = splits[35];
        down = splits[37];
        left = splits[39];
      right = splits[41];
        mute = splits[43];

    }

    private void DigitDialog() {
        ImageButton button0 = (ImageButton) findViewById(R.id.button0);
        ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        ImageButton button5 = (ImageButton) findViewById(R.id.button5);
        ImageButton button6 = (ImageButton) findViewById(R.id.button6);
        ImageButton button7 = (ImageButton) findViewById(R.id.button7);
        ImageButton button8 = (ImageButton) findViewById(R.id.button8);
        ImageButton button9 = (ImageButton) findViewById(R.id.button9);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(!irManager.hasIrEmitter())
        Toast.makeText(MainActivity.this, "Your device doesn't support Infrared", Toast.LENGTH_SHORT).show();

        if(enabled)
        vibrator.vibrate(100);
        switch (v.getId())
        {
            case R.id.buttonpower:
                if(!power.equals("0"))
                ConvertToIntArray(count2duration(hex2dec(power)));
                break;
            case R.id.mutebutton:
                if(!mute.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(mute)));
                break;
            case R.id.volup:
                if(!volplus.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(volplus)));
                break;
            case R.id.voldown:
                if(!voldown.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(voldown)));
                break;
            case R.id.chdown:
                if(!chdown.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(chdown)));
                break;
            case R.id.chup:
                if(!chup.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(chup)));
                break;
            case R.id.menu:
                if(!menu.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(menu)));
                break;
            case R.id.left:
                if(!left.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(left)));
                break;
            case R.id.right:
                if(!right.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(right)));
                break;
            case R.id.up:
                if(!up.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(up)));
                break;
            case R.id.down:
                if(!down.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(down)));
                break;
            case R.id.Ok:
                if(!enter.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(enter)));
                break;
            case R.id.exit:
                if(!menu.equals("0"))
                    ConvertToIntArray(count2duration(hex2dec(menu)));
                break;
            case R.id.plusbutton:
                DigitDialog();
                if(digits_layout.getVisibility() == View.INVISIBLE) {
                    digits_layout.setVisibility(View.VISIBLE);
                    progdownbutton.setVisibility(View.INVISIBLE);
                    progupbutton.setVisibility(View.INVISIBLE);
                    volupbutton.setVisibility(View.INVISIBLE);
                    voldownbutton.setVisibility(View.INVISIBLE);
                    progdownbutton.setVisibility(View.INVISIBLE);
                    progdownbutton.setVisibility(View.INVISIBLE);
                    vol.setVisibility(View.INVISIBLE);
                    prg.setVisibility(View.INVISIBLE);
                }
                else {
                    digits_layout.setVisibility(View.INVISIBLE);
                    progdownbutton.setVisibility(View.VISIBLE);
                    progupbutton.setVisibility(View.VISIBLE);
                    volupbutton.setVisibility(View.VISIBLE);
                    voldownbutton.setVisibility(View.VISIBLE);
                    progdownbutton.setVisibility(View.VISIBLE);
                    progdownbutton.setVisibility(View.VISIBLE);
                    vol.setVisibility(View.VISIBLE);
                    prg.setVisibility(View.VISIBLE);
                }
                break;
            case  R.id.button0:
                ConvertToIntArray(count2duration(hex2dec(zero)));
                break;
            case  R.id.button1:
                ConvertToIntArray(count2duration(hex2dec(one)));
                break;
            case  R.id.button2:
                ConvertToIntArray(count2duration(hex2dec(two)));
                break;
            case  R.id.button3:
                ConvertToIntArray(count2duration(hex2dec(three)));
                break;
            case  R.id.button4:
                ConvertToIntArray(count2duration(hex2dec(four)));
                break;
            case  R.id.button5:
                ConvertToIntArray(count2duration(hex2dec(five)));
                break;
            case  R.id.button6:
                ConvertToIntArray(count2duration(hex2dec(six)));
                break;
            case  R.id.button7:
                ConvertToIntArray(count2duration(hex2dec(seven)));
                break;
            case  R.id.button8:
                ConvertToIntArray(count2duration(hex2dec(eight)));
                break;
            case  R.id.button9:
                ConvertToIntArray(count2duration(hex2dec(nine)));
                break;
        }
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}

