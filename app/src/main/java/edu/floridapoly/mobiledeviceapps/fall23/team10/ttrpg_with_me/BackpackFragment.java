package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackpackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackpackFragment extends Fragment {
    public BackpackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Backpack.
     */
    // TODO: Rename and change types and number of parameters
    public static BackpackFragment newInstance(String param1, String param2) {
        BackpackFragment fragment = new BackpackFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_backpack, container, false);

        Button button1 = (Button) rootView.findViewById(R.id.button);
        Button button2 = (Button) rootView.findViewById(R.id.button2);
        Button button3 = (Button) rootView.findViewById(R.id.button3);
        Button button4 = (Button) rootView.findViewById(R.id.button4);

        Button[] buttonArray = {button1, button2, button3, button4};
        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Will open up more info about the item!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Inflate the layout for this fragment
        return rootView;
    }
}