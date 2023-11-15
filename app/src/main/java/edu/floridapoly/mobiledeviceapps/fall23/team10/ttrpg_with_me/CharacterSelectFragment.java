package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterSelectFragment extends Fragment {
    public CharacterSelectFragment() {
        // Required empty public constructor
    }
    public static CharacterSelectFragment newInstance(String param1, String param2) {
        CharacterSelectFragment fragment = new CharacterSelectFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_character_select, container, false);

        ImageButton import_button = (ImageButton) rootView.findViewById(R.id.charselect_button_import);
        ImageButton create_button = (ImageButton) rootView.findViewById(R.id.charselect_button_create);

        import_button.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Prompts file upload, for character import!", Toast.LENGTH_SHORT).show();
        });

        create_button.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Adds character!", Toast.LENGTH_SHORT).show();
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}