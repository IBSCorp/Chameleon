var i18nTemplate = function () {
    var getValue = function (v) {
        return chrome.i18n.getMessage(v);
    };
    var attr = 'i18n-content';
    var nodes = document.querySelectorAll('*['+attr+']');

    [].forEach.call(nodes, function (e) {
        var text = getValue(e.getAttribute(attr));
        if (text) e.innerText  = text;
    });
};
i18nTemplate();