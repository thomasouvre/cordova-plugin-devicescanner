var exec = cordova.require("cordova/exec");

function DeviceScanner() { }

DeviceScanner.prototype.scan = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'DeviceScanner', 'scan');
}

DeviceScanner.prototype.clear = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'DeviceScanner', 'clear');
}

DeviceScanner.prototype.getInfos = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'DeviceScanner', 'getInfos');
}

DeviceScanner.prototype.setInfos = function (data, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'DeviceScanner', 'setInfos', [data]);
}

DeviceScanner.prototype.listenState = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'DeviceScanner', 'listenState');
}

var deviceScanner = new DeviceScanner();
module.exports = deviceScanner;