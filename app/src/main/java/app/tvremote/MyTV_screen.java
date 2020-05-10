package app.tvremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by user on 06/11/2016.
 */
public class MyTV_screen extends AppCompatActivity {
    AdView mAdView;
   ListView list;
    private ArrayAdapter<String> listAdapter ;
    String my_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytv_layout);

        final ArrayList<String> my_tv_list = new ArrayList<String>();

        Toast.makeText(MyTV_screen.this, getString(R.string.toolbar_mytv), Toast.LENGTH_SHORT).show();
        loadArray(getApplicationContext(), my_tv_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            my_tv = extras.getString("name", "");
        }
        list = (ListView)findViewById(R.id.listview1);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.toolbar_mytv));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(my_tv != null)
         my_tv_list.add(my_tv);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my_tv_list);
        list.setAdapter(listAdapter);

        saveArray(my_tv_list);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2269857695949235/9554859504");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                Intent i = new Intent(MyTV_screen.this, MainActivity.class);
                i.putExtra("name", value);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String value = (String)parent.getItemAtPosition(position);
                for ( int i = 0;  i < my_tv_list.size(); i++){
                    String tempName = my_tv_list.get(i);
                    if(tempName.equals(value)){
                        my_tv_list.remove(i);
                        saveArray(my_tv_list);
                        loadArray(getApplicationContext(), my_tv_list);
                        listAdapter = new ArrayAdapter<String>(MyTV_screen.this, android.R.layout.simple_list_item_1, my_tv_list);
                        list.setAdapter(listAdapter);
                        break;
                    }
                }
                Toast.makeText(MyTV_screen.this, value + " removed from MyTV", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private  boolean saveArray(ArrayList<String> sKey)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* sKey is an array */
        mEdit1.putInt("Status_size", sKey.size());

        for(int i=0;i<sKey.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, sKey.get(i));
        }

        return mEdit1.commit();
    }

    private  void loadArray(Context mContext, ArrayList<String> sKey)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            sKey.add(mSharedPreference1.getString("Status_" + i, null));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mytv, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home :
                MyTV_screen.this.finish();
                Intent i = new Intent(MyTV_screen.this, Main_screen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.add:
                MyTV_screen.this.finish();
                Intent a = new Intent(MyTV_screen.this, List_screen.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
