package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView bottomNav = findViewById(R.id.BottomNavBar);

        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if(item.getItemId() == R.id.nav_backpack) {
                fragment = new BackpackFragment();
            } else if (item.getItemId() == R.id.nav_notes) {
                fragment = new NotesFragment();
            } else if (item.getItemId() == R.id.nav_characterStats) {
                fragment = new CharacterStatsFragment();
            } else if (item.getItemId() == R.id.nav_classStats) {
                fragment = new ClassStatsFragment();
            } else if (item.getItemId() == R.id.nav_characterSelect) {
                finish();
            }

            if (fragment != null) {
                replaceFragment(fragment);
                return true;
            }
            return false;
        });

        replaceFragment(new CharacterStatsFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainHost, fragment);
        fragmentTransaction.commit();
    }
}
