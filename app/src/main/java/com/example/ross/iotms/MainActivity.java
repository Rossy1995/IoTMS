package com.example.ross.iotms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Spinner dialogSpinner;
    ArrayAdapter<CharSequence> adapter;
    DBConnection myDbConnection;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myDbConnection = new DBConnection(this);
        setSupportActionBar(toolbar);
        retrieveDevices();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                View myView = getLayoutInflater().inflate(R.layout.dialog_add_device, null);
                final EditText dialogName = (EditText) myView.findViewById(R.id.EditTextName);
                final EditText dialogDescription = (EditText) myView.findViewById(R.id.EditDecription);
                dialogSpinner = (Spinner) myView.findViewById(R.id.TypeSpinner);
                adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.devicetypelist, R.layout.spinner_layout);
                adapter.setDropDownViewResource(R.layout.spinner_layout);
                dialogSpinner.setAdapter(adapter);
                Button submitDevice = (Button) myView.findViewById(R.id.submitButton);
                myBuilder.setView(myView);
                dialog = myBuilder.create();
                dialog.show();

                submitDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!dialogName.getText().toString().isEmpty() && !dialogDescription.getText().toString().isEmpty()){
                            boolean insert = myDbConnection.insertData(dialogName.getText().toString(), dialogSpinner.getSelectedItem().toString(), dialogDescription.getText().toString());
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

    @Override
    public void onBackPressed()
    {}


    private void retrieveDevices(){
        final Cursor myCursor = myDbConnection.getAllData();
        String [] fields = new String [] {myDbConnection.D_COL_2, myDbConnection.D_COL_3, myDbConnection.D_COL_4};
        int [] view = new int [] {R.id.name, R.id.type, R.id.desc};
        SimpleCursorAdapter myCursorAdaptor;
        myCursorAdaptor = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, myCursor, fields, view, 0);
        final ListView myList = (ListView) findViewById(R.id.deviceGrid);
        myList.setAdapter(myCursorAdaptor);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent graphIntent = new Intent(getApplicationContext(), GraphActivity.class);
                startActivity(graphIntent);
            }
        });
    }

     /*lv.setOnItemClickListener(new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

            c.moveToPosition(pos);
            int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
            Uri outURI = Uri.parse(data.toString() + rowId);
            Intent outData = new Intent();
            outData.setData(outURI);
            setResult(Activity.RESULT_OK, outData);
            finish();
        }

    });*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.log_out) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    mBuilder.setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (id == R.id.help)
        {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Help")
                    .setMessage("This application is used to list and monitor all your Internet of Thing" +
                            " devices.\n\n" +
                            "To add a device, click the button on the bottom right-hand corner of the screen.\n\n" +
                            "To see graphs based upon a device, click on the device in the list on the Main Menu.")

                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


}


