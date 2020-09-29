package com.example.fetchrewardsapp;

import android.os.AsyncTask;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Async Class used to fetch data from the API.
 * Able to Group the JSON data with list ID and sort according to names.
 */
public class FetchData extends AsyncTask<Void, Void, List<String>> {

    private static final String BASE_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    @Override
    protected List doInBackground(Void... voids) {
        List<String> output = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        try {
            // Open HTTP connection
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader((inputStream)));

            String line = "";
            // Read JSON object
            StringBuilder dataBuilder = new StringBuilder();
            while (line != null) {
                line = reader.readLine();
                dataBuilder.append(line);
            }

            JSONArray jsonArray = new JSONArray(dataBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);

                // Filter out null and empty names
                if (obj.isNull("name") || obj.getString("name").length() == 0) {
                    continue;
                }

                itemList.add(new Item(Integer.parseInt(obj.getString("listId")), obj.getString("id"), obj.getString("name")));
            }

            // Sort the list
            buildResultList(output, itemList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return output;
    }

    /**
     * Build result list by grouping list id and sort by names.
     * @param output output arraylist
     * @param list input raw data
     */
    private void buildResultList(List<String> output, List<Item> list) {

        // Group and sort the list id by using TreeMap
        Map<Integer, List<Item>> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        for (Item i : list) {
            int listId = i.getListid();
            if (map.containsKey(listId)) {
                List<Item> l = map.get(listId);
                l.add(i);
            } else {
                List<Item> l = new ArrayList<>();
                l.add(i);
                map.put(listId, l);
            }
        }
        // Sort the list by names
        for (Map.Entry<Integer, List<Item>> entry : map.entrySet()) {
            Collections.sort(entry.getValue(), new Comparator<Item>(){
                @Override
                public int compare(Item a, Item b) {
                    int n1 = Integer.parseInt(a.getName().split("\\s+")[1]);
                    int n2 = Integer.parseInt(b.getName().split("\\s+")[1]);
                    return n1 - n2;
                }
            });
            output.add("List ID: " + entry.getKey().toString());
            List<Item> curr = entry.getValue();
            for (Item i : curr) {
                output.add("   ID: "+i.getId() + ", name: " + i.getName());
            }
        }
        int a = 0;
    }

}
