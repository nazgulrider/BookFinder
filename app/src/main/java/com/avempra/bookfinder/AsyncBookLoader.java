package com.avempra.bookfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by shres on 5/22/2017.
 */

public class AsyncBookLoader extends AsyncTaskLoader<List<Book>> {


    public AsyncBookLoader(Context context) {
        super(context);

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        List<Book> bookList=QueryUtils.extractBooks();

        return bookList;
    }
}
