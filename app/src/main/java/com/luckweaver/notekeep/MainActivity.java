package com.luckweaver.notekeep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.addNotes)
            addNewNote();

        return item.getItemId() == R.id.addNotes;
    }

    public void openNotes(int i) {
        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra("ID", i);
        startActivity(intent);
    }

    public void addNewNote() {
        notesList.add("");
        setTitles();
        arrayAdapter.notifyDataSetChanged();
        openNotes(notesList.size() - 1);
    }

    public static void setTitles() {
        titles.clear();
        for (int c = 0; c < notesList.size(); c++) {
            String s = notesList.get(c);
            StringBuilder titleString = new StringBuilder();

            for (int i = 0; i < Math.min(100, s.length()); i++)
                titleString.append(s.charAt(i));

            if (s.length() > 100)
                titleString.append("...");

            titles.add(titleString.toString());
        }
    }

    ListView allNotes;
    static ArrayList<String> titles = new ArrayList<>();
    static ArrayList<String> notesList = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        if (preferences.getString("notesList", null) == null) {
            notesList.add("Example note");
            setTitles();
            preferences.edit().putString("notesList", ObjectSerializer.serialize(notesList)).apply();
        }

        allNotes = findViewById(R.id.allNotes);
        notesList = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("notesList", ""));

        setTitles();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        allNotes.setAdapter(arrayAdapter);

        allNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openNotes(i);
            }
        });

        allNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notesList.remove(position);
                                setTitles();
                                arrayAdapter.notifyDataSetChanged();
                                preferences.edit().putString("notesList", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                return true;
            }
        });
    }
}