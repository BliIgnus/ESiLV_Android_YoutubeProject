package fr.esilv.s8.youtubeproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView youtubeSearch = (SearchView) this.findViewById(R.id.searchYoutube);


        youtubeSearch.setActivated(true);
        youtubeSearch.setQueryHint("Youtube search");
        youtubeSearch.onActionViewExpanded();
        youtubeSearch.clearFocus();
        youtubeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit (String query) {
                boolean success = searchOnYoutube(query);

                return success;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return false;
            }
        });
    }






    protected void fillResults(String jsonResponse) throws JSONException {

        JSONObject json = new JSONObject(jsonResponse);
        JSONArray items = json.getJSONArray("items");

        final List<Video> videosListView = new ArrayList<Video>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject videoObject = items.getJSONObject(i);
            JSONObject videoSnippet = videoObject.getJSONObject("snippet");
            JSONObject videoId = videoObject.getJSONObject("id");
            String id = videoId.getString("videoId");
            String title = videoSnippet.getString("title");
            String date = videoSnippet.getString("publishedAt").substring(0, 10);
            String author = videoSnippet.getString("channelTitle");
            String description = videoSnippet.getString("description");

            String thumbnails = videoSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

            videosListView.add(new Video(title, description, thumbnails, id, date, author));


        }

        VideoAdapter adapter = new VideoAdapter(MainActivity.this, videosListView);

        ListView youtubeList = (ListView) this.findViewById(R.id.listResults);
        youtubeList.setAdapter(adapter);
        youtubeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video = videosListView.get(position);
                PlayerActivity.start(MainActivity.this, video);
            }
        });
    }

    protected boolean searchOnYoutube(String query) {
        //System.out.println(query);

        String requestAPI = "https://www.googleapis.com/youtube/v3/search";
        String partAPI = "?part=" + Config.YOUTUBE_PART;
        String queryAPI = "&q=" + query.replace(' ', '+');
        String typeAPI = "&type=" + Config.YOUTUBE_TYPE;
        String keyAPI = "&key=" + Config.YOUTUBE_API_KEY;

        String url = requestAPI + partAPI + queryAPI + typeAPI + keyAPI;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            //JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();
                            //JSONObject jsonResponse = new JSONObject(response);
                            //Fill the ListView with the JSON data
                            fillResults(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);

        return false;
    }
}
