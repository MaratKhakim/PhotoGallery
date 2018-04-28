package com.example.user.photogallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.GalleryItem;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    private final static String VAR_BUNDLE = "VarBundle";

    private String TAG = "SlideShowActivity";
    private ArrayList<GalleryItem> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount;
    private int selectedPosition = 0;

    public static Intent newIntent(Context context, Bundle bundle){
        Intent intent = new Intent(context, SlideShowActivity.class);
        intent.putExtra(VAR_BUNDLE, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_slide_show);
        setSupportActionBar(toolbar);
        setTitle("");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        lblCount = (TextView) findViewById(R.id.lbl_count);

        Bundle bundle = getIntent().getBundleExtra(VAR_BUNDLE);

        images = (ArrayList<GalleryItem>) bundle.getSerializable("images");
        selectedPosition = bundle.getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.photo_fullscreen, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            GalleryItem image = images.get(position);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(SlideShowActivity.this).load(image.getUrl())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(imageViewPreview);

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
