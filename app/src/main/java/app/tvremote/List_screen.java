package app.tvremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class List_screen extends AppCompatActivity  implements SearchView.OnQueryTextListener{

    boolean enabled = true;
    AdView mAdView;
    String[] TvBrands = {"Acer","Admiral","Advent", "Agath Mirror", "Aiwa", "Akai", "Alba", "AOC", "Apex", "AudioSonic", "Audiovox", "AVQL", "AWA", "Ban & Olufsen", "Beko", "BenQ", "Blaupunkt", "Brandt",
                         "Broksonic", "Cello", "CIS BOX", "Citizen", "Coby", "Condor", "Conrac", "Continental Edison", "Daewoo", "Dell", "Durabrand", "Electric", "Electrohome", "Element", "Emerson", "Emprex",
                         "Fast", "Finlux", "Fisher", "Fujitsu", "Funai", "GE", "GoldStar", "Goodmans", "Gosonic", "Gradiente", "Grundig", "Haier", "Hannspree", "Hanseatic", "Hewlett Packard", "Hitachi",
                         "Insignia", "Jensen", "JVC", "Kendo", "Kogan", "Konka", "LG", "Loewe", "Luxor", "LXI", "Magnavox", "Marantz", "Matsui", "Memorex", "Metz", "Micromax", "Mitsubishi", "Mivar", "MT Logic", "NEC", "NET",
                         "Nordmende", "Oceanic", "Onida", "Orion", "Panasonic", "Philco", "Philips", "Pilot", "Pioneer", "Planar", "Palaroid", "Prima", "Proscan", "Proton", "RCA", "Revox", "Salora", "Sampo", "Samsung",
                         "Sansui", "Sanyo", "Sceptre", "Schaub Lorenz", "SEG", "Seiki", "Seleco", "Seura", "Sharp", "Silo", "Silva Schneider", "Skyworth", "Sole", "Sony", "Soyo", "Supra", "Sylvania", "Symphonic",
                         "Tatung", "TCL", "Technika", "TechniSat", "Telefunken", "Thomson", "Thorn", "Toshiba", "Veon", "Vestel", "ViewSonic", "Vizio", "Vu", "Westinghouse", "Zenith"};
    private ArrayAdapter<String> listAdapter ;
    ListView listview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.toolbar_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(List_screen.this, getString(R.string.toast_list), Toast.LENGTH_SHORT).show();
        listview = (ListView)findViewById(R.id.listview);
        ArrayList<String> brandsList = new ArrayList<String>();
        brandsList.addAll( Arrays.asList(TvBrands) );

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, brandsList);


        listview.setAdapter( listAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                Intent i = new Intent(List_screen.this, MainActivity.class);
                i.putExtra("name", value);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String value = (String)parent.getItemAtPosition(position);
                Toast.makeText(List_screen.this, value + " added to MyTV", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(List_screen.this, MyTV_screen.class);
                i.putExtra("name", value);
                startActivity(i);
                return false;
            }
        });
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2269857695949235/9554859504");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setOnQueryTextListener(this);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home :
                List_screen.this.finish();
                Intent i = new Intent(List_screen.this, Main_screen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List_screen.this.listAdapter.getFilter().filter(newText);
        return false;
    }
}
