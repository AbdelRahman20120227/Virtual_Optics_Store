package com.example.ahmed.garrab.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ahmed.R;
import com.example.ahmed.garrab.model.Glasses;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ESC on 6/18/2016.
 */
public class GlassesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Glasses> rows;
    private Bitmap target;
    private Context context;

    public GlassesAdapter(Context context, ArrayList<Glasses> optics) {
        this.context = context;
        this.rows = optics;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Glasses getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowHolder holder = null;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.storerow, parent, false);
            holder = new RowHolder(view);
            view.setTag(holder);
        } else {
            holder = (RowHolder) view.getTag();
        }

        Glasses row = rows.get(position);
        holder.getModel().setText(row.getModelName());
        holder.getPrice().setText(String.valueOf(row.getPrice()));
        holder.getBrand().setText(row.getBrand());

        Picasso.with(context)
                .load(row.getUrl())
                .into(holder.getGlassesImage());
        holder.getGlassesImage().setTag(row);
        return view;
    }

}
