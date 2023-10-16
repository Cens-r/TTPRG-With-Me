package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView characterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.BottomNavBar);
        
        //bottomNav.setVisibility(View.INVISIBLE);
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if(item.getItemId() == R.id.nav_backpack) {
                fragment = new Backpack();
            } else if (item.getItemId() == R.id.nav_notes) {
                fragment = new Notes();
            } else if (item.getItemId() == R.id.nav_characterStats) {
                fragment = new CharacterStats();
            } else if (item.getItemId() == R.id.nav_quickActions) {
                fragment = new QuickActions();
            } else if (item.getItemId() == R.id.nav_classStats) {
                fragment = new ClassStats();
            }

            if (fragment != null) {
                replaceFragment(fragment);
                return true;
            }
            return false;
        });

        replaceFragment(new CharacterSelect());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainHost, fragment);
        fragmentTransaction.commit();
    }

    /* Unused function?? Not sure what this was for:
    public void onClick() {
        setContentView(R.layout.activity_main);
    }
     */
}