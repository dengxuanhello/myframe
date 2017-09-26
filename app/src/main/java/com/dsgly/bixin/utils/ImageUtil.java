package com.dsgly.bixin.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class ImageUtil {
    /**
     * 根据屏幕大小读取图片
     *
     * @param path
     * @return
     */
    public static Bitmap readBitmap(String path) {
        //读取图片宽度高度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        Point point = DisplayUtil.getScreenSize();
        //根据比例读取
        options.inSampleSize = calculateInSampleSize(options, point.x, point.y);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    public static boolean saveBitmapFile(Bitmap bitmap, File file) {
        if (bitmap == null || file == null) {
            return false;
        }
        boolean result;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        return result;
    }

    public static void saveBitmapFile2(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float limitWidth, float limitHeight) {
        Matrix matrix = new Matrix();
        float widthScale = limitWidth / bitmap.getWidth();
        float heightScale = limitHeight / bitmap.getHeight();
        if (widthScale < heightScale) {
            matrix.postScale(widthScale, widthScale);
        } else {
            matrix.postScale(heightScale, heightScale);
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight(), matrix, true);
        /**SDK不支持尺寸为奇数的图片，生成尺寸为偶数图片**/
        int newW = newBitmap.getWidth(), newH = newBitmap.getHeight();
        if (newBitmap.getWidth() % 2 == 1 || newBitmap.getHeight() % 2 == 1) {
            if (newBitmap.getWidth() % 2 == 1) {
                newW = newBitmap.getWidth() - 1;
            }
            if (newBitmap.getHeight() % 2 == 1) {
                newH = newBitmap.getHeight() - 1;
            }
            return Bitmap.createBitmap(newBitmap, 0, 0, newW, newH);
        }
        return newBitmap;
    }

    public static byte[] toBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    // 转换在线人数显示字样
    public static String transfer(int count) {
        if (count < 10000) {
            return count + "";
        } else {
            return new DecimalFormat("0.0").format((float) count / 10000) + "万";
//        } else if (count < 1000 * 1000) {
//            return count / 1000 + "k";
//        } else if (count < 1000 * 1000 * 10) {
//            return new DecimalFormat("0.0").format((float) count / (1000 * 1000)) + "m";
//        } else {
//            return count / (1000 * 1000) + "m";
        }
    }

//    public static void loadImage(Context context, String path, ImageView imageView) {
//        if (context == null || isActivityDestroyed(context)) {
//            return;
//        }
//        if (path != null && path.endsWith("gif")) {
//            // 去掉.asGif()，避免该后缀下非gif图不能正常展示
//            Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate().into(imageView);
//        } else {
//            Glide.with(context).load(path).dontAnimate().into(imageView);
//        }
//    }

    public static File fetchImage(Context context, String path) throws ExecutionException, InterruptedException {
        return Glide.with(context).load(path).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
    }

    private static boolean isActivityDestroyed(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (context instanceof Activity && ((Activity) context).isDestroyed()) {
                return true;
            }
        }
        return false;
    }

    public static void loadName(TextView tv, String nickName) {
        tv.setText(TextUtils.isEmpty(nickName) ? "小萝卜" : nickName);
    }

    /*public static void loadImageAvatar(Context context, String path, ImageView imageView) {
        if (context == null)
            return;
        Glide.with(context).load(path).placeholder(R.drawable.ic_default_avatar).dontAnimate().into(imageView);
    }

    public static void loadImageAvatar(Context context, int resourceId, ImageView imageView) {
        if (context == null)
            return;
        Glide.with(context).load(resourceId).dontAnimate().into(imageView);
    }*/

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void downloadImage(final Context context, String url, final OnDownloadImageListener listener) {
        new AsyncTask<String, Void, File>() {
            @Override
            protected File doInBackground(String... params) {
                //DebugLog.e("正在下载图片", "" + params[0]);
                try {
                    return Glide
                            .with(context)
                            .load(params[0])
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (file != null && file.exists()) {
                    if (listener != null) {
                        listener.onDownload(file.getAbsolutePath());
                    }
                }
            }
        }.execute(url);
    }

    public interface OnDownloadImageListener {
        void onDownload(String file);
    }
}
