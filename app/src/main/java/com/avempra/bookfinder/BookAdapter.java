package com.avempra.bookfinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shres on 5/21/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, @NonNull List<Book> booksList) {
        super(context, 0, booksList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View layoutView=convertView;

        if(layoutView==null){
            layoutView= LayoutInflater.from(getContext()).inflate(R.layout.book_info_layout,parent,false);
        }

        Book book=getItem(position);


        ImageView imageView=(ImageView) layoutView.findViewById(R.id.image_view);
        imageView.setImageDrawable(book.getImage());

        TextView titleView=(TextView) layoutView.findViewById(R.id.book_title);
        titleView.setText(book.getTitle());

        TextView subTitleView=(TextView)layoutView.findViewById(R.id.sub_title);
        subTitleView.setText(book.getSubTitle());

        TextView authorsView=(TextView)layoutView.findViewById(R.id.authors);
        authorsView.setText(book.getAuthors());

        TextView categoryView=(TextView)layoutView.findViewById(R.id.categories);
        categoryView.setText("Categories: "+book.getCategories());

        RatingBar ratingView=(RatingBar)layoutView.findViewById(R.id.rating);
        ratingView.setRating((float)book.getAverageRating());

        TextView countView=(TextView)layoutView.findViewById(R.id.rating_count);
        countView.setText("Total Ratings: " + book.getRatingsCount());

        TextView descriptionView=(TextView)layoutView.findViewById(R.id.description);
        descriptionView.setText(book.getDescription());


        return layoutView;
    }
}
