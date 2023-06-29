'use strict';

var iconService = (function () {
    function getPath(a) {
        return {
            path: {
                16: "images/icons/16x16" + a + ".png",
                48: "images/icons/48x48" + a + ".png",
                128: "images/icons/128x128" + a + ".png"
            }
        }
    }
    function setIcon(type) {
        chrome.browserAction.setIcon(getPath(type));
    }

    function setDefault() {
        setIcon('');
    }

    function setRec() {
        setIcon('rec');
    }

    function setPause() {
        setIcon('paused');
    }


    return {
        setDefault: setDefault,
        setRec: setRec,
        setPause: setPause,
    }
})();