"use strict";
exports.__esModule = true;
var net = require("net");
var Config_1 = require("./utils/Config");
var CustomPackage_1 = require("./Framesets/CustomPackage");
var LocalServer = (function () {
    function LocalServer() {
        Config_1.SettingConfig.isDebug = false;
        Config_1.SettingConfig.init("SettingConfig.json");
        this.LocalServer = net.createServer({ allowHalfOpen: true });
        this.LocalServer.on('connection', this.ServerOnConnection);
        this.LocalServer.on('error', this.ServerOnError);
    }
    LocalServer.prototype.ServerStart = function () {
        console.log('>> LocalServer Start... %s %s', Config_1.SettingConfig.LocalServer_ListenHost, Config_1.SettingConfig.LocalServer_ListenPort);
        this.LocalServer.listen(Config_1.SettingConfig.LocalServer_ListenPort, Config_1.SettingConfig.LocalServer_ListenHost);
    };
    LocalServer.prototype.ServerStop = function () {
        this.LocalServer.end();
    };
    LocalServer.prototype.ServerOnConnection = function (conn) {
        conn.setKeepAlive(true);
        conn.setNoDelay(true);
        var mCustomPackage = new CustomPackage_1.CustomPackage();
        var mSocket = new net.Socket({ allowHalfOpen: true });
        mSocket.setKeepAlive(true);
        mSocket.setNoDelay(true);
        mSocket.connect(Config_1.SettingConfig.RemoteServer_ListenPort, Config_1.SettingConfig.RemoteServer_Address, function () {
            console.log('>> LocalServer CONNECTED TO RemoteServer');
        });
        conn.on('data', function (data) {
            var tmp = mCustomPackage.formatData(data);
            mSocket.write(tmp);
            console.log('>> LocalServer send data,length:' + data.length);
        });
        mSocket.on('data', function (data) {
            mCustomPackage.put(data);
        });
        mCustomPackage.onRealDataReceivedHandler = function (data) {
            console.log('>> LocalServer receive package data,length:' + data.length);
            conn.write(data);
        };
        mCustomPackage.onErrorHandler = function (err) {
            console.log('LocalServer mCustomPackage Error %s', err);
            mSocket.end();
            conn.end();
        };
        mSocket.on('error', function (err) {
            console.log('LocalServer mSocket error %s', err.message);
            mSocket.end();
            conn.end();
        });
        mSocket.on('end', function () {
            console.log('LocalServer mSocket end');
            mSocket.end();
            conn.end();
        });
        conn.on('end', function () {
            console.log('LocalServer conn end ');
            mSocket.end();
            conn.end();
        });
        conn.on('error', function (err) {
            console.log('LocalServer conn error %s', err.message);
            mSocket.end();
            conn.end();
        });
    };
    LocalServer.prototype.ServerOnError = function (err) {
        console.log('LocalServer Error %s', err.message);
    };
    return LocalServer;
}());
exports.LocalServer = LocalServer;
var mLocalServer = new LocalServer();
mLocalServer.ServerStart();
//# sourceMappingURL=LocalServer.js.map