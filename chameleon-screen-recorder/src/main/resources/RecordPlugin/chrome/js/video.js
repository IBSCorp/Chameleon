'use strict';

var isLog = true;
var MediaStream = window.MediaStream;

if (typeof MediaStream === 'undefined' && typeof webkitMediaStream !== 'undefined') {
    MediaStream = webkitMediaStream;
}

/*global MediaStream:true */
if (typeof MediaStream !== 'undefined' && !('stop' in MediaStream.prototype)) {
    MediaStream.prototype.stop = function () {
        this.getAudioTracks().forEach(function (track) {
            track.stop();
        });

        this.getVideoTracks().forEach(function (track) {
            track.stop();
        });
    };
}

iconService.setDefault();

var videoRecorder = (function () {
    var streamVideo = null;
    var $tabCursor;
    var typeCapture, videoSize, audioBitrate, videoBitrate, videoFps, audioPlayer, context;
    var countdown = 0;
    var timer = null;
    var activeTab = null;
    var recorder = null;
    var isRecording = false;
    var isError = false;
    var timeStart = null;
    var timePause = null;

    function onMediaAccess(access) {
        if (access) {
            streamVideo.active && preRecord(streamVideo)
        } else {
            stopStream();
        }
    }

    function failure_handler(error) {
        console.log(error);
    }

    function startRecord(videoStream, audioStream) {
        if (isLog) console.log('startRecord', arguments);
        var recorder_option = {
            autoWriteToDisk: true,
            type: 'video',
            disableLogs: false,
            mimeType: 'video/webm\;codecs=vp8',
            audioBitsPerSecond: audioBitrate,
            videoBitsPerSecond: videoBitrate
        };

        recorder = RecordRTC(videoStream, recorder_option);
        recorder.startRecording();

        chrome.tabs.query({active: true}, function (tabs) {
            chrome.tabs.sendMessage(tabs[0].id, {operation: 'status_video', status: getStatus(), state: getState()});
        });

        iconService.setRec();
        screenshot.changeVideoButton();
        timeStart = Date.now();
    }

    function preRecord(stream) {
        if (chrome.runtime.lastError) {
            if (/activeTab/.test(chrome.runtime.lastError.message)) {
                isRecording = false;
                alert(chrome.i18n.getMessage('notificationErrorActiveTab'));
            }
            console.error(chrome.runtime.lastError.message);
        } else {
            streamVideo = stream;

            streamVideo.onended = function () {
                streamVideo.onended = function () {
                };

                console.log('stream.onended');
                stopRecord()
            };

            streamVideo.getVideoTracks()[0].onended = function () {
                if (streamVideo && streamVideo.onended) {
                    streamVideo.onended();
                }
            };
            startRecord(streamVideo);

        }
    }

    function countdownRun(cb) {
        (function () {
            function time() {
                if (countdown > 0) {
                    iconService.showBadge(countdown);
                    countdown--;
                    timer = setTimeout(time, 1000);
                } else {
                    iconService.setDefault();
                    timer = null;
                    cb && cb();
                }
            }

            time();
        })()
    }

    function captureTab() {
        if (isLog) console.log('captureTab', arguments);
        chrome.tabs.query({active: true, lastFocusedWindow: true}, function (tabs) {

                activeTab = tabs[0];
                var constraints = {
                    audio: false,
                    video: true,
                    videoConstraints: {
                        mandatory: {
                            chromeMediaSource: 'tab',
                            maxFrameRate: videoFps,
                            maxWidth: typeof videoSize !== 'object' ? activeTab.width : videoSize.width,
                            maxHeight: typeof videoSize !== 'object' ? activeTab.height : videoSize.height
                        }
                    }
                };
                localStorage.currentVideoWidth = constraints.videoConstraints.mandatory.maxWidth;
                localStorage.currentVideoHeight = constraints.videoConstraints.mandatory.maxHeight;

                chrome.tabCapture.capture(constraints, preRecord);

        });
    }

    function captureDesktop() {
        if (isLog) console.log('captureDesktop', arguments);
        chrome.desktopCapture.chooseDesktopMedia(['screen', 'window'], function (streamId) {
            if (!streamId) {
                isRecording = false;
            } else {
                var constraints = {
                    video: {
                        mandatory: {
                            chromeMediaSource: "desktop",
                            chromeMediaSourceId: streamId,
                            maxFrameRate: videoFps,
                            maxWidth: typeof videoSize !== 'object' ? window.screen.width : videoSize.width,
                            maxHeight: typeof videoSize !== 'object' ? window.screen.height : videoSize.height
                        }
                    }
                };
                localStorage.currentVideoWidth = constraints.video.mandatory.maxWidth;
                localStorage.currentVideoHeight = constraints.video.mandatory.maxHeight;
                countdownRun(function () {
                    window.navigator.getUserMedia(constraints, preRecord, failure_handler);
                })
            }

        });

    }

    function capture(param) {
        if (isLog) console.log('capture', arguments);
        if (isRecording) return;
        isRecording = true;
        countdown = param.countdown;
        videoSize = localStorage.videoSize === 'auto';
        videoBitrate = +localStorage.videoBitrate;
        videoFps = +localStorage.videoFps;
        switch (localStorage.videoSize) {
            case '4k':
                videoSize = {
                    width: 3840,
                    height: 2160
                };
                break;
            case 'full-hd':
                videoSize = {
                    width: 1920,
                    height: 1080
                };
                break;
            case 'hd':
                videoSize = {
                    width: 1280,
                    height: 720
                };
                break;
        }

        if (param.type === 'tab') {
            if (param.countdown > 0 && !param.not_timer) {
                isRecording = false;
                /*chrome.tabs.query({active: true, lastFocusedWindow: true}, function (tabs) {
                    if (!tabs.length || /^chrome/.test(tabs[0].url)) {
                        alert(chrome.i18n.getMessage('notificationErrorChromeTab'));
                    } else {
                       // timerContent.set(param.countdown, 'tab');
                    }
                });*/
            } else {
                typeCapture = 'tab';
                captureTab()
            }
        } else {
            typeCapture = 'desktop';
            captureDesktop();
        }
    }

    function stopStream(sendResponse) {
        if (isLog) console.log('stopStream', streamVideo, recorder);
        if (streamVideo.active && recorder.state !== 'recording') {
            timePause = null;
            recorder.resumeRecording();
            iconService.setRec();
        }
        window.setTimeout(function () {
            try {
                streamVideo.stop();
                stopRecord();
            } catch (e) {
                stopRecord();
            }
        }, 1500);

    }

    var cb = function (videoUrl, videoBlob) {

    };

    var init =function (cb) {
        function errorHandler(e) {
            console.error(e);
        }

        var xhr = new XMLHttpRequest();
        xhr.responseType = 'blob';
        xhr.onload = function (e) {
            if (xhr.status == 200) {
                var videoBlob = new Blob([xhr.response], {type: 'video/webm'});
                window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;
                window.requestFileSystem(window.PERSISTENT, videoBlob.size, function (fs) {
                    var truncated = false;
                    fs.root.getFile('video.webm', {create: true}, function (fileEntry) {
                        fileEntry.createWriter(function (writer) {
                            writer.onwriteend = function (e) {
                                if (!truncated) {
                                    truncated = true;
                                    this.truncate(this.position);
                                    return;
                                }
                                if (localStorage.videoReEncoding !== 'false') {
                                    //nacl_module.set_nacl(cb);
                                } else {
                                    var file_path = 'filesystem:chrome-extension://' + chrome.i18n.getMessage("@@extension_id") + '/persistent/video.webm';
                                    cb && cb(file_path, videoBlob);
                                }
                            };

                            writer.onerror = errorHandler;
                            writer.write(videoBlob);
                        }, errorHandler);
                    }, errorHandler);
                }, errorHandler);
            }
        };
        xhr.open('GET', localStorage.audioVideoWebMURL, true);
        xhr.send(null);
    };

    function stopRecord() {
        if (isLog) console.log('stopRecord', arguments);
        if (timer) {
            clearInterval(timer);
            countdown = 0;
            timer = null;
        }

        recorder.stopRecording(function (audioVideoWebMURL) {
            var blob = recorder.getBlob();
            timeStart = null;
            iconService.setDefault();
            $tabCursor && chrome.tabs.sendMessage($tabCursor.id, {cursor: false});
            $tabCursor = null;
            activeTab = null;
            isRecording = false;

            try {
                audioPlayer && (audioPlayer = undefined);
                context && (context.close());
                context = undefined;
            } catch (e) {
                console.log(e)
            }
            chrome.tabs.query({active: true}, function (tabs) {
                chrome.tabs.sendMessage(tabs[0].id, {operation: 'status_video', status: getStatus(), state: getState()});
            });
            screenshot.changeVideoButton();
            if (!isError) {
                localStorage.audioVideoWebMURL = audioVideoWebMURL;
                localStorage.videoUrl = 'filesystem:chrome-extension://' + chrome.i18n.getMessage("@@extension_id") + '/persistent/video.webm';

                init(cb);

                chrome.downloads.download({
                    url: localStorage.audioVideoWebMURL,
                    filename: 'record-video.webm',
                    saveAs: false
                });
            }
            console.log('isError', isError);
            isError = false;
        });
    }

    function pauseRecord() {
        if (isLog) console.log('pauseRecord', arguments);
        if (recorder.state === 'recording') {
            timePause = Date.now();
            recorder.pauseRecording();
            iconService.setPause();
        } else {
            timePause = null;
            recorder.resumeRecording();
            iconService.setRec();
        }
        chrome.tabs.query({active: true}, function (tabs) {
            chrome.tabs.sendMessage(tabs[0].id, {operation: 'status_video', status: getStatus(), state: getState()});
        });
    }

    function getState() {
        return (recorder && recorder.state);
    }

    function getStatus() {
        return timer || !!streamVideo.active;
    }

    function getTimeRecord() {
        var date = Date.now();
        timeStart = timeStart + (timePause ? date - timePause : 0);
        timePause = timePause ? date : null;
        return timeStart ? (date - timeStart) : 0;
    }

    chrome.tabs.onUpdated.addListener(function (tabId, info) {
        chrome.tabs.query({currentWindow: true, active: true}, function (tabs) {
            if (info.status == "loading" && tabs[0].id && tabs[0].url && activeTab &&
                tabs[0].id == tabId && activeTab.id == tabId && !/^chrome/.test(tabs[0].url)) {

            }
        });
    });

    chrome.tabs.onRemoved.addListener(function (tabId, info) {
        if (activeTab && activeTab.id == tabId) {
            stopStream();
        }
    });

    return {
        capture: capture,
        captureTab: captureTab,
        captureDesktop: captureDesktop,
        stopRecord: stopStream,
        pauseRecord: pauseRecord,
        getStatus: getStatus,
        getState: getState,
        getTimeRecord: getTimeRecord,
        onMediaAccess: onMediaAccess
    }
})();