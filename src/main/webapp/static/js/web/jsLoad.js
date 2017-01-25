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
                    //target.innerHTML = html;
                    innerHtml(target, html);
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
                if (data && typeof data === "string") {
                    data += data.indexOf("?") > 0 ? "&" : "?";
                    data += "function=JsLoad";
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

            function innerHtml(target, text) {
                target.innerHTML = text;
                var scripts = [];

                var ret = target.childNodes;
                for (var i = 0; ret[i]; i++) {
                    if (scripts && nodeName(ret[i], "script") && (!ret[i].type
                        || ret[i].type.toLowerCase() === "text/javascript")) {
                        scripts.push(ret[i].parentNode ? ret[i].parentNode.removeChild(ret[i]) : ret[i]);
                    }
                }

                for (var script in scripts) {
                    evalScript(scripts[script]);
                }
            }

            function nodeName(elem, name) {
                return elem.nodeName && elem.nodeName.toUpperCase() === name.toUpperCase();
            }

            function evalScript(elem) {
                var data = ( elem.text || elem.textContent || elem.innerHTML || "" );

                var head = document.getElementsByTagName("head")[0] || document.documentElement,
                    script = document.createElement("script");
                script.type = "text/javascript";
                script.appendChild(document.createTextNode(data));
                head.insertBefore(script, head.firstChild);
                head.removeChild(script);

                if (elem.parentNode) {
                    elem.parentNode.removeChild(elem);
                }
            }
            ;
        };
        return {
            load: function(className, node, tag) {
                load(className, node, tag);
            },
            innerHtml: function (target, html) {
                innerHtml(target, html);
            }
        };
    });
})(window, document, Object);