package com.example.ahmed.garrab.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.R;

/**
 * Created by ESC on 6/18/2016.
 */
public class RowHolder {

    private TextView model;
    private TextView brand;
    private TextView price;
    private ImageView glassesImage;

    public RowHolder(View view) {
        model = (TextView) view.findViewById(R.id.txt_model);
        brand = (TextView) view.findViewById(R.id.txt_brand);
        price = (TextView) view.findViewById(R.id.txt_price);
        glassesImage = (ImageView) view.findViewById(R.id.img_glasses);
    }

    public TextView getBrand() {
        return brand;
    }

    public ImageView getGlassesImage() {
        return glassesImage;
    }

    public TextView getModel() {
        return model;
    }

    public TextView getPrice() {
        return price;
    }
}
