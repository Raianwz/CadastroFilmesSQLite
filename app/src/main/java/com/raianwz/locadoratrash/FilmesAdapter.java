package com.raianwz.locadoratrash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FilmesAdapter extends ArrayAdapter<Filmes> {
    //Variaveis Globais do Adaptador
    Context myContext;
    int filmesLayout;
    List<Filmes> filmesList;
    SQLiteDatabase dbFilmes;

    //Construtor do Adaptador
    public FilmesAdapter(@NonNull Context myContext, int filmesLayout, List<Filmes> filmesList, SQLiteDatabase dbFilmes) {
        super(myContext, filmesLayout, filmesList);

        this.myContext = myContext;
        this.filmesLayout = filmesLayout;
        this.filmesList = filmesList;
        this.dbFilmes = dbFilmes;
    }

    //Inflar layout com o modelo e suas ações
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(filmesLayout, null);

        final Filmes filmes = filmesList.get(position);

        TextView txtViewNomeFilme = view.findViewById(R.id.nome_filme);
        TextView txtViewGeneroFilme = view.findViewById(R.id.nome_genero);
        TextView txtViewAnoFilme = view.findViewById(R.id.nome_ano);

        txtViewNomeFilme.setText(filmes.getFilme());
        txtViewGeneroFilme.setText(filmes.getGenero());
        txtViewAnoFilme.setText(filmes.getAno());

        Button btnEditar = view.findViewById(R.id.btnEditar);
        Button btnExcluir = view.findViewById(R.id.btnExcluir);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarFilme(filmes);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Deseja Excluir?");
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM filmes WHERE id = ?";
                        dbFilmes.execSQL(sql, new Integer[]{filmes.getId()});
                        //chamando método para atualizar a lista de filmes
                        regarregarFilmesDB();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Não faz nada :)
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public void alterarFilme(final Filmes filmes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);

        LayoutInflater inflater = LayoutInflater.from(myContext);

        View view = inflater.inflate(R.layout.filmes_alterar, null);
        builder.setView(view);

        final EditText txtEditarFilme = view.findViewById(R.id.txtEditarFilme);
        final EditText txtEditarGenero = view.findViewById(R.id.txtEditarGenero);
        final EditText txtEditarAno = view.findViewById(R.id.txtEditarAno);

        txtEditarFilme.setText(filmes.getFilme());
        txtEditarGenero.setText(filmes.getGenero());
        txtEditarAno.setText(filmes.getAno());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnAlterar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filme = txtEditarFilme.getText().toString().trim();
                String genero = txtEditarGenero.getText().toString().trim();
                String ano = txtEditarAno.getText().toString().trim();

                if (filme.isEmpty()) {
                    txtEditarFilme.setError("Filme está em branco!");
                    txtEditarFilme.requestFocus();
                    return;
                }

                if (genero.isEmpty()) {
                    txtEditarGenero.setError("Gênero do filme está em branco!");
                    txtEditarGenero.requestFocus();
                    return;
                }

                if (ano.isEmpty()) {
                    txtEditarAno.setError("Ano de lançamento está em branco!");
                    txtEditarAno.requestFocus();
                    return;
                }

                String sql = "UPDATE filmes SET filme = ?, genero = ?, ano = ? WHERE id = ?";
                dbFilmes.execSQL(sql, new String[]{filme, genero, ano, String.valueOf(filmes.getId())});
                Toast.makeText(myContext, "Filme alterado com sucesso!!", Toast.LENGTH_LONG).show();

                //Chamando método para atualizar a lista de filmes
                regarregarFilmesDB();

                //limpa a estrutura do AlertDialog
                dialog.dismiss();
            }
        });
    }
    public void regarregarFilmesDB(){
        Cursor cursorFilmes = dbFilmes.rawQuery("SELECT * FROM filmes", null);
        if(cursorFilmes.moveToFirst()){
            filmesList.clear();
            do{
                filmesList.add(new Filmes(
                        cursorFilmes.getInt(0),
                        cursorFilmes.getString(1),
                        cursorFilmes.getString(2),
                        cursorFilmes.getString(3)
                        ));
            } while(cursorFilmes.moveToNext());
        }
        cursorFilmes.close();
        notifyDataSetChanged();
    }
}
