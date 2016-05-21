package com.test.hello.myhelloworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends ActionBarActivity {

    EditText edtName;EditText edtMail;EditText edtphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName=(EditText)findViewById(R.id.edtName);
        edtMail=(EditText)findViewById(R.id.edtEmail);
        edtphone=(EditText)findViewById(R.id.edtMobile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }
    public void onClickRegister(View v)
    {
        if(edtName.getText().length()>0 && edtMail.getText().length()>0 && edtphone.getText().length()>0)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Registration ")
                    .setMessage("Registration successfull")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            startActivity(i);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
            Toast.makeText(this,"Complete the Registration form",Toast.LENGTH_LONG).show();
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
