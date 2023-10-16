package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterSelect extends Fragment {
    public CharacterSelect() {
        // Required empty public constructor
    }
    public static CharacterSelect newInstance(String param1, String param2) {
        CharacterSelect fragment = new CharacterSelect();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_select, container, false);
    }
}