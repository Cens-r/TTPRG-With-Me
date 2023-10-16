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
import android.widget.Toast;

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
        ImageButton importButton = (ImageButton) getView().findViewById(R.id.importButton);
        ImageButton addButton = (ImageButton) getView().findViewById(R.id.addButton);

        ImageButton deleteChar1 = (ImageButton) getView().findViewById(R.id.deleteCharacter1);
        TextView characterName1 = (TextView) getView().findViewById(R.id.characterName1);

        ImageButton deleteChar2 = (ImageButton) getView().findViewById(R.id.deleteCharacter2);
        TextView characterName2 = (TextView) getView().findViewById(R.id.characterName2);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Implicit action, used to import character!", Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Adds character!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteChar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Deletes character!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteChar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Deletes character!", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_select, container, false);
    }
}