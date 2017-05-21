package com.example.g50.chitr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        btn1=(Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        registerScreen.setOnClickListener(this);
    }


    public void onClick(View view)
    {
        if(view.getId()==R.id.btn1) {
            Intent i = new Intent(this, ActivityLanding.class);
            startActivity(i);
        }
        if(view.getId()==R.id.link_to_register)
        {
            Intent i = new Intent(this,Registration.class);
            startActivity(i);
        }
    }

}
