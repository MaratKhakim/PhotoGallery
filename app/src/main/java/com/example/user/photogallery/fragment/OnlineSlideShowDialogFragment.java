package com.example.user.photogallery.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.OnlinePhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class OnlineSlideShowDialogFragment extends DialogFragment {

    private static final String TAG = "OnlineSlideShow";

    private ArrayList<OnlinePhoto> images;

    public static OnlineSlideShowDialogFragment newInstance() {
        OnlineSlideShowDialogFragment fragment = new OnlineSlideShowDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        Bundle bundle = getArguments();

        images = (ArrayList<OnlinePhoto>) bundle.getSerializable("images");
        int selectedPosition = bundle.getInt("position", 0);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.setCurrentItem(selectedPosition, false);

        return view;
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final int finalPosition = position;

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            OnlinePhoto image = images.get(position);

            /*RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);*/

            Glide.with(OnlineSlideShowDialogFragment.this).load(image.getUrl())
                    .thumbnail(0.5f)
                    //.apply(requestOptions)
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
                    OnlineSlideShowDialogFragment.this.startActivity(Intent.createChooser(shareIntent, "Share"));
                }
            });

            String labelText = String.format(getResources().getString(R.string.label_count), position+1, images.size());
            labelCount.setText(labelText);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File dir = new File(path + "/"+getResources().getString(R.string.folder_name)+"/");
                    if (!dir.exists())
                        dir.mkdir();

                    Log.e(TAG, dir.toString());

                    File file = new File(dir, "photo_gallery_" + images.get(position).getId() + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        Bitmap bitmap = ((BitmapDrawable) imageViewPreview.getDrawable()).getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, fOut);
                        fOut.flush();
                        fOut.close();

                        //MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

                        String saved = getResources().getString(R.string.saved);
                        Toast.makeText(getActivity(), saved + " " + file.toString(), Toast.LENGTH_LONG).show();
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
