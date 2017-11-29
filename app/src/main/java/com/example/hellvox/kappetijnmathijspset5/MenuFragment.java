package com.example.hellvox.kappetijnmathijspset5;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class MenuFragment extends ListFragment {

    // Initialize variables
    JSONObject ObjectArray;
    String menu_value;
    ArrayList<Food> foodList = new ArrayList<>();
    FoodListAdapater adapter;
    static RestoDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        menu_value = arguments.getString("category");
        db = RestoDatabase.getInstance(getActivity().getApplicationContext());
        adapter = new FoodListAdapater(getContext(), R.layout.adapter_view_layout, foodList);

        // Get the menu items in a given categorie and set the result in a food object.
        String url = "https://resto.mprog.nl/menu";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray array = response.optJSONArray("items");
                        for(int i=0; i<array.length(); i++) {
                            ObjectArray = array.optJSONObject(i);
                            if (ObjectArray.optString("category").equals(menu_value)) {
                                foodList.add(new Food(ObjectArray.optString("name"), ObjectArray.optInt("price"), ObjectArray.optInt("id"), ObjectArray.optString("image_url")));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);
        this.setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Food object = (Food) this.getListAdapter().getItem(position);
        String name = object.getName();
        int dishid = object.getMenuid();
        int price = object.getPrice();
        String url = object.getURL();
        int amount = db.get(dishid);
        if (amount > 0) {
            amount += 1;
            db.update(dishid, amount);
            MainActivity.setupBadge();
            Toast.makeText(getActivity().getApplicationContext(),"Added one more " + name + "!", Toast.LENGTH_SHORT).show();
        } else {
            addItem(dishid, name, price, url);
            MainActivity.setupBadge();
            Toast.makeText(getActivity().getApplicationContext(),"Added "+name+"!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem(int id, String title, int price, String url) {
        db.insertOrder(id, title, price, 1, url);
    }
}
