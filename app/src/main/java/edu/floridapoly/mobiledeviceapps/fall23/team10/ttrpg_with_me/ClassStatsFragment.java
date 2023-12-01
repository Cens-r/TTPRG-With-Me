package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.ims.RcsUceAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ClassStatsFragment extends Fragment {
    Character character;
    List<Item> abilityList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        int characterId = intent.getIntExtra("CharacterId", -1);
        if (characterId < 0) {
            getActivity().finish();
        }
        character = (Character) Character.getObject(Character.class, characterId - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class_stats, container, false);
        if (character.Abilities != null) {
            abilityList = character.Abilities;
        } else {
            abilityList = new ArrayList<>();
        }

        recyclerView = rootView.findViewById(R.id.classstats_recycler_body);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ItemAdapter(abilityList, getContext());
        recyclerView.setAdapter(recyclerAdapter);
        return rootView;
    }
}