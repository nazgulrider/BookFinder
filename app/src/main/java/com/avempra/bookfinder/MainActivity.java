package com.avempra.bookfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SearchView searchView=(SearchView)findViewById(R.id.search_bar);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userInput=searchView.getQuery().toString();
                Intent showBookActivity=new Intent(MainActivity.this,BookActivity.class);
                showBookActivity.putExtra("UserInput",userInput);

                startActivity(showBookActivity);
            }
        });
    }
}
