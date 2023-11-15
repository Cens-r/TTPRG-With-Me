package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectFragment extends Fragment {
    ActivityResultLauncher<Intent> activityLauncher;

    List<Character> characterList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public CharacterSelectFragment() {}
    public static CharacterSelectFragment newInstance(String param1, String param2) {
        CharacterSelectFragment fragment = new CharacterSelectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterList = new ArrayList<>();

        ClassArchetype barbarian = new ClassArchetype("Barbarian", 5);
        ClassArchetype druid = new ClassArchetype("Druid", 2);

        Character c1 = new Character("Alderman Hervoet", "Human", barbarian);
        c1.setImageUrl("https://static.wikia.nocookie.net/clashofclans/images/4/48/Barbarian_face.png");
        characterList.add(c1);

        Character c2 = new Character("Vicar Joce", "Monkey", druid);
        c1.setImageUrl("https://static.wikia.nocookie.net/b__/images/7/79/Druid_Monkey.png");
        characterList.add(c2);

        Character c3 = new Character("Margrave Leofrick", "Orc", barbarian);
        c1.setImageUrl("https://i.imgur.com/ZsBDoKZ.jpg");
        characterList.add(c3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_character_select, container, false);

        // Setup Character List
        recyclerView = (RecyclerView) rootView.findViewById(R.id.charselect_recycler_body);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new CharacterAdapter(characterList, this.getContext());
        recyclerView.setAdapter(recyclerAdapter);

        ImageButton import_button = (ImageButton) rootView.findViewById(R.id.charselect_button_import);
        Button create_button = (Button) rootView.findViewById(R.id.charselect_button_create);

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Toast.makeText(getContext(), "Will retrieve info from file!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        import_button.setOnClickListener(this::openFile);
        create_button.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Adds character!", Toast.LENGTH_SHORT).show();
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        activityLauncher.launch(intent);
    }
}