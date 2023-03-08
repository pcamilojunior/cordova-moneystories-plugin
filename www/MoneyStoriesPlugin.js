var exec = require('cordova/exec');

exports.initializeSdk = function(params, successCallback, errorCallback) {
    exec(successCallback, errorCallback,  'MoneyStoriesPlugin', 'initializeSdk', [params]);
};

exports.refreshToken = function(token, successCallback, errorCallback) {
    exec(successCallback, errorCallback,  'MoneyStoriesPlugin', 'refreshToken', [token]);
};

exports.openStories = function(params, successCallback, errorCallback) {
    exec(successCallback, errorCallback,  'MoneyStoriesPlugin', 'openStories', [params]);
};