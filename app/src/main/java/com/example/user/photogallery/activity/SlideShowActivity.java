package com.example.user.photogallery.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.GalleryItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    private static final String TAG = "SlideShowActivity";
    private final static String VAR_BUNDLE = "VarBundle";

    private ArrayList<GalleryItem> images;

    public static Intent newIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SlideShowActivity.class);
        intent.putExtra(VAR_BUNDLE, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        Bundle bundle = getIntent().getBundleExtra(VAR_BUNDLE);

        images = (ArrayList<GalleryItem>) bundle.getSerializable("images");
        int selectedPosition = bundle.getInt("position", 0);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.setCurrentItem(selectedPosition, false);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final int finalPosition = position;

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.photo_fullscreen, container, false);

            final ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_fullscreen);
            final ImageButton btnShare = (ImageButton) view.findViewById(R.id.btnShare);
            final TextView labelCount = (TextView) view.findViewById(R.id.lbl_count);
            final ImageButton btnSave = (ImageButton) view.findViewById(R.id.btnSave);

            imageViewPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (relativeLayout.getVisibility() == View.GONE)
                        relativeLayout.setVisibility(View.VISIBLE);
                    else
                        relativeLayout.setVisibility(View.GONE);
                }
            });

            GalleryItem image = images.get(position);

            Log.e(TAG, "ID : " + image.getId());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(SlideShowActivity.this).load(image.getUrl())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(imageViewPreview);

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String appvalue = getResources().getString(R.string.share_begin);
                    String applicationName = getResources().getString(R.string.app_name);

                    Intent shareIntent = new Intent();
                    shareIntent.setType("text/plain");
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, appvalue + " " + applicationName + ": " + images.get(finalPosition).getUrl());
                    SlideShowActivity.this.startActivity(Intent.createChooser(shareIntent, "Share"));
                }
            });

            labelCount.setText((position + 1) + " of " + images.size());

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File dir = new File(path + "/PhotoGalleryApp/");
                    if (!dir.exists())
                        dir.mkdir();

                    File file = new File(dir, "photo_gallery_" + images.get(position).getId() + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        Bitmap bitmap = ((BitmapDrawable) imageViewPreview.getDrawable()).getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, fOut);
                        fOut.flush();
                        fOut.close();

                        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

                        String saved = getResources().getString(R.string.saved);
                        Toast.makeText(SlideShowActivity.this, saved + " " + file.toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Saved");
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "File not found " + e.getMessage());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
