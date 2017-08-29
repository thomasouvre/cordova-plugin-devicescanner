var exec = cordova.require("cordova/exec");

function DeviceScanner() { }

DeviceScanner.prototype.scan = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'DeviceScanner', 'scan');
}

var deviceScanner = new DeviceScanner();
module.exports = deviceScanner;
