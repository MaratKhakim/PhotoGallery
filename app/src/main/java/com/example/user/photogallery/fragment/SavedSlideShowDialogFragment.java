package com.example.user.photogallery.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.Photo;

public class SavedSlideShowDialogFragment extends SlideShowDialogFragment {

    public static SavedSlideShowDialogFragment newInstance() {
        return new SavedSlideShowDialogFragment();
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

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.saved_photo_fullscreen, container, false);

            final ImageView imageViewPreview = (ImageView) view.findViewById(R.id.saved_image_preview);
            Photo photo = mImages.get(position);

            Glide.with(SavedSlideShowDialogFragment.this).load(photo.getLink())
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
