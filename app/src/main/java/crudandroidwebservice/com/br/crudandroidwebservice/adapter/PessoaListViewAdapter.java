package crudandroidwebservice.com.br.crudandroidwebservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import crudandroidwebservice.com.br.crudandroidwebservice.R;
import crudandroidwebservice.com.br.crudandroidwebservice.pojo.Pessoa;

/**
 * Created by Carlos on 08/08/2015.
 */
public class PessoaListViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Pessoa> listPessoa;

    public PessoaListViewAdapter(Context context, List<Pessoa> listPessoa) {
        this.listPessoa = listPessoa;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listPessoa.size();
    }

    @Override
    public Object getItem(int position) {
        return listPessoa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listPessoa.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pessoa, null);
        }
        TextView textViewIdPessoa = (TextView) convertView.findViewById(R.id.textViewIdPessoa);
        TextView textViewNomePessoa = (TextView) convertView.findViewById(R.id.textViewNomePessoa);

        Pessoa p = listPessoa.get(position);

        textViewIdPessoa.setText(p.getId().toString());
        textViewNomePessoa.setText(p.getNome());
        return convertView;
    }
}
