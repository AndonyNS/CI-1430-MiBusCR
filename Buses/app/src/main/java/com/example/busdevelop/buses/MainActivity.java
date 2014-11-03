package com.example.busdevelop.buses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.plus.Plus;


public class MainActivity extends ActionBarActivity {

    private final String mPrefs_Name = "MyPrefsFile";
    private static boolean spubIni = true;
    private String[] mOpcionesMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private InterstitialAd interstitial;
    private GoogleApiClientSing mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mOpcionesMenu = new String[]{"Editar cuenta", "Buscar", "Cerrar Sesión"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(
                getSupportActionBar().getThemedContext(),
//                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
//                android.R.layout.simple_list_item_activated_1 :
                android.R.layout.simple_list_item_1, mOpcionesMenu));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        iniciarActivityMenu(EditarCuentaActivity.class);
                        break;
                    case 1:
                        iniciarActivityMenu(ObtRutasActivity.class);
                        break;
                    case 2:
                        logoutAndRestart();
                        break;
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new MainMenuFragment())
                    .commit();
        }

        SharedPreferences settings = getSharedPreferences(mPrefs_Name, 0);
        Intent intent;
        if (settings.getBoolean("SinRegistrar", true)) {

            Log.d("Comments", "No se ha registrado");
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Log.d("comments", "" + settings.getString("UserEmail", ""));
        }
        if (spubIni){
            // Create the interstitial.
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId("ca-app-pub-7772032558547848/9595547219");

            // Create ad request.
            AdRequest adRequest = new AdRequest.Builder().build();

            // Begin loading your interstitial.
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    displayInterstitial();
                }
            });
            spubIni = false;
        }

    }
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void iniciarRutas(View view){

        Intent intent = new Intent(this, RutasActivity.class);
        startActivity(intent);
    }

    public void iniciarFavoritos(View view){

        Intent intent = new Intent(this, FavoritosActivity.class);
        startActivity(intent);
    }

    public void iniciarUnderContruction(View view){

        Intent intent = new Intent(this, UnderConstructionActivity.class);
        startActivity(intent);
    }

    public void iniciarMiUbicacion(View view){
        Intent intent = new Intent(this, MiUbicacionActivity.class);
        startActivity(intent);
    }

    public void iniciarActivityMenu(Class activityClass){

        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void logoutAndRestart(){
        // Cerrar sesion de google
        mGoogleApiClient = GoogleApiClientSing.getInstancia();
        if(mGoogleApiClient.getGoogleApiClient() != null) {
            if (mGoogleApiClient.getGoogleApiClient().isConnected()) {
                Toast.makeText(getBaseContext(), "Sesion de Google finalizada", Toast.LENGTH_SHORT).show();
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient.getGoogleApiClient());
                mGoogleApiClient.getGoogleApiClient().disconnect();

            }

        }
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("UserEmail");
        editor.remove("SinRegistrar");
        editor.remove("UserPass");
        editor.commit();
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
            mAdView = (AdView) getView().findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /** Called when leaving the activity */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /** Called when returning to the activity */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /** Called before the activity is destroyed */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }


    }


}
