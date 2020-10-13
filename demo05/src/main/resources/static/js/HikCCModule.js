import './ClientContainer.js';

const PORT = 9527;
const PORT_STEP = 100;

const AUTO_ZINDEX_ELEMENT = 1;
const MUTABLE_ELEMENT = 2;

class HikCCSession {
	ccInstance = null;
	sessionId = "";

	_promise = undefined;

	constructor(instance) {
		this.ccInstance = instance;
	}

	open() {
		if (this.sessionId.length) {
			return this.sessionId;
		}
		return this._open();
	}

	close() {
		return this.ccInstance.close();
	}

	//销毁该会话，调用此方法后，不能再调用其它方法
	destroy() {
		if (this.ccInstance) {
			this.ccInstance.delete();
			this.ccInstance = null;
			this.sessionId = '';
			this._promise = undefined;
		}
	}

	initModule(module) {
		return this._initModule(module);
	}

	callModuleMethod(module, method, param) {
		return this._callModuleMethod(module, method, param);
	}

	uninitModule(module) {
		return this._uninitModule(module);
	}

	//以下是内部私有方法，外部不要调用
	_open() {
		if (this._promise === undefined) {
			this._promise = new Promise((resolve, reject) => {
				let ret = this.ccInstance.connect(
					ccWrapper.ccWsUri, {
					onopen: (session) => {
						console.log('CONNECTED session:' + session);
						this.sessionId = session;
						resolve(session);
					},
					onclose: (obj) => {
						console.log('cc_onclose :' + JSON.stringify(obj));
						this.sessionId = '';
						//this.ccInstance.delete();
						//this.ccInstance = null;
						reject('cc onclose');
						this._promise = undefined;
					},
					onerror: (session) => {
						console.log("cc_onerror session:" + session);
						this.sessionId = '';
						//this.ccInstance.delete();
						//this.ccInstance = null;
						reject('cc onerror');
						this._promise = undefined;
					},
					onnotify: (method, params, rsp) => {
						console.log('client request or notify:' + JSON.stringify(params));
						this._onRequestOrNotify(method, params, rsp);
					}
				}
				);

				if (0 == ret) {
					console.log("ccInstance connect failed");
					reject("ccInstance connect failed");
				}
			});
		}
		return this._promise;
	}

	_initModule(module) {
		return new Promise((resolve, reject) => {
			let ret = this.ccInstance.initModule(module, (success, res) => {
				if (success) {
					resolve(res);
				} else {
					reject(res);
				}
			});

			if (ret < 0) {
				console.log("ccInstance initModule failed");
				reject("ccInstance initModule failed");
			}
		});
	}

	_callModuleMethod(module, method, param) {
		return new Promise((resolve, reject) => {
			let ret = this.ccInstance.callModuleMethod(module, method, param, (success, res) => {
				if (success) {
					resolve(res);
				} else {
					reject(res);
				}
			});

			if (ret < 0) {
				console.log("ccInstance callModuleMethod failed");
				reject("ccInstance callModuleMethod failed");
			}
		});
	}

	_uninitModule(module) {
		return new Promise((resolve, reject) => {
			let ret = this.ccInstance.uninitModule(module, (success, res) => {
				if (success) {
					resolve(res);
				} else {
					reject(res);
				}
			});

			if (ret < 0) {
				console.log("ccInstance uninitModule failed");
				reject("ccInstance uninitModule failed");
			}
		});
	}

	async _onRequestOrNotify(method, params, rsp) {
		try {
			let strArray = method.split('.');
			if (strArray.length === 0) {
				throw {
					code: -32601,
					message: 'method is not found'
				};
			}

			let temp = window;
			for (let i = 0, len = strArray.length; i < len; i++) {
				temp = temp[strArray[i]];
			}

			if (!temp) {
				throw {
					code: -32601,
					message: 'method is not found'
				};
			}

			let res = await temp(params);
			if (rsp) {
				rsp.result = res;
			}
		} catch (err) {
			if (rsp) {
				rsp.error = err;
			}
		}

		if (rsp) {
			this.ccInstance.replyToCC(rsp);
		}
	}
}

class HikCCWrapper {
	ccWsUri = "ws://localhost:9527";

	_ccPromise = undefined;

	ccPull() {
		window.location = "HikCC://port=9527";
	}

	//tryCount连接重试次数
	async ccIsOk(tryCount = 3) {
		//首先等待wasm加载完毕，
		await HikCCModule.initPromise();

		let port = PORT;

		for (; ;) {
			try {
				await this._ccInit(port);
				break;
			} catch (err) {
				if (--tryCount) {
					port += PORT_STEP;
				} else {
					throw new Error("连接cc失败，检查cc是否安装或启动." + err);
				}
			}
		}
	}

	ccClear() {
		HikCCModule.CCWndMgr.uninit();
		this._ccPromise = undefined;
	}

	async ccBindWindow(winId, elementId, type = 0) {
		await this.ccIsOk();
		return await this._ccBindWindow(winId, elementId, type);
	}

	async ccUnBindWindow(winId) {
		await this.ccIsOk();
		let ret = HikCCModule.CCWndMgr.unbindWindow(winId);
		if (ret) {
			return ret;
		} else {
			throw ret;
		}
	}

	async ccUpdateWindow(winId) {
		await this.ccIsOk();
		let ret = HikCCModule.CCWndMgr.updateWindow(winId);
		if (ret) {
			return ret;
		} else {
			throw ret;
		}
	}

	async ccUpdateAllWindow() {
		await this.ccIsOk();
		HikCCModule.CCWndMgr.updateAllWindow();
		return 'updateAllWindow success';
	}

	async ccCreateSession() {
		await this.ccIsOk();
		let session = new HikCCSession(new HikCCModule.ClientContainer);
		await session.open();
		return session;
	}

	//以下是内部私有方法，外部不要调用
	_ccInit(port) {
		if (this._ccPromise === undefined) {
			this._ccPromise = new Promise((resolve, reject) => {
				let ret = HikCCModule.CCWndMgr.init('ws://localhost:' + port, (success) => {
					console.log("cc_wndMgr_inited_result :" + success);
					if (success) {
						this.ccWsUri = 'ws://localhost:' + port;
						resolve("cc init successful!");
					} else {
						HikCCModule.CCWndMgr.uninit();
						reject("cc init failed!");
					}
				});

				if (0 == ret) {
					console.log("call HikCCModule.CCWndMgr.init failed");
					reject("call HikCCModule.CCWndMgr.init failed!");
				}
			});

			this._ccPromise.catch(() => {
				this._ccPromise = undefined;
			})
		}
		return this._ccPromise;
	}

	_ccBindWindow(winId, elementId, type) {
		return new Promise((resolve, reject) => {
			let ret = HikCCModule.CCWndMgr.bindWindow(winId, elementId, (success, res) => {
				if (success) {
					resolve(res);
				} else {
					reject(res);
				}
			}, type);

			if (0 == ret) {
				console.log("call HikCCModule.CCWndMgr.bindWindow failed");
				reject("call HikCCModule.CCWndMgr.bindWindow failed!");
			}
		});
	}
}

var ccWrapper = new HikCCWrapper();

export default ccWrapper;
export {
	ccWrapper,
	AUTO_ZINDEX_ELEMENT,
	MUTABLE_ELEMENT
};
