package com.raianwz.locadoratrash;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FilmesActivity extends AppCompatActivity {

    List<Filmes> filmesList;
    FilmesAdapter filmesAdapter;
    SQLiteDatabase dbFilmes;
    ListView listViewFilmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filmes_layout);

        listViewFilmes = findViewById(R.id.listarFilmesView);
        filmesList = new ArrayList<>();

        dbFilmes = openOrCreateDatabase(MainActivity.NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        visualizarFilmesDatabase();
    }

    //Listará todos os filmes do banco de dados
    private void visualizarFilmesDatabase() {
        Cursor cursorFilmes = dbFilmes.rawQuery("SELECT * FROM filmes", null);
        if (cursorFilmes.moveToFirst()) {
            do {
                filmesList.add(new Filmes(
                        cursorFilmes.getInt(0),
                        cursorFilmes.getString(1),
                        cursorFilmes.getString(2),
                        cursorFilmes.getString(3)
                ));
            } while (cursorFilmes.moveToNext());
        }
        cursorFilmes.close();

        //Verificar o layout
        filmesAdapter = new FilmesAdapter(this, R.layout.filmes_model, filmesList, dbFilmes);

        listViewFilmes.setAdapter(filmesAdapter);
    }
}