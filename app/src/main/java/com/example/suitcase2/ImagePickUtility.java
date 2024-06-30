package com.example.suitcase2;

import android.app.Activity;
import android.view.View;

public class ImagePickUtility {
    public static void picImage(View view, Activity activity){
        ImagePicker.with(activity)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start();
    }
}
