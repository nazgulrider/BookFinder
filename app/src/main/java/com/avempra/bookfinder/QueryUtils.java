package com.avempra.bookfinder;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by shres on 5/21/2017.
 */

public class QueryUtils {

    private static String searchQuery;

    public static void setSearchQuery(String searchQuery) {
        QueryUtils.searchQuery = searchQuery;
    }

    private static String numberOfResults;

    private static final String BASE_URL="https://www.googleapis.com/books/v1/volumes";
    private static final String QUERY_PARAM="q";
    private static final String MAX_RESULTS_PARAM="maxResults";

    public QueryUtils() {
    }


    private static URL returnURL(){
        Uri queryUri=Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM,searchQuery)
                .appendQueryParameter(MAX_RESULTS_PARAM,numberOfResults)
                .build();
        URL queryURL=null;
        try {
            queryURL=new URL(queryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryURL;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";
        if(url==null) {
            return jsonResponse;
        }

        HttpURLConnection connection=null;
        InputStream inputStream=null;

        try {
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.connect();
            if(connection.getResponseCode()==200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e("QueryUtils","Error Response Code: "+connection.getResponseCode());
            }


        } catch (IOException e) {
            Log.e("QueryUtils", "Failed to acquire json string",e);
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }


        return jsonResponse;
    }
    public static Drawable loadImageFromWeb(String url){
        Drawable image=null;
        try {
            InputStream is=(InputStream) new URL(url).getContent();
            image=Drawable.createFromStream(is,"image");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return image;
    }

    public static List<Book> extractBooks(){

        List<Book> books=new ArrayList<Book>();
        URL url= returnURL();
        Log.i(TAG, "extractBooks URl "+url);
        String jsonResponse=null;
        Drawable image=null;
        try {
            jsonResponse=makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            JSONArray bookArray=jsonObject.getJSONArray("items");

            for (int i=0;i<bookArray.length();i++) {
                JSONObject bookObject=bookArray.getJSONObject(i);
                JSONObject volumeInfo=bookObject.getJSONObject("volumeInfo");

                String title;
                if(volumeInfo.has("title")){
                    title=volumeInfo.getString("title");
                }else {
                    title="No title available";
                }

                String subTitle;
                if(volumeInfo.has("subtitle")){
                    subTitle=volumeInfo.getString("subtitle");
                }else {
                    subTitle="";
                }

                JSONArray authorArray;
                String authors;
                if(volumeInfo.has("authors")){
                    authorArray=volumeInfo.getJSONArray("authors");
                    authors=authorArray.getString(0);
                    for (int j = 1; j <authorArray.length() ; j++) {
                        authors+=", "+authorArray.getString(j);
                    }
                }else {
                    authors="No authors available";
                }

                JSONArray categoriesArray;
                String categories;
                if(volumeInfo.has("categories")){
                    categoriesArray=volumeInfo.getJSONArray("categories");
                    categories=categoriesArray.getString(0);
                    for (int j = 1; j <categoriesArray.length() ; j++) {
                        categories+=", "+categoriesArray.getString(j);
                    }
                }else {
                    categories="No categories available";
                }

                String description;
                if(volumeInfo.has("description")){
                    description=volumeInfo.getString("description");
                }else{
                    description="No description available";
                }

                double rating;
                if(volumeInfo.has("averageRating")){
                    rating=volumeInfo.getDouble("averageRating");
                }else{
                    rating=0.0;
                }

                String ratingCount;
                if(volumeInfo.has("ratingsCount")){
                    ratingCount=""+volumeInfo.getInt("ratingsCount");
                }else{
                    ratingCount="N/A";
                }

                JSONObject imageUrlObject;
                String imageUrl="";
                String previewUrl;
                if(volumeInfo.has("previewLink")){
                    previewUrl=volumeInfo.getString("previewLink");
                }else{
                    previewUrl="";
                }

                if(volumeInfo.has("imageLinks")){
                    imageUrlObject=volumeInfo.getJSONObject("imageLinks");
                    if(imageUrlObject.has("thumbnail")) {
                        imageUrl = imageUrlObject.getString("thumbnail");
                        image=loadImageFromWeb(imageUrl);
                    }else{
                        imageUrl="";
                    }

                }
                books.add(new Book(title,subTitle,authors,categories,description,rating,ratingCount,imageUrl,previewUrl,image));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils","error extracting Json Response", e);
        }
        return books;
    }

    public static void setNumberOfResults(String numberOfResults) {
        QueryUtils.numberOfResults = numberOfResults;
    }
}
