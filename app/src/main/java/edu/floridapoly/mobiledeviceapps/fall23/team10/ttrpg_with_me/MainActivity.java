package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityLauncher;
    List<Character> characterList;
    List<String> classList;

    DatabaseManager db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(this);

        characterList = new ArrayList<>();
        classList = new ArrayList<>();
        recyclerView = findViewById(R.id.charselect_recycler_body);
        recyclerView.setHasFixedSize(true);

        Cursor characterCursor = db.fetchAll("CHARACTERS");
        if (characterCursor.moveToFirst()) {
            do {
                int jsonIndex = characterCursor.getColumnIndex("JSON");
                String json = characterCursor.getString(jsonIndex);
                int pkIndex = characterCursor.getColumnIndex("pk");
                long pk = characterCursor.getLong(pkIndex);

                Character character = ClassManager.fromJson(json, Character.class);
                character.pk = pk;
                Character.IntializeItems(character);
                Character.trackObject(character);
                characterList.add(character);

                character.Backpack.forEach((key, value) -> {
                    Cursor itemCursor = db.getItems(pk, key);
                    if (itemCursor.moveToFirst()) {
                        do {
                            int itemJsonIndex = itemCursor.getColumnIndex("JSON");
                            String itemJson = itemCursor.getString(itemJsonIndex);

                            Item item = ClassManager.fromJson(itemJson, Item.class);
                            character.Backpack.get(key).add(item);
                        } while(itemCursor.moveToNext());
                    }
                });

                Cursor noteCursor = db.getItems(pk, "Notes");
                if (noteCursor.moveToFirst()) {
                    do {
                        int itemJsonIndex = noteCursor.getColumnIndex("JSON");
                        String itemJson = noteCursor.getString(itemJsonIndex);

                        Item item = ClassManager.fromJson(itemJson, Item.class);
                        character.Notes.add(item);
                    } while(noteCursor.moveToNext());
                }

                Log.d("Character", "Primary Key: " + character.pk);
                Cursor abilityCursor = db.getItems(pk, "Abilities");
                if (abilityCursor.moveToFirst()) {
                    do {
                        int itemJsonIndex = abilityCursor.getColumnIndex("JSON");
                        String itemJson = abilityCursor.getString(itemJsonIndex);
                        Log.d("Character", "ItemJson: "+itemJson);

                        Item item = ClassManager.fromJson(itemJson, Item.class);
                        character.Abilities.add(item);
                    } while(abilityCursor.moveToNext());
                }
                Log.d("Character", character.Abilities.toString());

            } while(characterCursor.moveToNext());
        }

        Cursor classCursor = db.fetchAll("CLASSES");
        if (classCursor.moveToFirst()) {
            do {
                int jsonIndex = classCursor.getColumnIndex("JSON");
                String json = classCursor.getString(jsonIndex);
                int pkIndex = classCursor.getColumnIndex("pk");
                long pk = classCursor.getLong(pkIndex);

                ClassArchetype classArc = ClassManager.fromJson(json, ClassArchetype.class);
                classArc.pk = pk;
                ClassArchetype.trackObject(classArc);
                classList.add(classArc.name);
            } while(classCursor.moveToNext());
        }

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
        Objects.requireNonNull(dialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        EditText nameInput = dialog.findViewById(R.id.createcharacter_edittext_name);
        EditText raceInput = dialog.findViewById(R.id.createcharacter_edittext_race);
        AutoCompleteTextView classInput = dialog.findViewById(R.id.createcharacter_edittext_class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, classList);
        classInput.setAdapter(adapter);

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

                newCharacter.pk = db.addLine("CHARACTERS", newCharacter.toJson());
                classArc.pk = db.addLine("CLASSES", classArc.toJson());
                dialog.dismiss();
            } else {
                ClassArchetype classArc = new ClassArchetype("Bard");

                Character newCharacter = new Character("DEBUG", "Human", classArc);
                newCharacter.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSaHmgseRqO6CI14XWSh5swCN19tzNhtgptvg&usqp=CAU");
                characterList.add(newCharacter);
                recyclerAdapter.notifyDataSetChanged();
                dialog.dismiss();
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