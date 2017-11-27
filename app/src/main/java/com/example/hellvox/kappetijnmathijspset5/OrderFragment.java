package com.example.hellvox.kappetijnmathijspset5;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment {

    static RestoDatabase db;
    static RestoAdapter adapter;
    Cursor mCursor;
    private ListView list;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        list = view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        db = RestoDatabase.getInstance(getActivity().getApplicationContext());
        mCursor = db.selectAll();
        adapter = new RestoAdapter(getContext(), mCursor, 0);
        list.setAdapter(adapter);
    }
}
