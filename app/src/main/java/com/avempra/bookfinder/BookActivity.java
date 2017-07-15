package com.avempra.bookfinder;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private BookAdapter mAdapter;
    private static final int BOOK_LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);


        Intent intent=getIntent();
        if(intent.hasExtra("UserInput")){
            QueryUtils.setSearchQuery(intent.getStringExtra("UserInput"));
        }

        ListView bookView=(ListView) findViewById(R.id.list_view);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookView.setAdapter(mAdapter);

        LoaderManager loaderManager=getLoaderManager();

        loaderManager.initLoader(BOOK_LOADER_ID,null,this);

        bookView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book=mAdapter.getItem(position);
                if(book.getPreviewLink()!=null) {
                    Uri uri = Uri.parse(book.getPreviewLink());
                    Intent getPreview = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(getPreview);
                }
            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String maxResult=sharedPreferences.getString(getString(R.string.max_result_key),getString(R.string.max_result_value));
        QueryUtils.setNumberOfResults(maxResult);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return new AsyncBookLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> booksList) {
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.loading_indicator);
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();

        if(booksList!=null && !booksList.isEmpty()){
            mAdapter.addAll(booksList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.book_finder_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent startSettingsActivity=new Intent(this,SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key==getString(R.string.max_result_key)){
            QueryUtils.setNumberOfResults(sharedPreferences.getString(getString(R.string.max_result_key),getString(R.string.max_result_value)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}
