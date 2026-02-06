package com.example.suit_case;

import android.app.Activity;
import android.view.View;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class ImagePickerUtility {

    // A utility method to open the image picker

    public static void imagePicker(View view, Activity activity){
        ImagePicker.with(activity)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
}
