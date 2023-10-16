package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.reflect.Array;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickActions extends Fragment {
    public QuickActions() {
        // Required empty public constructor
    }

    public static QuickActions newInstance(String param1, String param2) {
        QuickActions fragment = new QuickActions();
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

        View rootView = inflater.inflate(R.layout.fragment_quick_actions, container, false);

        ImageButton action1 = (ImageButton) rootView.findViewById(R.id.general_gotoButton);
        ImageButton action2 = (ImageButton) rootView.findViewById(R.id.character_gotoButton);
        ImageButton action3 = (ImageButton) rootView.findViewById(R.id.class_gotoButton);
        ImageButton action4 = (ImageButton) rootView.findViewById(R.id.backpack_gotoButton);

        ImageButton[] actionArray = {action1, action2, action3, action4};
        for (int i = 0; i < actionArray.length; i++) {
            actionArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Will perform some quick action!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return rootView;
    }
}