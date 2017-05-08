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

import java.util.List;


public class MainActivity extends AppCompatActivity {

    GridView deviceGrid;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    static Context mDialogContext = null;
    DBConnection myDbConnection;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myDbConnection = new DBConnection(this);
        //btnGraph = (Button) findViewById(R.id.login);
        setSupportActionBar(toolbar);
        mDialogContext = this;
        retrieveDevices();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View myView = getLayoutInflater().inflate(R.layout.dialog_add_device, null);
                final EditText mName = (EditText) myView.findViewById(R.id.EditTextName);
                final EditText mDescription = (EditText) myView.findViewById(R.id.EditDecription);
                spinner = (Spinner) myView.findViewById(R.id.TypeSpinner);
                adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.devicetypelist, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                Button submitDevice = (Button) myView.findViewById(R.id.submitButton);
                mBuilder.setView(myView);
                dialog = mBuilder.create();
                dialog.show();

                submitDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mName.getText().toString().isEmpty() && !mDescription.getText().toString().isEmpty()){
                            boolean insert = myDbConnection.insertData(mName.getText().toString(), spinner.getSelectedItem().toString(), mDescription.getText().toString());
                            if (insert){
                                Toast.makeText(MainActivity.this, "Device Added", Toast.LENGTH_SHORT).show();
                                retrieveDevices();
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
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


    private void retrieveDevices(){
        Cursor myCursor = myDbConnection.getAllData();
        String [] fields = new String [] {myDbConnection.D_COL_2, myDbConnection.D_COL_3, myDbConnection.D_COL_4};
        int [] view = new int [] {R.id.name, R.id.type, R.id.desc};
        SimpleCursorAdapter myCursorAdaptor;
        myCursorAdaptor = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, myCursor, fields, view, 0);
        ListView myList = (ListView) findViewById(R.id.deviceGrid);
        myList.setAdapter(myCursorAdaptor);
    }
    /**
     * Starts GameActivity
     * @param view enables the enables the method to used as onClick in the XML
     */
    public void graphView (View view){ // used to begin the game by creating an intent and starting an activity based on that activity
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent1 = new Intent(this,GraphActivity.class);
            this.startActivity(intent1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


