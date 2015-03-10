package mycompany.googlegeocoding;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback{
    MapFragment mapFragment;
    GoogleMap Map;

    GeocodeTalking g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Map = mapFragment.getMap();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Address, City, State");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String temp){
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String temp){
                try {
                    if (temp.length() != 0) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);

                        g=new GeocodeTalking(temp,Map);
                        return true;
                    }
                }
                catch (Exception e){
                    System.out.println(e);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        LatLng new2Location=new LatLng(30.285117,-97.734100);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new2Location, 15));
        Marker ut =map.addMarker(new MarkerOptions()
                .position(new2Location)
                .title("University of Texas")
                .draggable(true));
        UiSettings i = map.getUiSettings();
        i.setZoomControlsEnabled(true);
        i.setMyLocationButtonEnabled(true);
    }

}