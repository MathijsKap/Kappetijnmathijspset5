package com.example.hellvox.kappetijnmathijspset5;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Initialize variables
    static TextView textCartItemCount;
    static RestoDatabase db;
    private final String TAG = getClass().getSimpleName();
    private CategoriesFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Restaurant");
        db = RestoDatabase.getInstance(getApplicationContext());

        // source: https://stackoverflow.com/questions/5164126/retain-the-fragment-object-while-rotating
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if(fragment == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            mHomeFragment = new CategoriesFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mHomeFragment, TAG)
                    .commit();
        } else {
            // get our old fragment back !
            mHomeFragment = (CategoriesFragment) fragment;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.buttonCart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buttonCart: {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                OrderFragment fragment = new OrderFragment();
                fragment.show(ft, "dialog");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Function to make a badge with the amount of items in the cart.
    // source: https://stackoverflow.com/questions/43194243/notification-badge-on-action-item-android
    public static void setupBadge() {
        int mCartItemCount = 0;
        for (int i=0;i<7;i++) {
            mCartItemCount += db.get(i);
        }
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
