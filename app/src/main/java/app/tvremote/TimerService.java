package app.tvremote;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by user on 10/11/2016.
 */
public class TimerService extends Service {
    InterstitialAd interstitial;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final int interval = 420000; // 7 minutes

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.interstitial_ad));
        final AdRequest request = new AdRequest.Builder().build();

         Handler handler = new Handler();
         Runnable runnable = new Runnable(){
            public void run() {
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
            }
        };
        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
        return super.onStartCommand(intent, flags, startId);
    }

}
