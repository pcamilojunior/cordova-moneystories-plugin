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

- __initializeSdk__: Method to initialise the SDK. This function returns a Array of Stories

- __openStories__: Method to open your customazed stories with custom parameters.

## initializeSdk()

__Parameters__:

- __baseUrl__: The base URL should be provided by Finshare.

- __accessToken__: Access Token should be provided by Finshape.

- __languageCode__: Provide the language that you want to see in your stories.

- __customerId__: Provide your customerId.

- __successCallback__: A callback that is passed when the service is started with success. _(Function)_

- __errorCallback__: A callback that executes if an error occurs retrieving the `MoneyStories Plugin` initializeSdk. _(Function)_

### Example - initializeSdk()

```javascript
var success = function (data) {
    console.log(data);
}

var fail = function (error) {
    console.log(error);
}

cordova.plugins.MoneyStoriesPlugin.initializeSdk(
    {
        baseUrl: "PASTE YOUR BASE URL HERE",
        languageCode: "en",
        accessToken: "PASTE YOUR ACCESS TOKEN HERE",
        customerId: "XXXXX"
    },
    success, 
    fail
);
``` 


## openStories()

__Parameters__:

- __jsonObject__: The JSON object which contains the fields necessary to open the Story View

- __successCallback__: A callback that is passed when the service is started with success. _(Function)_

- __errorCallback__: A callback that executes if an error occurs retrieving the `MoneyStories Plugin` openStories. _(Function)_


### Example - openStories()

```javascript
var success = function (data) {
    console.log(data);
}

var fail = function (error) {
    console.log(error);
}

cordova.plugins.MoneyStoriesPlugin.openStories(
    {
        period: "DAILY",
        date: "2022-03-22"
    },
    success, 
    fail
);
```

```javascript
Observations:

    For the openStories action in the period field, the available values are:
    - DAILY
    - WEEKLY
    - MONTHLY

    - Date format should be: YYYY-MM-DD, Example: 2023-01-01
````

### Story View screenshot

<p align="left">
  <img src="https://github.com/pcamilojunior/cordova-moneystories-plugin/blob/master/screenshot.PNG" width="350" title="Screenshot Stories">
</p>


#### Contributors
- OutSystems - Mobility Experts
    - Paulo Cesar Camilo, <paulo.camilo.junior@outsystems.com>

#### Document author
- Paulo Cesar Camilo, <paulo.camilo.junior@outsystems.com>

###Copyright OutSystems, 2023

---

LICENSE
=======


[The MIT License (MIT)](http://www.opensource.org/licenses/mit-license.html)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.