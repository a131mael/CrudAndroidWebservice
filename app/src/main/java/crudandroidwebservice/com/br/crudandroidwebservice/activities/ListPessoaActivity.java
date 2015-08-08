package crudandroidwebservice.com.br.crudandroidwebservice.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import crudandroidwebservice.com.br.crudandroidwebservice.R;
import crudandroidwebservice.com.br.crudandroidwebservice.adapter.PessoaListViewAdapter;
import crudandroidwebservice.com.br.crudandroidwebservice.pojo.Pessoa;

public class ListPessoaActivity extends ActionBarActivity {

    private static final String URL = "http://192.168.25.4:8080/JavaWebservice/pessoaResource/getAll";

    private ListView listViewPessoa;

    private void init() {
        listViewPessoa = (ListView) findViewById(R.id.listViewPessoa);
        listViewPessoa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pessoa p = (Pessoa) listViewPessoa.getItemAtPosition(position);

                Intent intent = new Intent(ListPessoaActivity.this, PrincipalActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("nome", p.getNome());
                startActivity(intent);
                finish();
            }
        });
        new HttpAsyncTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pessoa);
        init();
    }

    private String getAll() {
        String resposta = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet get = new HttpGet(URL);
        try {
            get.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(get);
            resposta = inputStreamToString(response.getEntity().getContent()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
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


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return getAll();
        }

        @Override
        protected void onPostExecute(String result) {
            List<Pessoa> listPessoa = new ArrayList<Pessoa>();
            JSONArray jsonArray = null;

            try {
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    Pessoa pessoa = new Pessoa();
                    pessoa.setId(jsonObject.getInt("id"));
                    pessoa.setNome(jsonObject.getString("nome"));
                    listPessoa.add(pessoa);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PessoaListViewAdapter adapter = new PessoaListViewAdapter(getBaseContext(), listPessoa);
            listViewPessoa.setAdapter(adapter);
        }

    }
}
