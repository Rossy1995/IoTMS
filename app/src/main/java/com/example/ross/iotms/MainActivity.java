package com.example.ross.iotms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    GridView deviceGrid;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    static Context mDialogContext = null;
    DBConnection myDbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDialogContext = this;
        myDbConnection = new DBConnection(this);

        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View myView = getLayoutInflater().inflate(R.layout.dialog_add_device, null);
                final EditText mName = (EditText) myView.findViewById(R.id.EditTextName);
                final EditText mDescription = (EditText) myView.findViewById(R.id.EditDecription);
                spinner = (Spinner) myView.findViewById(R.id.TypeSpinner);
                adapter = ArrayAdapter.createFromResource(mDialogContext, R.array.devicetypelist, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                Button submit = (Button) myView.findViewById(R.id.submitButton);
                mBuilder.setView(myView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mName.getText().toString().isEmpty() && !mDescription.getText().toString().isEmpty()){
                            boolean inserted = myDbConnection.insertData(mName.getText().toString(), spinner.getSelectedItem().toString(), mDescription.getText().toString());
                            if (inserted = true) {
                                Toast.makeText(MainActivity.this, "Device Added", Toast.LENGTH_SHORT).show();
                                populateListView();
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please fill out entire form", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListView()
    {
        Cursor cursor = myDbConnection.getAllData();
        String[] fromFieldNames = new String[] {DBConnection.D_COL_2, DBConnection.D_COL_4};
        int[] toViewIDs = new int[] {R.id.name, R.id.desc};

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);

        ListView myList = (ListView) findViewById(R.id.deviceGrid);
        myList.setAdapter(myCursorAdapter);

    }
}
