package com.raianwz.locadoratrash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáveis globais
    public static final String NOME_BANCO_DE_DADOS = "filme.db";

    EditText txtFilme, txtGenero, txtAno;
    Button btnAdicionar, btnVisualizar;

    SQLiteDatabase dbFilmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Anunciand ao java componentes do XML
        txtFilme = findViewById(R.id.txtFilme);
        txtGenero = findViewById(R.id.txtGenero);
        txtAno = findViewById(R.id.txtAno);

        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(this);

        btnVisualizar = findViewById(R.id.btnVisualizar);
        btnVisualizar.setOnClickListener(this);

        //Criando banco de dados;
        dbFilmes = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        //Criando a tabela no Banco de dados especifidado
        criarTabelaFilme();
    }

    //Este método criará a tabela, quando abrirmos o App, mas só criará a tabela caso não esteja criada ainda
    private void criarTabelaFilme() {
        String sql = "CREATE TABLE IF NOT EXISTS filmes (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "filme varchar(255) NOT NULL," +
                "genero varchar(255) NOT NULL," +
                "ano varchar(100) NOT NULL);";

        dbFilmes.execSQL(sql);
    }

    //Este método validará os campos filme, genero e ano
    private boolean verificarEntrada(String filme, String genero, String ano) {

        if (filme.isEmpty()) {
            txtFilme.setError("Por favor, digite o nome do filme");
            txtFilme.requestFocus();
            return false;
        }
        if (genero.isEmpty()) {
            txtGenero.setError("Por favor, digite o gênero do filme");
            txtGenero.requestFocus();
            return false;
        }
        if (ano.isEmpty()) {
            txtAno.setError("Por favor, digite o ano de lançamento do filme");
            txtAno.requestFocus();
            return false;
        }

        return true;
    }

    //Metédo que irá fazer operaçaõ de Inserir os filmes
    private void adicionarFilme() {
        String nomeFilme = txtFilme.getText().toString().trim();
        String generoFilme = txtGenero.getText().toString().trim();
        String anoFilme = txtAno.getText().toString().trim();

        //Validando entrada
        if (verificarEntrada(nomeFilme, generoFilme, anoFilme)) {
            String insertSQL = "INSERT INTO filmes(" +
                    "filme, " +
                    "genero, " +
                    "ano)" +
                    "VALUES(?, ?, ?);";
            dbFilmes.execSQL(insertSQL, new String[]{nomeFilme, generoFilme, anoFilme});

            Toast.makeText(getApplicationContext(), "Filme adicionado com sucesso!!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdicionar:
                adicionarFilme();
                break;
            case R.id.btnVisualizar:
                startActivity(new Intent(getApplicationContext(), FilmesActivity.class));
                break;
        }
    }
}