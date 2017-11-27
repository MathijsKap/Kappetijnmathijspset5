package com.example.hellvox.kappetijnmathijspset5;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class RestoAdapter extends ResourceCursorAdapter {

    public RestoAdapter(Context context, Cursor cursor, int flags) {
        super(context, R.layout.row_order, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.textView5);
        TextView idd = view.findViewById(R.id.id);
        TextView amountt = view.findViewById(R.id.textamount);
        TextView pricee = view.findViewById(R.id.textPrice);
        ImageView imageView = view.findViewById(R.id.image);

        String title = cursor.getString(cursor.getColumnIndex( "title"));
        String id  = cursor.getString(cursor.getColumnIndex( "_id"));
        int amount = cursor.getInt(cursor.getColumnIndex( "amount"));
        int price = cursor.getInt(cursor.getColumnIndex( "price"));
        String image = cursor.getString(cursor.getColumnIndex("url"));

        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.ic_food_fork_drink)
                .into(imageView);
        name.setText(title);
        amountt.setText("" + amount);
        pricee.setText(""+price*amount);
        idd.setText(id);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        private DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
