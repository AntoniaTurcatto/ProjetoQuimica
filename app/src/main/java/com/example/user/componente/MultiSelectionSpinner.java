package com.example.user.componente;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.ItemConteudo;

import java.util.ArrayList;
public class MultiSelectionSpinner extends AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {
    ArrayList<ItemConteudo> items = null;
    ArrayAdapter adapter;

    // utiliza esse construtor
    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
        Log.d("Teste","Construtor");
    }

    @Override
    public void onClick(DialogInterface dialog, int idx, boolean isChecked) {
        if (items != null && idx < items.size()) {
            items.get(idx).setValue(isChecked);
            adapter.clear();
            adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "'idx' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Log.d( "Teste", "Perform");
        String[] itemNames = new String[items.size()];
        boolean[] itemSelection = new boolean[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getConteudo().getNomeConteudo();
            itemSelection[i] = items.get(i).isValue();
        }
        builder.setMultiChoiceItems(itemNames, itemSelection, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                // Do nothing
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }


    public void setItems(ArrayList<Conteudo> listaConteudos) {
        this.items = new ArrayList<ItemConteudo>();
        // gerando a lista de ItemConteudo a partir de Conteudo
        for (int x = 0; x < listaConteudos.size(); x++) {
            ItemConteudo itemConteudo = new ItemConteudo(listaConteudos.get(x));
            this.items.add(itemConteudo);
            Log.d("Teste","Gerando os ItemConteudo para - : " + items.get(x).getConteudo().getNomeConteudo());
        }
        adapter.clear();
        adapter.add("");
    }


    public ArrayList<Conteudo> getSelectedItems() {
        ArrayList<Conteudo> conteudosSelecionados = new ArrayList<>();
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).isValue() == true) { // ou if (items.get(i).isValue()) {..}
                conteudosSelecionados.add(items.get(i).getConteudo());
            }
        }

        return conteudosSelecionados;
    }


    // Return number of items selected
    public int getSelectedSize() {
        int size = 0;
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).isValue() == true) {
                size++;
            }
        }

        return size;
    }


    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).isValue() == true) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(items.get(i).getConteudo().getNomeConteudo());
            }
        }
        return sb.toString();
    }

}
