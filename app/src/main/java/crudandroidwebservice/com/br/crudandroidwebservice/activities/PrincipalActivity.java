package crudandroidwebservice.com.br.crudandroidwebservice.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import crudandroidwebservice.com.br.crudandroidwebservice.R;

public class PrincipalActivity extends ActionBarActivity implements OnClickListener {
    private static final String URL = "http://192.168.25.4:8080/JavaWebservice/pessoaResource/";

    private String idPessoa;

    private TextView textViewId;
    private EditText editTextId;
    private EditText editTextNome;
    private Button buttonSalvar;
    private Button buttonListagem;
    private Button buttonExcluir;

    private void init() {
        textViewId = (TextView) findViewById(R.id.textViewId);
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNome = (EditText) findViewById(R.id.editTextNome);
        buttonSalvar = (Button) findViewById(R.id.buttonSalvar);
        buttonListagem = (Button) findViewById(R.id.buttonListagem);
        buttonExcluir = (Button) findViewById(R.id.buttonExcluir);
        buttonSalvar.setOnClickListener(this);
        buttonListagem.setOnClickListener(this);
        buttonExcluir.setOnClickListener(this);

        idPessoa = this.getIntent().getStringExtra("id");

        if (idPessoa != null && !idPessoa.isEmpty()) {
            editTextId.setText(idPessoa);
            editTextNome.setText(this.getIntent().getStringExtra("nome"));
        } else {
            textViewId.setVisibility(View.GONE);
            editTextId.setVisibility(View.GONE);
            buttonExcluir.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSalvar:
                if (idPessoa == null || idPessoa.isEmpty()) {
                    new HttpAsyncPOST().execute();
                } else {
                    new HttpAsyncPUT().execute();
                }
                break;
            case R.id.buttonListagem:
                startActivity(new Intent(PrincipalActivity.this, ListPessoaActivity.class));
                break;
            case R.id.buttonExcluir:
                new HttpAsyncDELETE().execute();
                break;
        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String linha = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((linha = rd.readLine()) != null) {
                total.append(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

    private class HttpAsyncPOST extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return post();
        }

        @Override
        protected void onPostExecute(String result) {
            editTextNome.setText("");
            imprimirMensagem(result);
        }

    }

    private String post() {
        String mensagem = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        try {
            JSONObject json = new JSONObject();
            json.put("nome", editTextNome.getText().toString());

            post.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(post);
            mensagem = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mensagem;
    }

    private class HttpAsyncPUT extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return put();
        }

        @Override
        protected void onPostExecute(String result) {
            buttonExcluir.setVisibility(View.GONE);
            textViewId.setVisibility(View.GONE);
            editTextId.setVisibility(View.GONE);
            editTextNome.setText("");

            idPessoa = "";
            imprimirMensagem(result);
        }

    }

    private String put() {
        String mensagem = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPut put = new HttpPut(URL + idPessoa);
        try {
            JSONObject json = new JSONObject();
            json.put("nome", editTextNome.getText().toString());

            put.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            put.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(put);
            mensagem = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mensagem;
    }

    private class HttpAsyncDELETE extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return delete();
        }

        @Override
        protected void onPostExecute(String result) {
            buttonExcluir.setVisibility(View.GONE);
            textViewId.setVisibility(View.GONE);
            editTextId.setVisibility(View.GONE);
            editTextNome.setText("");

            idPessoa = "";

            imprimirMensagem(result);
        }
    }

    private String delete() {
        String mensagem = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpDelete delete = new HttpDelete(URL + idPessoa);
        try {
            delete.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(delete);
            mensagem = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mensagem;
    }
}