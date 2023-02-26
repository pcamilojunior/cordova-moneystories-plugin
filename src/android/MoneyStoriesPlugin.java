package com.outsystems.moneystories.plugin;


import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import com.outsystems.moneystories.BridgeActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

import hu.wup.moneystories.MoneyStories;
import hu.wup.moneystories.data.model.PeriodTypeModel;
import hu.wup.moneystories.data.model.StoryLineBaseModel;
import hu.wup.moneystories.data.model.StoryLineModel;
import hu.wup.moneystories.ui.storyBar.StoryBarView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MoneyStoriesPlugin extends CordovaPlugin {

    private CallbackContext callbackContext;
    private final String ACTION_INIT_SDK = "initializeSdk";
    private final String ACTION_OPEN_STORIES = "openStories";

    private String baseUrl;
    private String accessToken;
    private String languageCode;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;

        if (action != null) {
            try {
                cordova.getThreadPool().execute(() -> {
                    if (ACTION_INIT_SDK.equals(action)) {
                        initMoneyStoriesSdk(args);
                    }
                    if (ACTION_OPEN_STORIES.equals(action)) {
                        openStoriesScreen(args);
                    }
                });
            } catch (Exception ex) {
                this.callbackContext.error(ex.getMessage());
                return false;
            }
        } else {
            this.callbackContext.error("Action invalid!");
            return false;
        }

        return true;
    }

    private void initMoneyStoriesSdk(JSONArray args) {
        try {
            if (args != null) {
                setupArgumentsToInitSDK(args);

                MoneyStories moneyStories = new MoneyStories.Builder()
                        .context(this.cordova.getContext())
                        .withBaseUrl(baseUrl)
                        .inDebugMode()
                        .withLanguageCode(languageCode)
                        .build();

                moneyStories.setAccessToken("Bearer " + accessToken);
                MoneyStories.Companion.initialize(moneyStories);
                this.callbackContext.success();
            } else {
                this.callbackContext.error("Error to initialize the SDK arguments not found");
            }
        } catch (Exception ex) {
            this.callbackContext.error("Error to initialize the SDK: "+ex.getMessage());
        }
    }

    private void setupArgumentsToInitSDK(JSONArray args) throws JSONException {
        JSONObject object = (JSONObject) args.get(0);

        if (object.has("baseUrl")) {
            baseUrl = (String) object.get("baseUrl");
        } else {
            callbackContext.error("Field baseUrl not present or invalid!");
        }
        if (object.has("languageCode")) {
            languageCode = (String) object.get("languageCode");
        } else {
            callbackContext.error("Field languageCode not present or invalid!");
        }
        if (object.has("accessToken")) {
            accessToken = (String) object.get("accessToken");
        } else {
            callbackContext.error("Field accessToken not present or invalid!");

        }
    }

    private void openStoriesScreen(JSONArray args) {
        try {
            if (args.length() > 0) {
                JSONObject object = (JSONObject) args.get(0);
                if (!object.has("period") || !object.has("date")) {
                    this.callbackContext.error("Fields period or date is not present or invalid!");
                }

                String period = (String) object.get("period");
                String date = (String) object.get("date");
                prepareParamsToOpenStories(period, date);
            } else {
                callbackContext.error("Fields to open the stories not present or invalid!");
            }
        } catch (Exception ex) {
            callbackContext.error("Fields to open the stories not present or invalid!");
        }
    }

    private void prepareParamsToOpenStories(String periodType, String date) {
        try {

            PeriodTypeModel periodTypeModel = PeriodTypeModel.valueOf(periodType);
            LocalDate dateParsed = LocalDate.parse(date);

            triggerStoryAct(new StoryLineModel(dateParsed, periodTypeModel, true));

        } catch (Exception exception) {
            this.callbackContext.error("Fields period or date are not valid!");
        }
    }

    private void triggerStoryAct(StoryLineBaseModel data) {
        Intent intent = new Intent(this.cordova.getContext(), BridgeActivity.class);
        intent.putExtra(StoryBarView.INTENT_SELECTED_ITEM, data);
        this.cordova.getContext().startActivity(intent);
    }
}