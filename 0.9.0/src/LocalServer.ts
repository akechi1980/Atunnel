/**
 * 可用的请求转发工具
 * 可加密，可压缩
 * 双向流动
 */

import * as crypto from 'crypto';
import * as zlib from 'zlib';
import * as net from 'net';

import {SettingConfig} from './utils/Config';
import {CustomPackage} from './Framesets/CustomPackage';

export class LocalServer{
    LocalServer : any;

    constructor() {
        SettingConfig.isDebug = false;
        SettingConfig.init("SettingConfig.json");

        this.LocalServer = net.createServer({allowHalfOpen: true});
        //LocalServer
        this.LocalServer.on('connection', this.ServerOnConnection);
        this.LocalServer.on('error',this.ServerOnError);
    }
   

    ServerStart(){
        console.log('>> LocalServer Start... %s %s',SettingConfig.LocalServer_ListenHost,SettingConfig.LocalServer_ListenPort);
        this.LocalServer.listen(SettingConfig.LocalServer_ListenPort,SettingConfig.LocalServer_ListenHost);
    }

    ServerStop(){
        this.LocalServer.end();
    }

    ServerOnConnection(conn){
        conn.setKeepAlive(true);
        conn.setNoDelay(true);

        // setTimeout(() => {
        //     console.log('>> LocalServer CONNECTED : ' + conn.RemoteServer);
        // }, 1000);
        

        var mCustomPackage = new CustomPackage();

        var mSocket = new net.Socket({allowHalfOpen: true});
        mSocket.setKeepAlive(true);
        mSocket.setNoDelay(true);

        mSocket.connect(SettingConfig.RemoteServer_ListenPort,SettingConfig.RemoteServer_Address, function() {
            console.log('>> LocalServer CONNECTED TO RemoteServer');
        });

        conn.on('data', function(data) {
            //console.log('>> LocalServer send data,length:'+ data.length);
            var tmp = mCustomPackage.formatData(data);
            //console.log('>> LocalServer send data,length:'+ tmp.length);
            //conn.pause();
            mSocket.write(tmp);
            //conn.resume();
            console.log('>> LocalServer send data,length:'+ data.length);
        });

        mSocket.on('data', function(data) {
            //console.log('>> LocalServer received data,length:'+ data.length);
            //mSocket.pause();
            mCustomPackage.put(data);
            //mSocket.resume();
        });   
        
        //当客户端收到完整的数据包时
        mCustomPackage.onRealDataReceivedHandler = function(data){
            console.log('>> LocalServer receive package data,length:'+ data.length);
            conn.write(data);
        };

        mCustomPackage.onErrorHandler = function(err){
            console.log('LocalServer mCustomPackage Error %s',err);
            mSocket.end();
            conn.end();
        };

        mSocket.on('error', function(err) {
            console.log('LocalServer mSocket error %s',err.message);
            mSocket.end();
            conn.end();
        });

        mSocket.on('end', function() {
            console.log('LocalServer mSocket end');
            mSocket.end();
            conn.end();
        });

        conn.on('end', function() {
            console.log('LocalServer conn end ');
            mSocket.end();
            conn.end();
        });

        conn.on('error', function(err) {
            console.log('LocalServer conn error %s',err.message);
            mSocket.end();
            conn.end();
        });
    }

    ServerOnError(err){
        console.log('LocalServer Error %s',err.message);
    }

}


let mLocalServer = new LocalServer();
mLocalServer.ServerStart();
