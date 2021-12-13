package com.tezodevi.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {

    public static Bitmap toBitmap(String imageData) {
        byte[] decodedImageBytes = Base64.decode(imageData, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
    }

    public static byte[] toByteArray(Context context, Uri imageUri) {
        try (InputStream iStream = context.getContentResolver().openInputStream(imageUri)) {
            return getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        return ByteStreams.toByteArray(inputStream);
    }
}
