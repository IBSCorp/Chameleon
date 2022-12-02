(function () {
    if (!EXT_HOTKEY_JS_INSERTED) {
        var EXT_HOTKEY_JS_INSERTED = true;
        var hotkeys = {};

        chrome.extension.sendRequest({operation: 'hotkeys'}, function (response) {
            if (response)
                if (response.hotkeys) {
                    hotkeys = JSON.parse(response.hotkeys);
                }
        });

        var sendToChrome = function (type) {
            chrome.extension.sendRequest({'operation': 'hotkey', 'name': type});
        };

        window.addEventListener('keydown', function (e) {
            var k = e.keyCode;
            if (e.shiftKey && e.ctrlKey) {
                if (k == hotkeys.entire) {
                    sendToChrome('entire');
                    e.preventDefault();
                    return false;
                }

                if (k == hotkeys.fragment) {
                    sendToChrome('fragment');
                    e.preventDefault();
                    return false;
                }

                if (k == hotkeys.selected) {
                    sendToChrome('selected');
                    e.preventDefault();
                    return false;
                }
                if (k == hotkeys.scroll) {
                    sendToChrome('scroll');
                    e.preventDefault();
                    return false;
                }
                if (k == hotkeys.visible) {
                    sendToChrome('visible');
                    e.preventDefault();
                    return false;
                }
                if (k == hotkeys.window) {
                    sendToChrome('window');
                    e.preventDefault();
                    return false;
                }
            }

            return true;
        }, false);
    }
})();