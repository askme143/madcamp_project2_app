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
    /* Constants */
    private static final String TAG = "FragmentGallery";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private Context mContext;
    private FragmentGallery mFragment;
    private int mCellSize;
    private String mFacebookID;
    private String mImageDirPath;

    /* Path string for camera action */
    private String mCurrentPhotoPath;

    /* Variables for Grid View */
    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private ArrayList<Bitmap> mBitmapArrayList;
    private ArrayList<Image> mImageArrayList;

    private FloatingActionButton mFloatButton;

    /* if long clicked before, disable short click action */
    private boolean longClicked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_gallery, container, false);

        /* Get views */
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mFloatButton = view.findViewById(R.id.cameraIcon);

        /* Build constants */
        mContext = view.getContext();
        mFragment = this;
        mCellSize = (getResources().getDisplayMetrics().widthPixels - mGridView.getRequestedHorizontalSpacing()) / 3;
        mImageDirPath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/MadCampApp";
        mFacebookID = ((MainActivity) getActivity()).getFacebookID();

        /* Set image adapter and floating button */
        if (mImageAdapter == null) {
            mImageArrayList = new ArrayList<>();
            mImageAdapter = new ImageAdapter(mContext, mCellSize, mImageArrayList);
        }
        mGridView.setAdapter(mImageAdapter);
        mFloatButton.setVisibility(View.VISIBLE);

        /* Draw grid view */
        initFragment();

        /* If writeImageSelection */
        if (((MainActivity) getActivity()).isWriteImageSelection()) {
            mFloatButton.setVisibility(View.GONE);
            mGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    ((MainActivity) mContext).endWriteImageSelection(mImageArrayList.get(position).getOriginalImage());
                }
            });

            return view;
        }

        /* Set button listeners */
        setListener();

        return view;
    }

    private void initFragment() {
        String url = "http://192.249.19.244:1780";
        String testUrl = url + "/gallery/download";
        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", mFacebookID);
        contentValues.put("skip_number", "0");
        contentValues.put("require_number", "0");

        MyResponse response = new MyResponse() {
            @Override
            public void response(byte[] result) {
                try {
                    /* Build bitmap array list */
                    JSONObject jsonObject = new JSONObject(new String(result));
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    mBitmapArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        byte[] imageByteArray = Base64.decode(jsonArray.getJSONObject(i).getString("image"), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                        mBitmapArrayList.add(bitmap);
                    }

                    /* Add IMAGEs */
                    mImageArrayList.clear();
                    for (int i = 0; i < mBitmapArrayList.size(); i++) {
                        mImageArrayList.add(new Image(mBitmapArrayList.get(i), mCellSize));
                    }

                    mImageAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(testUrl, contentValues, response);
        networkTask.execute(null);
    }

    private void setListener() {
        /* Initialize long click indicator */
        longClicked = false;

        /* Floating button */
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dispatchTakePictureIntent(); }
        });

        /* Item click, then full image activity starts on POSITION */
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (!longClicked) {
                    Intent i = new Intent(getActivity(), FullImageActivity.class);

                    i.putExtra("id", position);
                    i.putExtra("fb_id", mFacebookID);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                }
            }
        });

        /* If long clicked, enter to selection mode */
        mGridView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @SuppressLint("FragmentBackPressedCallback")
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        longClicked = true;

                        /* Change adapter */
                        final ImageSelectAdapter deleteAdapter = new ImageSelectAdapter(mContext, mFacebookID, mCellSize, mImageArrayList);
                        mGridView.setAdapter(deleteAdapter);

                        /* Change callback button action */
                        final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                mGridView.setAdapter(mImageAdapter);
                                mFloatButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.camera));

                                longClicked = false;
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


                        /* Change floating button */
                        mFloatButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.trash));
                        mFloatButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                longClicked = false;

                                /* Delete selected images */
                                deleteAdapter.deleteChecked();

                                /* Recover the image adapter */
                                mGridView.setAdapter(mImageAdapter);

                                /* Recover floating button */
                                mFloatButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.camera));
                                mFloatButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dispatchTakePictureIntent();
                                    }
                                });

                                /* Recover callback button action */
                                callback.remove();
                            }
                        });
                        return false;
                    }
                }
        );
    }

    /* Camera action */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile = null;
            try {
                File storageDir = new File(mImageDirPath);
                if (!storageDir.exists()) { storageDir.mkdirs(); }

                @SuppressLint("SimpleDateFormat")
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Timestamp(System.currentTimeMillis()));
                photoFile = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);

                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                Log.e(TAG, ex.toString());
                return;
            }

            Uri photoURI = FileProvider.getUriForFile(mContext, "com.example.madcampserverapp.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    /* After camera action */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                /* Get file and add to the file system */
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                mContext.sendBroadcast(mediaScanIntent);

                /* Add a bitmap on array lists */
                Bitmap bitmap = rotateImage(BitmapFactory.decodeFile(mCurrentPhotoPath), mCurrentPhotoPath);
                mBitmapArrayList.add(0, bitmap);
                mImageArrayList.add(new Image(bitmap, mCellSize));

                /* Upload an image */
                String testUrl = "http://192.249.19.244:1780" + "/gallery/upload";
                ContentValues contentValues = new ContentValues();
                contentValues.put("fb_id", mFacebookID);
                contentValues.put("file_name", mCurrentPhotoPath);

                /* After Uploaded, delete an image file */
                final String path = mCurrentPhotoPath;
                MyResponse response = new MyResponse() {
                    @Override
                    public void response(byte[] result) {
                        /* TODO: Handle exceptions (null result) */
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

                /* Update View */
                mImageAdapter.notifyDataSetChanged();

                Toast.makeText(mContext, "Uploaded", Toast.LENGTH_SHORT).show();
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
