package com.example.madcampserverapp.ui.gallery;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.content.Intent;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentGallery extends Fragment {
    private FragmentGallery mFragment = this;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private Context mContext;
    private GridView mGridView;
    private FloatingActionButton mFloatButton;

    private int mCellSize;
    private String mFacebookID;

    private String mCurrentPhotoPath;
    private String mImageDirPath;

    private String[] mImagePaths;
    private ImageAdapter mImageAdapter;
    private ArrayList<Image> mImageArrayList;

    private int click_enable;
    ////////////////////////////////////////////
    private ArrayList<Bitmap> mBitmapArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_gallery, container, false);

        mContext = view.getContext();
        mImageDirPath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/MadCampApp";
        mFacebookID = ((MainActivity) getActivity()).getFacebookID();
        System.out.println(mFacebookID);

        /* Get GRID_VIEW */
        mGridView = (GridView) view.findViewById(R.id.grid_view);

        requestBitmapArrayList();

        mGridView.setAdapter(mImageAdapter);

        /* Floating camera button */
        mFloatButton = view.findViewById(R.id.cameraIcon);
        mFloatButton.setVisibility(View.VISIBLE);

//        if (((MainActivity) getActivity()).isSelection()) {
//            mFloatButton.setVisibility(View.GONE);
//            mGridView.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    mGridView.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View v,
//                                                int position, long id) {
//                            if (click_enable == 1) {
//                                Intent i = new Intent(getActivity(), FullImageActivity.class);
//                                i.putExtra("id", position);
//                                i.putExtra("imagePaths", mImagePaths);
//                                i.putExtra("imageDirPath", mImageDirPath);
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
//                            }
//                        }
//                    });
//
//                    ((MainActivity) mContext).finishSelectImage(mImageArrayList.get(position));
//                }
//            });
//
//            return view;
//        }

        setListener();

        return view;
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Timestamp(System.currentTimeMillis()));
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(mImageDirPath);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("captureCamera Error", ex.toString());
                return;
            }

            Uri photoURI = FileProvider.getUriForFile(mContext, "com.example.madcampserverapp.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                galleryAddPic();

                /* Update View */
                mImageAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("Request Take Photo", e.toString());
            }
        }
    }

    private Bitmap rotateImage (Bitmap bitmap, String path) {
        /* Code below rotates the bitmap to be original direction */
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                default:
                    break;
            }

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);

        Bitmap bitmap = rotateImage(BitmapFactory.decodeFile(mCurrentPhotoPath), mCurrentPhotoPath);
        /* Add bitmap on array lists */
        mBitmapArrayList.add(0, bitmap);
        mImageArrayList.add(new Image(bitmap, mCellSize));

        /* Upload an image */
        String testUrl = "http://192.249.19.242:7380" + "/gallery/upload";
        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", mFacebookID);
        contentValues.put("file_name", mCurrentPhotoPath);

        final String path = mCurrentPhotoPath;

        MyResponse response = new MyResponse() {
            @Override
            public void response(byte[] result) {
                Log.e("hello", new String(result));
                /* Delete an image */
                File fdelete = new File(path);
                fdelete.delete();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fdelete);
                mediaScanIntent.setData(contentUri);
                mContext.sendBroadcast(mediaScanIntent);
            }
        };

        NetworkTask networkTask = new NetworkTask(testUrl, bitmap, contentValues, response);
        networkTask.execute(null);

        Toast.makeText(mContext, "Uploaded", Toast.LENGTH_SHORT).show();
    }

    private void initFragment() {
        /* Make an array of image paths */
        /* Get proper CELL_SIZE which is (width pixels - space between cells) / 3 */
        mCellSize = (getResources().getDisplayMetrics().widthPixels - mGridView.getRequestedHorizontalSpacing()) / 3;

        /* Make an array list of IMAGEs */
        mImageArrayList = new ArrayList<>();
        for (int i = 0; i < mBitmapArrayList.size(); i++) {
            mImageArrayList.add(new Image(mBitmapArrayList.get(i), mCellSize));
        }

        /* Set new image adapter to GRIDVIEW */
        mImageAdapter = new ImageAdapter(getActivity(), mCellSize, mImageArrayList);

        click_enable = 1;
    }

    private void requestBitmapArrayList() {
        String url = "http://192.249.19.242:7380";
        String testUrl = url + "/gallery/download";
        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", mFacebookID);
        contentValues.put("skip_number", "0");
        contentValues.put("require_number", "0");

        MyResponse response = new MyResponse() {
            @Override
            public void response(byte[] result) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(result));
                    JSONArray jsonArray = jsonObject.getJSONArray("images");

                    mBitmapArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        byte[] imageByteArray = Base64.decode(jsonArray.getJSONObject(i).getString("image"), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                        mBitmapArrayList.add(bitmap);
                    }

                    initFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(testUrl, contentValues, response);
        networkTask.execute(null);
    }

    private void setListener() {
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        /* Set click listener. Start FULL_IMAGE_ACTIVITY with POSITION which
            indicates an clicked image */
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (click_enable == 1) {
                    Intent i = new Intent(getActivity(), FullImageActivity.class);

                    i.putExtra("id", position);
                    i.putExtra("fb_id", mFacebookID);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                }
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @SuppressLint("FragmentBackPressedCallback")
             @Override
             public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                 click_enable = 0;
                 final ImageSelectAdapter deleteAdapter = new ImageSelectAdapter(mContext, mFacebookID, mCellSize, mImageArrayList);
                 mGridView.setAdapter(deleteAdapter);
                 mFloatButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.trash));

                 final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                     @Override
                     public void handleOnBackPressed() {
                         mGridView.setAdapter(mImageAdapter);
                         mFloatButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.camera));

                         click_enable = 1;
                         mFloatButton.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 dispatchTakePictureIntent();
                             }
                         });

                         this.remove();
                     }
                 };
                 requireActivity().getOnBackPressedDispatcher().addCallback(mFragment, callback);

                 mFloatButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         deleteAdapter.deleteChecked();
                         mGridView.setAdapter(mImageAdapter);
                         mFloatButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.camera));


                         click_enable = 1;
                         mFloatButton.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 dispatchTakePictureIntent();
                             }
                         });

                         callback.remove();
                     }
                 });
                 return false;
             }
         }
    );
    }

    public static class NetworkTask extends ThreadTask<Void, byte[]> {

        private String mUrl;
        private MyResponse mMyResponse;

        private Bitmap mBitmap;
        private ContentValues mValues;
        private JSONObject mJSONObject;

        public NetworkTask(String url, Bitmap bitmap, ContentValues contentValues, MyResponse myResponse) {
            mUrl = url;
            mBitmap = bitmap;
            mValues = contentValues;
            mMyResponse = myResponse;
        }

        public NetworkTask(String url, ContentValues values, MyResponse myResponse) {
            mUrl = url;
            mValues = values;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected byte[] doInBackground(Void arg) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            if (mBitmap != null)
                return requestHttpURLConnection.uploadImage(mUrl, mBitmap, mValues);
            else if (mValues != null)
                return requestHttpURLConnection.request(mUrl, mValues);
            else
                return null;
        }

        @Override
        protected void onPostExecute(byte[] result) {
            mMyResponse.response(result);
        }
    }
}
