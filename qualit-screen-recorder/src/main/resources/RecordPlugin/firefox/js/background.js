"use strict";

var plugin = {};
var sendResponseCallBack;
var screenshot = {
    path: 'filesystem:chrome-extension://' + chrome.i18n.getMessage("@@extension_id") + '/temporary/',
    generated: false,
    newwholepage: true,
    enableNpapi: false,
    imgData: null,
    button_video: null,
    videoRecorder: videoRecorder,

    changeVideoButton: function () {
        if (localStorage.showContentMenu == 'true') {
            if (screenshot.videoRecorder.getStatus()) {
                chrome.contextMenus.update(screenshot.button_video, {
                    title: chrome.i18n.getMessage("optionsLabelStopVideo"),
                    onclick: function () {
                        videoRecorder.stopRecord()
                    }
                })
            } else {
                chrome.contextMenus.update(screenshot.button_video, {
                    title: chrome.i18n.getMessage("btnCaptureVideo"),
                    onclick: function () {
                        videoRecorder.capture({
                            type: 'tab',
                            countdown: localStorage.videoCountdown
                        });
                    }
                })
            }
        }
    },

    fragmentsData: [],

    init: function () {
        localStorage.showContentMenu = false;
    }
};

function getTimeStamp() {
    var y, m, d, h, M, s, mm, timestamp;
    var time = new Date();
    y = time.getFullYear();
    m = time.getMonth() + 1;
    d = time.getDate();
    h = time.getHours();
    M = time.getMinutes();
    s = time.getSeconds();
    mm = time.getMilliseconds();
    timestamp = Date.now();
    if (m < 10) m = '0' + m;
    if (d < 10) d = '0' + d;
    if (h < 10) h = '0' + h;
    if (M < 10) M = '0' + M;
    if (s < 10) s = '0' + s;
    if (mm < 10) mm = '00' + mm;
    else if (mm < 100) mm = '0' + mm;
    return y + '.' + m + '.' + d + ' ' + h + ':' + M + ':' + s + ' ' + mm + ' ' + timestamp;
}


chrome.downloads.onChanged.addListener(function (downloadDelta) {
    if (downloadDelta.filename != undefined) {
        console.log('filename is ' + downloadDelta.filename.current);
        //localStorage.fileNamePath = downloadDelta.filename.current;
        sendResponseCallBack({
            path: downloadDelta.filename.current
        });
    }
});
/*
 var editorExtensionId = "cffifjgabjalihmjiojbennijblkhcef";

// Make a simple request:
chrome.runtime.sendMessage(editorExtensionId, {command: 'stop_video', filename: 'фывафыв фыва фывафы ыфва'},
  function(response) {
	console.log(response)  ;
  });
*
* */
chrome.runtime.onMessageExternal.addListener(
    function(request, sender, sendResponse) {
        switch (request.command) {
            case 'start_tab_video' :
                screenshot.videoRecorder.capture({
                    type: 'tab',
                    countdown: '0'
                });
                break;
            case 'start_desktop_video' :
                screenshot.videoRecorder.capture({
                    type: 'desktop',
                    countdown: localStorage.videoCountdown
                });
                break;
            case 'stop_video' :
                sendResponseCallBack = sendResponse;
                screenshot.videoRecorder.stopRecord();
                return true;
                break;
            case 'pause_video' :
                if (screenshot.videoRecorder.getState() === 'recording') {
                    screenshot.videoRecorder.pauseRecord();
                }
                break;
        }
    });

chrome.extension.onMessage.addListener(function (request, sender, sendResponse) {
    if (request.operation == 'status_video_change') {
        switch (request.status) {
            case 'play' :
                if (screenshot.videoRecorder.getState() !== 'recording') {
                    screenshot.videoRecorder.pauseRecord();
                }
                break;
            case 'pause' :
                if (screenshot.videoRecorder.getState() === 'recording') {
                    screenshot.videoRecorder.pauseRecord();
                }
                break;
            case 'stop' :
                screenshot.videoRecorder.stopRecord();
                break;
        }
    }
    if (request.operation == 'video_deawing_tools') {
        localStorage.deawingTools = request.value;
    }

});

//these variables are responsible for the operation of each function in separate tabs
var thisCrop, thisFragment, thisScrollCrop;

chrome.extension.onRequest.addListener(function (request, sender, sendResponse) {

    if (request.operation == 'hotkeys') {
        sendResponse({hotkeys: localStorage.hotkeys});
    } else if (request.operation == 'hotkey') {
        console.log(request.name);
        if (request.name == 'tab_video') {
            screenshot.videoRecorder.capture({
                type: 'tab',
                countdown: localStorage.videoCountdown
            });
        }
        if (request.name == 'desktop_video') {
            screenshot.videoRecorder.capture({
                type: 'desktop',
                countdown: localStorage.videoCountdown
            });
        }
        if (request.name == 'stop_video') {
            screenshot.videoRecorder.stopRecord()
        }

    }
});

chrome.commands.onCommand.addListener(function (command) {
    if (command === 'start_tab_video') {
        screenshot.videoRecorder.capture({
            type: 'tab',
            countdown: '0'
        });
    }
    if (command === 'start_desktop_video') {
        screenshot.videoRecorder.capture({
            type: 'desktop',
            countdown: localStorage.videoCountdown
        });
    }
    if (command === 'stop_video') {
        screenshot.videoRecorder.stopRecord()
    }
    if (command === 'pause_video') {
        if (screenshot.videoRecorder.getState() === 'recording') {
            screenshot.videoRecorder.pauseRecord();
        }
    }
});

if (localStorage.hotkeys) {
    var hotkeys = JSON.parse(localStorage.hotkeys);
    localStorage.hotkeys = JSON.stringify({
        tab_video: hotkeys.tab_video || 85,
        desktop_video: hotkeys.desktop_video || 56,
        stop_video: hotkeys.stop_video || 57,
        visible: hotkeys.visible || 49,
        fragment: hotkeys.fragment || 54,
        selected: hotkeys.selected || 50,
        scroll: hotkeys.scroll || 51,
        entire: hotkeys.entire || 52,
        window: hotkeys.window || 53
    });
} else {
    localStorage.hotkeys = JSON.stringify({
        tab_video: '85',
        desktop_video: '56',
        stop_video: '57',
        visible: '49',
        fragment: '54',
        selected: '50',
        scroll: '51',
        entire: '52',
        window: '53'
    });
}

if (!localStorage.hotkeysSendNS) {
    localStorage.hotkeysSendNS = JSON.stringify({
        key: '13',
        title: 'Enter'
    });
}

localStorage.micSound = localStorage.micSound || 'true';
localStorage.tabSound = localStorage.tabSound || 'false';
localStorage.videoReEncoding = localStorage.videoReEncoding || 'true';
localStorage.micPopup = localStorage.micPopup || 'false';
localStorage.cursorAnimate = localStorage.cursorAnimate || 'false';
localStorage.deawingTools = localStorage.deawingTools || 'false';
localStorage.recordType = localStorage.recordType || 'tab';
localStorage.videoSize = localStorage.videoSize || 'auto';
localStorage.videoBitrate = localStorage.videoBitrate || '4500000';
localStorage.audioBitrate = localStorage.audioBitrate || '96000';
localStorage.videoFps = localStorage.videoFps || '24';
localStorage.deleteDrawing = localStorage.deleteDrawing || '6';
localStorage.selectMic = localStorage.selectMic || 'default';
localStorage.videoCountdown = localStorage.videoCountdown || '3';
localStorage.format = localStorage.format || 'png';
localStorage.imageQuality = localStorage.imageQuality || '92';
localStorage.enableEdit = localStorage.enableEdit || 'edit';
localStorage.quickCapture = localStorage.quickCapture || 'false';
localStorage.enableSaveAs = localStorage.enableSaveAs || 'true';
localStorage.saveCropPosition = localStorage.saveCropPosition || 'false';
localStorage.showContentMenu = localStorage.showContentMenu || 'true';
localStorage.keepOriginalResolution = localStorage.keepOriginalResolution || 'true';
localStorage.hideFixedElements = localStorage.hideFixedElements || 'true';
localStorage.shareOnGoogle = localStorage.shareOnGoogle || 'false';
localStorage.cropPosition = localStorage.cropPosition || JSON.stringify({
    "x": 50,
    "y": 50,
    "x2": 450,
    "y2": 250,
    "w": 400,
    "h": 200
});
localStorage.cropScrollPosition = localStorage.cropScrollPosition || JSON.stringify({
    "x": 50,
    "y": 50,
    "x2": 450,
    "y2": 250,
    "w": 400,
    "h": 200
});

window.onload = function () {
    screenshot.init();
};