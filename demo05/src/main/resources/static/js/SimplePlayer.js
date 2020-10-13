import ccWrapper from './HikCCModule.js';
export { AUTO_ZINDEX_ELEMENT, MUTABLE_ELEMENT } from './HikCCModule.js';

class WebPlayer {
    hikvideoplay = undefined;
    playHandle = undefined;
    winId = undefined;
	elementId = undefined;

    constructor(vp, ph, wid, eid) {
        this.hikvideoplay = vp;
        this.playHandle = ph;
        this.winId = wid;
		this.elementId = eid;
    }

    startPlayReal(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "startPlayReal", params);
    }

    stopPlayReal(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "stopPlayReal", params);
    }

    startPlayBack(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "startPlayBack", params);
    }

    stopPlayBack(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "stopPlayBack", params);
    }

    snapShot(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "snapShot", params);
    }

    sound(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "sound", params);
    }

    digitalZoom(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "digitalZoom", params);
    }

    pause(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "pause", params);
    }

    frameNext(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "frameNext", params);
    }

    framePre(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "framePre", params);
    }

    timeSelect(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "timeSelect", params);
    }

    downloadSelect(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "downloadSelect", params);
    }

    PTZControl(params) {
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "PTZControl", params);
    }

    // destroy() {
    //     return ccWrapper.ccUnBindWindow(this.winId);
    // }
    showErrorInfo(params){
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "showErrorInfo", params);
    }
    clear(params){
        params.playHandle = this.playHandle;
        return this.hikvideoplay.session.callModuleMethod("CCvideoplay", "clear", params);
    }
}

class WebPlayerMgr {
    session = undefined;
    _promise = undefined;
    _playerPromiseMap = new Map();

    init() {
        if (this._promise === undefined) {
            this._promise = ccWrapper.ccCreateSession();
            this._promise.catch(() => {
                this._promise = undefined;
            });
        }
        return this._promise;
    }

    async preStart() {
        if (this.session === undefined) {
            this.session = await this.init();
        }
        return await this.session.initModule("CCvideoplay");
    }

    getPlayer(elementId, params, type = 0){
        if(this._playerPromiseMap.get(elementId) === undefined){
            this._playerPromiseMap.set(elementId, this.createPlayer(elementId, params, type));
        }
        return this._playerPromiseMap.get(elementId);
    }

    async createPlayer(elementId, params, type = 0) {
        if (this.session === undefined) {
            this.session = await this.init();
        }
		
		params.sessionId = this.session.sessionId;
        let res = await this.session.callModuleMethod("CCvideoplay", "createPlayer", params);

        if (!res.hasOwnProperty('winId')) {
            throw 'createPlayer failed!'
        }

        let bindRes = await ccWrapper.ccBindWindow(res.winId, elementId, type);

        console.log("bindResult success,winId:" + bindRes);

        let showRes = await this.session.callModuleMethod("CCvideoplay", "showPlayWnd", {
            playHandle: res.playHandle
        });

        console.log("CCvideoplay showPlayWnd reslut:" + JSON.stringify(showRes));

        return new WebPlayer(this, res.playHandle, res.winId, elementId);
    }
    
    destroy(player) {
        this._playerPromiseMap.delete(player.elementId);
        return ccWrapper.ccUnBindWindow(player.winId);
    }

    async commonConfig(params){
        if (this.session === undefined) {
            this.session = await this.init();
        }
        params.sessionId = this.session.sessionId;
        this.session.callModuleMethod("CCvideoplay", "commonConfig", params);
    }
}

var playerFactory = new WebPlayerMgr();

//以下是测试代码
//直接导出到window，供index.html直接调用测试
window['playerFactory'] = playerFactory;
window['ccWrapper'] = ccWrapper;

export default playerFactory;
export { playerFactory, ccWrapper };
