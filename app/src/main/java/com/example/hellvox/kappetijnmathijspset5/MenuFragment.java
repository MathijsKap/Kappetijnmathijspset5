package com.example.hellvox.kappetijnmathijspset5;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {

    ArrayList<String> dishesArray = new ArrayList<String>();
    ArrayAdapter<String> menuItems;
    JSONObject ObjectArray;
    String menu_value;


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
        menuItems = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, dishesArray);
        String url = "https://resto.mprog.nl/menu";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray array = response.optJSONArray("items");
                        for(int i=0; i<array.length(); i++) {
                            ObjectArray = array.optJSONObject(i);
                            if (ObjectArray.optString("category").equals(menu_value)) {
                                dishesArray.add(ObjectArray.optString("name"));
                            }
                        }
                        menuItems.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);
        this.setListAdapter(menuItems);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Object object = this.getListAdapter().getItem(position);
        String pen = object.toString();
        Toast.makeText(getActivity().getApplicationContext(), pen, Toast.LENGTH_SHORT).show();
    }
}
