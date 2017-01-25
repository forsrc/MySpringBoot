;
(function(window, document, Object) {
    'use strict';
    (function(window, factory) {
        // Expose Ajax
        var define = window.define || null;
        if (typeof define === 'function' && define.amd) {
            // AMD
            define('JsLoad', [], function() {
                return factory();
            });
        } else {
            window.JsLoad = factory();
        }
    })(window, function() {

        var load = function(className, node, tag) {
            className = className || "js-load";
            var targets = getElementsByClassName(className, node, tag);
            targets.forEach(function(target) {
                var url = target.getAttribute("data-url");
                var data = target.getAttribute("data-params");
                ajax(url, data, function(html) {
                    target.innerHTML = html;
                });
            });

            function getElementsByClassName(className, node, tag) {
                var classElements = [];
                node = node || document;
                tag = tag || '*';
                var els = node.getElementsByTagName(tag);
                var elsLen = els.length;
                var pattern = new RegExp("(^|\\s)" + className + "(\\s|$)");
                for (var i = 0, j = 0; i < elsLen; i++) {
                    if (pattern.test(els[i].className)) {
                        classElements[j] = els[i];
                        j++;
                    }
                }
                return classElements;
            }

            function ajax(url, data, callback) {
                data = data || null;
                if (data && typeof data === "string") {
                    try {
                        data = JSON.parse(data + "");
                        data["function"] = "JsLoad";
                    } catch (e) {
                    }
                }
                Ajax.send({
                    type: "GET",
                    url: url,
                    async: true,
                    data: data,
                    timeout: 5000,
                    onSuccess: function(html) {
                        callback(html);
                    },
                    onComplete: function() {

                    },
                    onError: function() {

                    }
                });
            }
            ;
        };
        return {
            load: function(className, node, tag) {
                load(className, node, tag);
            }
        };
    });
})(window, document, Object);