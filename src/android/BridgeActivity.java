package com.outsystems.moneystories;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import hu.wup.moneystories.data.model.StoryLineBaseModel;
import hu.wup.moneystories.ui.main.MoneyStoriesActivity;
import hu.wup.moneystories.ui.storyBar.StoryBarView;
import io.cordova.hellocordova.R;
public class BridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bridgelayout);

        try {
            if (getIntent().getExtras() != null && getIntent().getExtras().get(StoryBarView.INTENT_SELECTED_ITEM) != null) {
                StoryLineBaseModel data = (StoryLineBaseModel) getIntent().getExtras().get(StoryBarView.INTENT_SELECTED_ITEM);
                Intent intentStory = new Intent(this, MoneyStoriesActivity.class);
                intentStory.putExtra(StoryBarView.INTENT_SELECTED_ITEM, data);
                this.startActivityForResult(intentStory, 2001);
            } else {
                finish();
            }
        } catch (Exception exception) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2001) {
            finish();
        }
    }
}
