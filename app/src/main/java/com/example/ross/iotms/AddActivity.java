package com.example.ross.iotms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    Spinner spinner;
    TextView addDevice;
    EditText name, description;
    ArrayAdapter<CharSequence> adapter;
    DBConnection myDbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        myDbConnection = new DBConnection(this);
        name = (EditText) findViewById(R.id.EditTextName);
        description = (EditText) findViewById(R.id.EditDecription);
        spinner = (Spinner) findViewById(R.id.TypeSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.devicetypelist, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+" selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * adds device to database
     * @param view enables the method to used as onClick in the XML
     */
    public void addDevice (View view){
        String enterName = name.getText().toString();
        String enterDescription = description.getText().toString();
        String enterType = spinner.getSelectedItem().toString();
        boolean inserted = myDbConnection.insertData(enterName, enterType, enterDescription);

        if (inserted = true) {
            Toast.makeText(AddActivity.this, "Added to High Scores", Toast.LENGTH_LONG).show(); // shows message that score has been added to db
        } else {
            Toast.makeText(AddActivity.this, "Adding to scores failed", Toast.LENGTH_LONG).show();
        }
    }
}
