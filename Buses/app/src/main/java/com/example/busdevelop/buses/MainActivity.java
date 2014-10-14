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


public class MainActivity extends ActionBarActivity {

    private final String mPrefs_Name = "MyPrefsFile";
    private String[] mOpcionesMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpcionesMenu = new String[] {"Editar cuenta", "Buscar", "Opción 3"};
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
/*                    case 2:
                        fragment = new Fragment2();
                        break;
                    case 3:
                        fragment = new Fragment3();
                        break;
*/                }

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
        if (settings.getBoolean("SinRegistrar",true)){

            Log.d("Comments", "No se ha registrado");
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            Log.d("comments",""+settings.getString("UserEmail",""));
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

    public void iniciarActivityMenu(Class activityClass){

        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
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

}
