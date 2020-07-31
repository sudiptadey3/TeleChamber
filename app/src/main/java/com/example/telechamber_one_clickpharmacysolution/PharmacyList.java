package com.example.telechamber_one_clickpharmacysolution;

import com.example.telechamber_one_clickpharmacysolution.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class PharmacyList extends ListActivity {

    protected EditText searchText;
    protected SQLiteDatabase db;
    protected Cursor cursor;
    protected ListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        searchText = (EditText) findViewById(R.id.searchText);
        db = (new DatabaseHelper(this)).getWritableDatabase();
    }

    public void onListItemClick(ListView parent, View view, int position, long id) {
        Intent intent = new Intent(this, PharmacyDetails.class);
        Cursor cursor = (Cursor) adapter.getItem(position);
        intent.putExtra("DRUG_ID", cursor.getInt(cursor.getColumnIndex("_id")));
        startActivity(intent);
    }

    public void search(View view) {
        // || is the concatenation operation in SQLite
        Log.d(PharmacyList.class.getName(), "<<<<<<<<<search string>>>>>>>: " + searchText.getText().toString());
        cursor = db.rawQuery("SELECT _id, drugName, pharmaName, price FROM lifelineDrug where drugName like ?",new String[]{"%"+searchText.getText().toString()+"%"});
        Log.d(PharmacyList.class.getName(), "<<<<<<<<<cursor>>>>>>>: " + cursor.getCount());
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.drug_list_item,
                cursor,
                new String[] {"drugName", "price"},
                new int[] {R.id.drugName, R.id.price});
        setListAdapter(adapter);
    }

}
