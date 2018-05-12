package com.james.androidadpractice.homepage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.james.androidadpractice.R;
import com.james.androidadpractice.util.ActivityUtils;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        startHomePageFragment();
    }

    private void startHomePageFragment() {
        HomePageFragment homePageFragment =
                (HomePageFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (homePageFragment == null) {
            homePageFragment = HomePageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homePageFragment, R.id.contentFrame);
        }

        HomePagePresenter homePagePresenter = new HomePagePresenter(homePageFragment);
    }
}
