package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

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
    List<DisplayObject> abilityList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class_stats, container, false);
        abilityList = new ArrayList<>();
        abilityList.add(new DisplayObject("Header Test", "This is a display body, this is pretty cool!", false));

        recyclerView = rootView.findViewById(R.id.classstats_recycler_body);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new DisplayAdapter(abilityList, getContext());
        recyclerView.setAdapter(recyclerAdapter);
        return rootView;
    }
}