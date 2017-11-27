package com.example.hellvox.kappetijnmathijspset5;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


public class OrderFragment extends DialogFragment implements View.OnClickListener {

    static RestoDatabase db;
    static RestoAdapter adapter;
    Cursor mCursor;
    private ListView list;
    Button clearbutton;
    Button orderbutton;
    Activity activityObj;
    TextView total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        list = view.findViewById(R.id.list);
        list.setOnItemLongClickListener(new GoButtonLongClickListener());
        total = (TextView) view.findViewById(R.id.total);
        orderbutton = (Button) view.findViewById(R.id.Order);
        orderbutton.setOnClickListener(this);
        clearbutton = (Button) view.findViewById(R.id.Clear);
        clearbutton.setOnClickListener(this);
        activityObj = this.getActivity();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        db = RestoDatabase.getInstance(getContext());
        mCursor = db.selectAll();
        adapter = new RestoAdapter(getContext(), mCursor, 0);
        list.setAdapter(adapter);
        total();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Order: {
                if (!(adapter.getCount() == 0)) {
                    db.clear();
                    String url = "https://resto.mprog.nl/order";
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int time = response.optInt("preparation_time");
                            Toast.makeText(activityObj, "Order coming in " + time + " minutes", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activityObj, "Something went wrong, try again.", Toast.LENGTH_LONG).show();
                        }
                    });
                    // Access the RequestQueue through your singleton class.
                    MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
                    updateData();
                    MainActivity.setupBadge();
                } else Toast.makeText(activityObj, "Empty order!", Toast.LENGTH_LONG).show();
                dismiss();
                break;
          }
            case R.id.Clear: {
                dismiss();
                break;
            }
        }
    }

    private void total() {
        int totaal = 0;
        for (int i=0;i<7;i++) {
            totaal += db.get(i)*db.getprice(i);
        }
        total.setText("Total: €"+ totaal);
    }

    private class GoButtonLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            LinearLayout item = (LinearLayout) view;
            TextView idHidden = (TextView) item.getChildAt(0);
            db.delete(Long.parseLong(idHidden.getText().toString()));
            Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
            updateData();
            MainActivity.setupBadge();
            total();
            return true;
        }
    }

    private static void updateData() {
        adapter.swapCursor(db.selectAll());
    }
}
