package com.outsystems.moneystories.plugin;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import hu.wup.moneystories.MoneyStories;
import hu.wup.moneystories.data.model.PeriodTypeModel;
import hu.wup.moneystories.data.model.StoryLineBaseModel;
import hu.wup.moneystories.data.model.StoryLineModel;
import hu.wup.moneystories.di.AppContainer;
import hu.wup.moneystories.ui.base.RowViewModel;
import hu.wup.moneystories.ui.main.MoneyStoriesActivity;
import hu.wup.moneystories.ui.storyBar.StoryBarView;
import hu.wup.moneystories.ui.storyBar.StoryBarViewModel;


public class MoneyStoriesPlugin extends CordovaPlugin {

    private       CallbackContext callbackContext;
    private final String ACTION_INIT_SDK = "initializeSdk";
    private final String ACTION_REFRESH_TOKEN = "refreshToken";
    private final String ACTION_OPEN_STORIES = "openStories";

    private final String MORE_ACTION = "MORE";
    private String baseUrl;
    private String accessToken;
    private String languageCode;
    private String customerId;
    private MoneyStories moneyStories;

    private static int STORY_SCREEN_CODE_RESULT = 2003;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;

        if (action != null) {
            try {
                cordova.getThreadPool().execute(() -> {
                    if (ACTION_INIT_SDK.equals(action)) {
                        Log.v("MoneyStoriesPlugin", " >>> Action initMoneyStoriesSdk");
                        initMoneyStoriesSdk(args);
                    }
                    if (ACTION_OPEN_STORIES.equals(action)) {
                        Log.v("MoneyStoriesPlugin", " >>> Action openStories");
                        openStoriesScreen(args);
                    }
                    if (ACTION_REFRESH_TOKEN.equals(action)) {
                        Log.v("MoneyStoriesPlugin", " >>> Action refreshToken");
                        setRefreshToken(args);
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

                moneyStories = new MoneyStories.Builder()
                        .context(this.cordova.getContext())
                        .withBaseUrl(baseUrl)
                        .inDebugMode()
                        .withLanguageCode(languageCode)
                        .build();

                moneyStories.setAccessToken("Bearer " + accessToken);
                moneyStories.setCustomerId(customerId);

                MoneyStories.Companion.initialize(moneyStories);
                Log.v("MoneyStoriesPlugin", " >>> initMoneyStoriesSdk");
                this.returnInitStories();
            } else {
                this.callbackContext.error("Error to initialize the SDK arguments not found");
            }
        } catch (Exception ex) {
            Log.v("MoneyStoriesPlugin", " >>> Exception initMoneyStoriesSdk: "+ ex.getMessage());
            this.callbackContext.error("Error to initialize the SDK: " + ex.getMessage());
        }
    }

    private void returnInitStories() {
        StoryBarViewModel viewModel = initViewModel();
        Log.v("MoneyStoriesPlugin", " >>> ViewModel initialised: "+ viewModel.toString());
        Observer<List<RowViewModel<StoryLineBaseModel>>> result = items -> {

            if (items.isEmpty()) {
                callbackContext.error("No stories to display");
            } else {
                try {
                    List<StoryModel> stories = new ArrayList<>();

                    for (RowViewModel<StoryLineBaseModel> item : items) {
                        if (item.getItem() instanceof StoryLineModel) {

                            StoryModel model = new StoryModel();
                            model.period = ((StoryLineModel) item.getItem()).getPeriod().name();
                            model.read = ((StoryLineModel) item.getItem()).getRead();
                            model.startDate = ((StoryLineModel) item.getItem()).getStartDate().toString();

                            stories.add(model);
                        }
                    }
                    String resultJson = new Gson().toJson(stories);
                    Log.v("MoneyStoriesPlugin", " >>> Result Stories: "+ resultJson);
                    callbackContext.success(resultJson);
                } catch (Exception ex) {
                    Log.v("MoneyStoriesPlugin", " >>> Exception to get Stories: "+ ex.getMessage());
                    callbackContext.error("Error to retrieve the stories");
                }
            }
        };

        handler.post(() -> {
            viewModel.initStoryBar();
            viewModel.getStoryBarItems().observe(cordova.getActivity(), result);
        });
    }

    private static class StoryModel {
        public String startDate;
        public boolean read;
        public String period;
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
        if (object.has("customerId")) {
            customerId = (String) object.get("customerId");
        } else {
            callbackContext.error("Field customerId not present or invalid!");
        }
    }

    private void setRefreshToken(JSONArray args) {
        try {
            if (args != null && args.length() > 0) {
                String token = (String) args.get(0);
                Log.v("MoneyStoriesPlugin", " >>> setRefreshToken new token: "+ token);
                if (moneyStories != null) {
                    MoneyStories.Companion.getInstance().setAccessToken("Bearer " + token);
                    moneyStories.setAccessToken("Bearer " + token);
                    callbackContext.success();
                }
            }
        } catch(Exception ex) {
            Log.v("MoneyStoriesPlugin", " >>> Exception setRefreshToken new token: "+ ex.getMessage());
            this.callbackContext.error("Error to refresh token: "+ex.getMessage());
        }
    }

    private void openStoriesScreen(JSONArray args) {
        try {
            if (args.length() > 0) {
                JSONObject object = (JSONObject) args.get(0);

                if (!object.has("period") || !object.has("date")) {
                    this.callbackContext.error("Fields period or date is not present or invalid!");
                }

                if (object.has("period") && object.get("period").equals(MORE_ACTION)) {
                    StoryLineBaseModel baseLineToMoreOption = new StoryLineBaseModel();
                    triggerStoryAct(baseLineToMoreOption);
                } else {
                    String period = (String) object.get("period");
                    String date = (String) object.get("date");
                    prepareParamsToOpenStories(period, date);
                }
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
        Log.v("MoneyStoriesPlugin", " >>> Story line opened: "+data.toString());
        Intent intent = new Intent(this.cordova.getContext(), MoneyStoriesActivity.class);
        intent.putExtra(StoryBarView.INTENT_SELECTED_ITEM, data);
        this.cordova.startActivityForResult(this, intent, STORY_SCREEN_CODE_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == STORY_SCREEN_CODE_RESULT) {
            Log.v("MoneyStoriesPlugin", " >>> Story line closed");
            callbackContext.success();
        }
    }

    private StoryBarViewModel initViewModel() {
        Log.v("MoneyStoriesPlugin", " >>> AppContainer "+AppContainer.Companion.getInstance());
        return new StoryBarViewModel(AppContainer.Companion.getInstance().getUtilModule().getGson(),
                                     AppContainer.Companion.getInstance().getResourceModule().getUpdateResourcesUseCase(),
                                     AppContainer.Companion.getInstance().getResourceModule().getGetResourcesUseCase(),
                                     AppContainer.Companion.getInstance().getPreloadModule().getGetPreloadUseCase(),
                                     AppContainer.Companion.getInstance().getConfigModule().getGetConfigurationUseCase(),
                                     AppContainer.Companion.getInstance().getDeviceInfoModule().getGetDeviceInfoAnalyticsDataUseCase(),
                                     AppContainer.Companion.getInstance().getAnalyticsModule().getStoreAnalyticsDataUseCase());
    }
}
