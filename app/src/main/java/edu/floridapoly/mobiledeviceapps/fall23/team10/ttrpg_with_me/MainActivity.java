package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityLauncher;
    List<Character> characterList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        characterList = new ArrayList<>();
        recyclerView = findViewById(R.id.charselect_recycler_body);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new CharacterAdapter(characterList, this);
        recyclerView.setAdapter(recyclerAdapter);

        ImageButton import_button = findViewById(R.id.charselect_button_import);
        Button create_button = findViewById(R.id.charselect_button_create);

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Toast.makeText(this, "TODO: Retrieve info from file!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        import_button.setOnClickListener(this::openFile);
        create_button.setOnClickListener(view -> {
            showDialog();
        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_character_create);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        EditText nameInput = dialog.findViewById(R.id.createcharacter_edittext_name);
        EditText raceInput = dialog.findViewById(R.id.createcharacter_edittext_race);
        EditText classInput = dialog.findViewById(R.id.createcharacter_edittext_class);

        AppCompatButton createButton = dialog.findViewById(R.id.createcharacter_button_create);
        createButton.setOnClickListener(view -> {
            String name = String.valueOf(nameInput.getText());
            String race = String.valueOf(raceInput.getText());
            String classname = String.valueOf(classInput.getText());

            if (!name.isEmpty() && !race.isEmpty() && !classname.isEmpty()) {
                ClassArchetype classArc = new ClassArchetype(classname);

                Character newCharacter = new Character(name, race, classArc);
                newCharacter.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSaHmgseRqO6CI14XWSh5swCN19tzNhtgptvg&usqp=CAU");
                characterList.add(newCharacter);
                recyclerAdapter.notifyDataSetChanged();

                Toast.makeText(this, "TODO: Connect to backend saving!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "One of the three required inputs is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        AppCompatButton closeButton = dialog.findViewById(R.id.createcharacter_button_close);
        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        activityLauncher.launch(intent);
    }
}