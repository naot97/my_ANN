package com.example.sipln.movieapp.Presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sipln.movieapp.R;

import java.io.InputStream;
import java.util.List;

public class ActorAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> nameActor;
    private List<String> imgActor;

    public ActorAdapter(Context mContext, List<String> nameActor, List<String> imgActor) {
        this.mContext = mContext;
        this.nameActor = nameActor;
        this.imgActor = imgActor;
    }


    @Override
    public int getCount() {
        return nameActor.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grv_actor_row, null);
        }

        TextView textView = convertView.findViewById(R.id.txtActor);
        ImageView imageView = convertView.findViewById(R.id.imgViewActor);

        textView.setText(nameActor.get(position));
        Log.e("TEXTVIEW", textView.getText().toString());
        new DownloadImageTask(imageView).execute(imgActor.get(position));
        //imageView.setImageURI(Uri.parse("https://m.media-amazon.com/images/M/MV5BMTUwNjQ0ODkxN15BMl5BanBnXkFtZTcwMDc5NjQwNw@@._V1_UY317_CR11,0,214,317_AL_.jpg"));

        return convertView;
    }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
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
