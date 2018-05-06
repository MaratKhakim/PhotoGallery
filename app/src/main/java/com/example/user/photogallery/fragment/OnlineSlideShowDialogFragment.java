package com.example.user.photogallery.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.Photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OnlineSlideShowDialogFragment extends SlideShowDialogFragment {

    private static final String TAG = "OnlineSlideShow";

    public static OnlineSlideShowDialogFragment newInstance() {
        return new OnlineSlideShowDialogFragment();
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new ViewPagerAdapter();
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final int finalPosition = position;

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.online_photo_fullscreen, container, false);

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

            Photo image = mImages.get(position);

            Glide.with(OnlineSlideShowDialogFragment.this).load(image.getLink())
                    .thumbnail(0.5f)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .into(imageViewPreview);

            // share a photo
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String appvalue = getResources().getString(R.string.share_begin);
                    String applicationName = getResources().getString(R.string.app_name);

                    Intent shareIntent = new Intent();
                    shareIntent.setType("text/plain");
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, appvalue + " " + applicationName + ": " + mImages.get(finalPosition).getLink());
                    OnlineSlideShowDialogFragment.this.startActivity(Intent.createChooser(shareIntent, "Share"));
                }
            });

            // text to show a postion of the current photo
            String labelText = String.format(getResources().getString(R.string.label_count), position+1, mImages.size());
            labelCount.setText(labelText);

            // save a photo to the external storage
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream outputStream;

                    // create a folder to save photos
                    File dir = new File(path + "/"+getResources().getString(R.string.folder_name)+"/");
                    if (!dir.exists())
                        dir.mkdir();

                    Log.d(TAG, dir.toString());

                    // give a name to the photo
                    File file = new File(dir, "photo_gallery_" + mImages.get(position).getId() + ".jpg");
                    try {
                        outputStream = new FileOutputStream(file);
                        Bitmap bitmap = ((BitmapDrawable) imageViewPreview.getDrawable()).getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, outputStream);
                        outputStream.flush();
                        outputStream.close();

                        String saved = getResources().getString(R.string.saved);
                        Toast.makeText(getActivity(), saved + " " + file.toString(), Toast.LENGTH_LONG).show();

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
            return mImages.size();
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
