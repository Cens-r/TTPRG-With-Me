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
    FileReaderWriterHelper frw;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(this);
        frw = new FileReaderWriterHelper(this);

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

                String uuid = character.class_uuid;
                Log.d("Character", "Class UUID: " + uuid);
                String classJson = db.retrieveClass(uuid);
                if (classJson != null && !ClassArchetype.CheckClassExists(uuid)) {
                    ClassArchetype classArc = ClassArchetype.fromJson(classJson, ClassArchetype.class);
                    classArc.uuid(uuid);
                    ClassArchetype.trackObject(classArc);
                    character.classArc = classArc;
                } else {
                    int id = ClassArchetype.GetMapValue(uuid);
                    ClassArchetype classArc = (ClassArchetype) ClassArchetype.getObject(ClassArchetype.class, id);
                    character.classArc = classArc;
                }

                character.Backpack.forEach((key, value) -> {
                    Cursor itemCursor = db.getItems(pk, key);
                    if (itemCursor.moveToFirst()) {
                        do {
                            int itemJsonIndex = itemCursor.getColumnIndex("JSON");
                            String itemJson = itemCursor.getString(itemJsonIndex);
                            int ItemPKIndex = itemCursor.getColumnIndex("pk");
                            long itemPK = itemCursor.getLong(ItemPKIndex);

                            Item item = ClassManager.fromJson(itemJson, Item.class);
                            item.pk = itemPK;
                            character.Backpack.get(key).add(item);
                        } while(itemCursor.moveToNext());
                    }
                    itemCursor.close();
                });

                Cursor noteCursor = db.getItems(pk, "Notes");
                if (noteCursor.moveToFirst()) {
                    do {
                        int itemJsonIndex = noteCursor.getColumnIndex("JSON");
                        String itemJson = noteCursor.getString(itemJsonIndex);
                        int ItemPKIndex = noteCursor.getColumnIndex("pk");
                        long itemPK = noteCursor.getLong(ItemPKIndex);

                        Item item = ClassManager.fromJson(itemJson, Item.class);
                        item.pk = itemPK;
                        character.Notes.add(item);
                    } while(noteCursor.moveToNext());
                }
                noteCursor.close();

                Cursor abilityCursor = db.getItems(pk, "Abilities");
                if (abilityCursor.moveToFirst()) {
                    do {
                        int itemJsonIndex = abilityCursor.getColumnIndex("JSON");
                        String itemJson = abilityCursor.getString(itemJsonIndex);
                        int ItemPKIndex = abilityCursor.getColumnIndex("pk");
                        long itemPK = abilityCursor.getLong(ItemPKIndex);

                        Item item = ClassManager.fromJson(itemJson, Item.class);
                        item.pk = itemPK;
                        character.Abilities.add(item);
                    } while(abilityCursor.moveToNext());
                }
                abilityCursor.close();
            } while(characterCursor.moveToNext());
        }
        characterCursor.close();

        Cursor classCursor = db.fetchAll("CLASSES");
        if (classCursor.moveToFirst()) {
            do {
                int uuidIndex = classCursor.getColumnIndex("UUID");
                String uuid = classCursor.getString(uuidIndex);
                if (ClassArchetype.CheckClassExists(uuid)) { continue; }

                int jsonIndex = classCursor.getColumnIndex("JSON");
                String json = classCursor.getString(jsonIndex);

                ClassArchetype classArc = ClassManager.fromJson(json, ClassArchetype.class);
                int id = ClassArchetype.trackObject(classArc);
                ClassArchetype.AddMapValue(uuid, id);
                classList.add(classArc.name);
            } while(classCursor.moveToNext());
        }
        classCursor.close();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new CharacterAdapter(characterList, this);
        recyclerView.setAdapter(recyclerAdapter);

        ImageButton import_button = findViewById(R.id.charselect_button_import);
        Button create_button = findViewById(R.id.charselect_button_create);

        import_button.setOnClickListener(v -> {
            frw.GetFile();
        });
        create_button.setOnClickListener(view -> {
            showDialog();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int exportCode = FileReaderWriterHelper.EXPORT_CHARACTER;
        int importCode = FileReaderWriterHelper.IMPORT_CHARACTER;

        if (requestCode >= exportCode && requestCode < exportCode + 1000) {
            frw.exporter(requestCode, resultCode, data);
        } else if (requestCode == importCode) {
            Character character = frw.importer(resultCode, data);
            characterList.add(character);
            classList.add(character.classArc.name);
            recyclerAdapter.notifyDataSetChanged();
        }
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
                // SHOULD HAVE ADDED THIS BEFORE UPDATING CLASS STUFF
                newCharacter.classArc = classArc;
                newCharacter.class_uuid = classArc.uuid;

                characterList.add(newCharacter);
                recyclerAdapter.notifyDataSetChanged();

                newCharacter.pk = db.addLine("CHARACTERS", newCharacter.toJson());
                db.addClass(classArc.uuid, classArc.toJson());
                dialog.dismiss();
            } else {
                dialog.dismiss();
                Toast.makeText(this, "One of the three required inputs is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        AppCompatButton closeButton = dialog.findViewById(R.id.createcharacter_button_close);
        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}