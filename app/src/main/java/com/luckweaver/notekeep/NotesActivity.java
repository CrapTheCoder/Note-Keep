package com.luckweaver.notekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.*;
import android.text.*;
import android.util.Log;
import android.widget.*;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    TextView notesView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        final int ID = getIntent().getIntExtra("ID", -1);

        notesView = findViewById(R.id.notesView);
        notesView.setText(MainActivity.notesList.get(ID));

        notesView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notesList.set(ID, charSequence.toString());
                MainActivity.setTitles();
                MainActivity.arrayAdapter.notifyDataSetChanged();
                preferences.edit().putString("notesList", ObjectSerializer.serialize(MainActivity.notesList)).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}