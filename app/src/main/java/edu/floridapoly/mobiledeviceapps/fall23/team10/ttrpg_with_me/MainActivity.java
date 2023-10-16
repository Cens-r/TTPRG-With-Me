package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.icu.util.Freezable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.BottomNavBar);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.MainHost, new CharacterSelect()).commit();

    }
    public final NavigationBarView.OnItemSelectedListener navListener = item -> {

        Fragment selected = null;
        if(item.getItemId() == R.id.nav_backpack)
        {
            selected = new Backpack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.MainHost, new Backpack()).commit();
        }
        else if (item.getItemId() == R.id.nav_characterSelect) {
            selected = new CharacterSelect();
        }
        else if (item.getItemId() == R.id.nav_characterStats) {
        selected = new CharacterStats();
        }
        else if (item.getItemId() == R.id.nav_quickActions) {
        selected = new QuickActions();
        }
        else if (item.getItemId() == R.id.nav_classStats) {
            selected = new ClassStats();
        }

        if(selected != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.MainHost, selected).commit();
        }
        return true;
    };

}