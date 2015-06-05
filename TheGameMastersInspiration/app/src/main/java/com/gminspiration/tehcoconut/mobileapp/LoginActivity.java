package com.gminspiration.tehcoconut.mobileapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    EditText userEdit,passEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEdit = (EditText) findViewById(R.id.et_username);
        passEdit = (EditText) findViewById(R.id.et_password);


    }

    public void login(View v){
        String username = userEdit.getText().toString();
        String password = passEdit.getText().toString();

        //Toast.makeText(getApplicationContext(), "Tried to Log in!", Toast.LENGTH_SHORT).show();
        new BackgroundLogin(getApplicationContext()).execute(username, password);
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
}
