package cnam.smb116.tp5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cnam.smb116.tp5.DBHelper.EnseignantsHelper;
import cnam.smb116.tp5.Model.Enseignant;

public class ShowEnseignantsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_enseignants);
        listView = (ListView) findViewById(R.id.listView);
        populateListView();
    }

    // Récupère la liste des enseignants et les affiche dans la ListView de l'IHM via un adapter
    public void populateListView() {
        EnseignantsHelper helper = new EnseignantsHelper(this);
        ArrayList<Enseignant> enseignants = helper.getEnseignants();
        ListAdapter adapter = new ArrayAdapter<Enseignant>(this, android.R.layout.simple_list_item_1, enseignants);
        listView.setAdapter(adapter);
    }

    public void doBack(View view) {
        finish();
    }
}