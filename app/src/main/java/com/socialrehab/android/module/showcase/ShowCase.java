package com.socialrehab.android.module.showcase;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kepler.projectsupportlib.BaseActivity;
import com.socialrehab.R;
import com.socialrehab.android.support.SharedPref;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;

public class ShowCase extends BaseActivity implements View.OnClickListener {

    public static final String DATA = "data";
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.next)
    TextView next;
    private ShowCasePagerAdapter showCasePagerAdapter;
    private int[] _imagePaths;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey(DATA)) {
            onBackPressed();
            return;
        }
        _imagePaths = bundle.getIntArray(DATA);
        indicator.setRadius(5);
        indicator.setFillColor(getResources().getColor(R.color.app_dark_txt_color));
        indicator.setStrokeColor(getResources().getColor(R.color.app_dark_txt_color));
        indicator.setStrokeWidth(1);
        indicator.setSnap(false);
        showCasePagerAdapter = new ShowCasePagerAdapter();
        pager.setAdapter(showCasePagerAdapter);
        indicator.setViewPager(pager);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(pager.getCurrentItem()==_imagePaths.length-1){
                    next.setText(R.string.got_it);
                }else{
                    next.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected ProgressBar getHorizontalProgressBar() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_show_case;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.next:
                if(pager.getCurrentItem()==_imagePaths.length-1){
                    SharedPref.getInstance(getApplicationContext()).setShowCaseShowed();
                    onBackPressed();
                }else {
                    pager.setCurrentItem(pager.getCurrentItem()+1,true);
                }
                break;
        }
    }

    class ShowCasePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return _imagePaths.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return pageTitle[position];
//    }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
//                false);
//
//        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
//        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

            final ImageView imageView = new ImageView(ShowCase.this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(_imagePaths[position]);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);

        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }


    }
}
