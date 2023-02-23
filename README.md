# Cordova Money Stories Plugin

MoneyStories SDK is a delightful component for show stories provided by Finshape communication with backend.

 The SDK is powered with [Finshape](https://finshape.com/products/money-stories/) industry-proven and world leading the digital banking and personalisation solutions that help you get where you want to be on your digitalisation journey – fast and without detours.

 ## Supported Platforms

- iOS
- Android


## Installation
- Run the following command:

```shell
    cordova plugin add https://github.com/pcamilojunior/cordova-moneystories-plugin.git
``` 

# MoneyStories Plugin

The `MoneyStories Plugin` Digital banking
made fast, easy and personalised.

## Methods

- __initializeSdk__: Method to initialise the SDK.

- __openStories__: Method to open your customazed stories with custom parameters.

## initializeSdk()

__Parameters__:

- __baseUrl__: The base URL should be provided by Finshare.

- __accessToken__: Access Token should be provided by Finshape.

- __languageCode__: Provide the language that you want to see your stories.


- __successCallback__: A callback that is passed when the service is started with success. _(Function)_

- __errorCallback__: A callback that executes if an error occurs retrieving the `MoneyStories Plugin` initializeSdk. _(Function)_

### Example

```javascript
var success = function (data) {
    console.log(data);
}

var fail = function (error) {
    console.log(error);
}

cordova.plugins.MoneyStoriesPlugin.initializeSdk(
    {
        baseUrl: "PASTE YOUR PASE URL HERE",
        languageCode: "en",
        accessToken: "PASTE YOUR ACCESS TOKEN HERE"
    },
    success, 
    fail
);