package com.example.telechamber_one_clickpharmacysolution;

import com.example.telechamber_one_clickpharmacysolution.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DirectReports extends ListActivity {

    protected Cursor cursor=null;
    protected ListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_reports);

        SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();

        int drugId = getIntent().getIntExtra("DRUG_ID", 0);

        Cursor cursor = db.rawQuery("SELECT _id, drugName, pharmaName, price FROM lifelineDrug WHERE _id = ?",
                new String[]{""+drugId});

        if (cursor.getCount() != 1)
        {
            return;
        }

        cursor.moveToFirst();

        TextView employeeNameText = (TextView) findViewById(R.id.drugName);
        employeeNameText.setText(cursor.getString(cursor.getColumnIndex("drugName")));

        TextView titleText = (TextView) findViewById(R.id.price);
        titleText.setText(cursor.getString(cursor.getColumnIndex("price")));

        cursor = db.rawQuery("SELECT _id, drugName, pharmaName, price, officePhone, cellPhone, email FROM lifelineDrug WHERE _Id = ?",
                new String[]{""+drugId});
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.drug_list_item,
                cursor,
                new String[] {"drugName", "price"},
                new int[] {R.id.drugName, R.id.price});
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView parent, View view, int position, long id) {
        Intent intent = new Intent(this, PharmacyDetails.class);
        Cursor cursor = (Cursor) adapter.getItem(position);
        intent.putExtra("DRUG_ID", cursor.getInt(cursor.getColumnIndex("_id")));
        startActivity(intent);
    }

}
