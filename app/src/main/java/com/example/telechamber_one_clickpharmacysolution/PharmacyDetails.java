package com.example.telechamber_one_clickpharmacysolution;

import java.util.ArrayList;
import java.util.List;

import com.example.telechamber_one_clickpharmacysolution.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PharmacyDetails extends ListActivity {

    protected TextView drugNameText;
    protected TextView titleText;
    protected List<PharmacyAction> actions;
    protected PharmacyActionAdapter adapter;
    protected int drugId;
    protected int managerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_details);

        drugId = getIntent().getIntExtra("DRUG_ID", 0);
        Log.d(PharmacyDetails.class.getName(), "<<<<<<<<<drug id>>>>>>>: " + drugId);
        SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT drug._id, drug.drugName, drug.pharmaName, drug.price, drug.address, drug.symtoms, drug.city, drug.pincode, drug.officePhone, drug.cellPhone, drug.email FROM lifelineDrug drug WHERE drug._id = ?",
                new String[]{""+drugId});
        Log.d(PharmacyList.class.getName(), "<<<<<<<<<cursor222>>>>>>>: " + cursor.getCount());
        if (cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            Log.d(PharmacyDetails.class.getName(), "<<<<<<<<<inside>>>>>>>: ");
            drugNameText = (TextView) findViewById(R.id.drugName);
            drugNameText.setText(cursor.getString(cursor.getColumnIndex("drugName")));
            Log.d(PharmacyDetails.class.getName(), "<<<<<<<<<drug name text>>>>>>>: " + drugNameText.getText());
            titleText = (TextView) findViewById(R.id.price);
            titleText.setText(cursor.getString(cursor.getColumnIndex("price")));

            actions = new ArrayList<PharmacyAction>();

            String symtoms = cursor.getString(cursor.getColumnIndex("symtoms"));
            if (symtoms != null) {
                actions.add(new PharmacyAction("Disease", symtoms));
            }
            String pharmaName = cursor.getString(cursor.getColumnIndex("pharmaName"));
            if (pharmaName != null) {
                actions.add(new PharmacyAction("Pharmacy Name", pharmaName));
            }

            String address = cursor.getString(cursor.getColumnIndex("address"));
            if (address != null) {
                actions.add(new PharmacyAction("Address", address));
            }

            String cellPhone = cursor.getString(cursor.getColumnIndex("cellPhone"));
            if (cellPhone != null) {
                actions.add(new PharmacyAction("Call mobile", cellPhone, PharmacyAction.ACTION_CALL));
                actions.add(new PharmacyAction("SMS", cellPhone, PharmacyAction.ACTION_SMS));
            }

            String email = cursor.getString(cursor.getColumnIndex("email"));
            if (email != null) {
                actions.add(new PharmacyAction("Email", email, PharmacyAction.ACTION_EMAIL));
            }

	        /*managerId = cursor.getInt(cursor.getColumnIndex("managerId"));
	        if (managerId>0) {
	        	actions.add(new PharmacyAction("View manager", cursor.getString(cursor.getColumnIndex("managerFirstName")) + " " + cursor.getString(cursor.getColumnIndex("managerLastName")), PharmacyAction.ACTION_VIEW));
	        }

	        cursor = db.rawQuery("SELECT count(*) FROM employee WHERE managerId = ?",
					new String[]{""+drugId});
	        cursor.moveToFirst();
	        int count = cursor.getInt(0);
	        if (count>0) {
	        	actions.add(new PharmacyAction("View direct reports", "(" + count + ")", PharmacyAction.ACTION_REPORTS));
	        }*/

            adapter = new PharmacyActionAdapter();
            setListAdapter(adapter);
        }

    }

    public void onListItemClick(ListView parent, View view, int position, long id) {

        PharmacyAction action = actions.get(position);

        Intent intent;
        switch (action.getType()) {

            case PharmacyAction.ACTION_CALL:
                Uri callUri = Uri.parse("tel:" + action.getData());
                intent = new Intent(Intent.ACTION_CALL, callUri);
                startActivity(intent);
                break;

            case PharmacyAction.ACTION_EMAIL:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{action.getData()});
                startActivity(intent);
                break;

            case PharmacyAction.ACTION_SMS:
                Uri smsUri = Uri.parse("sms:" + action.getData());
                intent = new Intent(Intent.ACTION_VIEW, smsUri);
                startActivity(intent);
                break;

            case PharmacyAction.ACTION_REPORTS:
                intent = new Intent(this, DirectReports.class);
                intent.putExtra("DRUG_ID", drugId);
                startActivity(intent);
                break;

            case PharmacyAction.ACTION_VIEW:
                intent = new Intent(this, PharmacyDetails.class);
                intent.putExtra("DRUG_ID", drugId);
                startActivity(intent);
                break;
        }
    }

    class PharmacyActionAdapter extends ArrayAdapter<PharmacyAction> {

        PharmacyActionAdapter() {
            super(PharmacyDetails.this, R.layout.action_list_item, actions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PharmacyAction action = actions.get(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.action_list_item, parent, false);
            TextView label1 = (TextView) view.findViewById(R.id.label1);
            label1.setText(action.getLabel());
            TextView data = (TextView) view.findViewById(R.id.data);
            data.setText(action.getData());
            return view;
        }

    }

}
