package com.example.ahmed.garrab.db;

import android.provider.BaseColumns;

/**
 * Created by Shady on 11/03/2017.
 */

public class GlassesContract {

    public static final class GlassesEntry implements BaseColumns {

        public static final String TABLE_NAME = "glasses";

        public static final String MODEL = "model";
        public static final String BRAND = "brand";
        public static final String URL = "url";
        public static final String PRICE = "price";
    }

}
