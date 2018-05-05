package com.example.user.photogallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.SavedPhoto;

import java.util.ArrayList;

public class SavedSlideShowDialogFragment extends DialogFragment {

    private static final String TAG = "SavedSlideShowFragment";

    private ArrayList<SavedPhoto> mImages;

    public static SavedSlideShowDialogFragment newInstance() {
        return new SavedSlideShowDialogFragment();
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

        mImages = (ArrayList<SavedPhoto>) bundle.getSerializable("images");
        int selectedPosition = bundle.getInt("position", 0);

        ViewPagerAdapter myViewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.setCurrentItem(selectedPosition, false);

        return view;
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.saved_photo_fullscreen, container, false);

            final ImageView imageViewPreview = (ImageView) view.findViewById(R.id.saved_image_preview);

            SavedPhoto image = mImages.get(position);

            Glide.with(SavedSlideShowDialogFragment.this).load(image.getUri())
                    .thumbnail(0.5f)
                    .into(imageViewPreview);

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
