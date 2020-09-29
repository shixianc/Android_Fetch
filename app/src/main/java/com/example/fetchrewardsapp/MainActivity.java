package com.example.fetchrewardsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This android program fetch data from predefined API route and process the JSON data and list the results on listView.
 */
public class MainActivity extends AppCompatActivity {

    public List<String> output;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.id_list_view);
    }

    /**
     * Button onclick function
     * Open a thread to execute API call and process the json data
     * update the listView with results.
     * @param view
     */
    public void display(View view) {
        FetchData process = new FetchData();
        try {
            output = process.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, output);
        listView.setAdapter(arrayAdapter);
    }
}
