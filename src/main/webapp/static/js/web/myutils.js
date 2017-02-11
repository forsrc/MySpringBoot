;(function(window, document, Object) {
    'use strict';
    (function(window, factory) {
        var define = window.define || null;
        if (typeof define === 'function' && define.amd) {
            define('MyUtils', [], function() {
                return factory();
            });
        } else {
            window.MyUtils = factory();
        }
    })(window, function() {

        /**
         *
         * @param str {String}
         * @returns {number}
         */
        String.prototype.getByteLength = function (str) {
            var sTmpStr = str;
            var sTmpChar = "";
            var len = sTmpStr.length;
            var length = 0;
            var i = 0;
            for (; i < len; i++) {
                sTmpChar = sTmpStr.charAt(i);
                if (encodeURIComponent(sTmpChar).length > 4) {
                    length += 2;
                } else if (sTmpChar !== '\r') {
                    length += 1;
                }
            }
            return length;
        };

        /**
         * var str = "This {0} is test. {1} {-1}";
         * str = str.formatStr(["func", "ok"]);
         * @param args {Array}
         * @returns {string}
         */
        String.prototype.formatStr = function (args) {
            var str = this;
            var regex = "";
            if (this.formatStrRegExp) {
                regex = this.formatStrRegExp;
            } else {
                String.prototype.formatStrRegExp = /{(-?[0-9]+)}/g;
                regex = this.formatStrRegExp;
            }

            return str.replace(regex, function (match, number) {
                var intVal = Number(number);
                var replace = "";
                if (intVal >= 0 && intVal < args.length) {
                    replace = args[intVal];
                    return replace || match;
                }
                if (intVal < 0 && args.length + intVal >= 0 && args.length + intVal < args.length) {
                    replace = args[args.length + intVal];
                }
                return replace || match;
            });
        };

        /**
         * var str = "This {name} is test. {age}";
         * str = str.formatFromJson({"name": "ok", "age" : 27});
         * @param json {JSON}
         * @returns {String}
         */
        String.prototype.formatFromJson = function (json) {
            var str = this;
            var jsonObj = json;
            if (typeof jsonObj === "string") {
                try {
                    jsonObj = JSON.parse(jsonObj);
                } catch (e) {
                    //console.error(e);
                    jsonObj = json;
                    return str;
                }

            }

            var regex = "";
            if (this.formatFromJsonRegExp) {
                regex = this.formatFromJsonRegExp;
            } else {
                String.prototype.formatFromJsonRegExp = /{(\w+)}/g;
                regex = this.formatFromJsonRegExp;
            }
            try {
                str = str.replace(regex, function (match, item) {
                    return jsonObj[item] !== undefined ? jsonObj[item] : match;
                });

            } catch (e) {
                //console.error(e);
            }
            return str !== undefined ? str : this;
        };

        /**
         *
         * @param length {Number}
         * @returns {String}
         */
        String.prototype.zeropadding = function (length) {
            var __this = this;
            var __length = length - __this.length;
            var __str = __this;
            while (__length-- > 0) {
                __str = "0" + __str;
            }
            return __str;
        };


        /**
         *
         * @param formId {String} form id;
         * @returns {JSON}
         */
        function form2json(formId) {
            var form1 = document.getElementById(formId);
            var i = 0;
            var length = form1.length;
            var item = {};
            var json = {};
            for (; i < length; i++) {
                item = form1.elements[i];
                if (item.name) {
                    json[item.name] = item.value !== undefined ? item.value : '';
                }
            }
            return json;
        }

        /**
         *
         * @param arrays {Array}
         * @param callback {Function}  : callback({Number}, {Object}){}
         */
        function iterator(arrays, callback) {
            if (typeof callback !== "function") {
                throw new Error("Function callback in iterator() is not a function.");
            }
            var len = arrays.length;
            var i = 0;
            for (; i < len; i++) {
                callback.call(arrays[i], i, arrays[i]);
            }
        }

        /**
         *
         * @param callback {Function}  : callback({Number}, {Object}){}
         */
        Array.prototype.iterator = function (callback) {
            if (typeof callback !== "function") {
                throw new Error("Function callback in iterator() is not a function.");
            }
            var arrays = this;
            iterator(arrays, callback);
        };

        Array.prototype.contains = function (obj) {
            var __this = this;
            var __return = false;
            iterator(__this, function (i, v) {
                if (v === obj) {
                    return __return = true;
                }
            });
            return __return;
        };

        Array.prototype.add = function (obj) {
            this.push(obj);
        };

        Array.prototype.remove = function (obj) {
            var index = this.indexOf(obj);
            if (index >= 0) {
                this.splice(index, 1);
            }
        };

        var Map = function () {
            this.map = [];
            if (Map.__inited) {
                return this;
            }
            /**
             *
             * @param key {Object}
             * @param value {Object}
             * @returns {Map}
             */
            Map.prototype.put = function (key, value) {
                this.map.push({key: key, value: value});
                return this;
            };
            /**
             *
             * @param key {Object}
             * @returns {number} -1: none;
             */
            Map.prototype.indexOf = function (key) {
                var __map = this.map;
                var __return = -1;
                iterator(__map, function (i, v) {
                    if (v.key === key) {
                        return __return = i;
                    }
                });
                return __return;
            };
            /**
             *
             * @param key {Object}
             * @returns {Map}
             */
            Map.prototype.remove = function (key) {
                var __this = this;
                var index = __this.indexOf(key);
                if (index >= 0) {
                    __this.map.splice(index, 1);
                }
                return __this;
            };
            /**
             *
             * @param key {Object}
             * @returns {Object} null: none;
             */
            Map.prototype.get = function (key) {
                var __this = this;
                var index = __this.indexOf(key);
                if (index >= 0) {
                    return __this.map[index].value;
                }
                return null;
            };
            /**
             *
             * @returns {Map}
             */
            Map.prototype.clear = function () {
                //this.map.splice(0, this.map.length);
                this.map = [];
                return this;
            };
            Map.prototype.size = function () {
                return this.map.length;
            };
            Map.__inited = true;
        };

        /*
         * @param format {String} def: "yyyy-MM-dd HH:mm:ss.SSS"
         * @returns {String}
         */
        Date.prototype.format = function (format) {
            if (!Date.prototype.formatStr) {
                Date.prototype.formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
            }
            var __date = this;
            var __format = format ? format : Date.prototype.formatStr;
            return (__format + "")
                .replace("yyyy", __date.getFullYear() + "")
                .replace("MM", (__date.getMonth() + 1 + "").zeropadding(2))
                .replace("dd", (__date.getDate() + "").zeropadding(2))
                .replace("HH", (__date.getHours() + "").zeropadding(2))
                .replace("mm", (__date.getMinutes() + "").zeropadding(2))
                .replace("ss", (__date.getSeconds() + "").zeropadding(2))
                .replace("SSS", (__date.getMilliseconds() + "").zeropadding(3))
                ;
        };
        /**
         *
         * @param format {String} def: "yyyy-MM-dd HH:mm:ss.SSS"
         * @returns {Date}
         */
        String.prototype.toDate = function (format) {
            if (!Date.prototype.formatStr) {
                Date.prototype.formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
            }
            var __format = format ? format : Date.prototype.formatStr;
            var __date = new Date();
            var __this = this;
            __date.setFullYear(__this.substr(__format.indexOf("yyyy"), 4));
            __date.setMonth(parseInt(__this.substr(__format.indexOf("MM"), 2)) - 1);
            __date.setDate(__this.substr(__format.indexOf("dd"), 2));
            __date.setHours(__this.substr(__format.indexOf("HH"), 2));
            __date.setMinutes(__this.substr(__format.indexOf("mm"), 2));
            __date.setSeconds(__this.substr(__format.indexOf("ss"), 2));
            __date.setMilliseconds(__this.substr(__format.indexOf("SSS"), 3));
            return __date;
        };

        var LOGGING = (function () {
            var _instance;

            function constructor() {
                return {
                    enable: true,
                    log: function (msg) {
                        if (this.enable)
                            console.log(new Date() + " --> " + msg);
                    },
                    setEnable: function (enable) {
                        this.enable = enable;
                    }
                };
            }

            return {
                getInstance: function () {
                    if (!_instance) {
                        _instance = new constructor();
                    }
                    return _instance;
                }

            };
        })();

        var ajax = function (options) {
            options = {
                type: options.type || "GET",
                url: options.url || "",
                async: options.async === false ? false : true,
                timeout: options.timeout || 5000,
                data: options.data || null,
                username: options.username|| null,
                password: options.password|| null,
                onSuccess: options.onSuccess || function () {
                },
                onComplete: options.onComplete || function () {
                },
                onError: options.onError || function () {
                },
                typeJson: options.typeJson || {
                    "GET": "GET",
                    "POST": "POST",
                    "PUT": "PUT",
                    "PATCH": "PATCH",
                    "DELETE": "DELETE"
                },
                requestHeader: options.requestHeader || {
                    "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
                },
                overrideMimeType: options.overrideMimeType || null // "text/plain; charset=utf-8"
            };
            if (options.data && typeof options.data === "string") {
                try {
                    options.data = JSON.parse(options.data);
                } catch (e) {
                }
            }
            var xhr = getXMLHttpRequest(options.url);
            xhr.open(options.type, options.url, options.async, options.username, options.password);
            setRequestHeader(options.requestHeader, xhr);
            if (options.overrideMimeType) {
                xhr.overrideMimeType(options.overrideMimeType);
            }
            var isTimeout = false;
            setTimeout(function () {
                isTimeout = true;
            }, options.timeout);
            xhr.onreadystatechange = function () {
                if (xhr.readyState !== 4 && !isTimeout) {
                    return;
                }
                if (isSuccess(xhr)) {
                    var contentType = xhr.getResponseHeader("content-type");
                    var isXml = !options.type && contentType && contentType.indexOf("xml") >= 0;
                    var data = options.type === "xml" || isXml ? xhr.responseXML : xhr.responseText;
                    //options.onSuccess.call(this, data);
                    var jsonObj = data;
                    if (contentType.indexOf("json") > 0 || options.type.indexOf("json")) {
                        try {
                            jsonObj = JSON.parse(data);
                        } catch (e) {
                            jsonObj = data;
                        }
                    }
                    options.onSuccess(jsonObj, xhr);
                } else {
                    if (options.onError) {
                        //options.onError.call(this, xhr);
                        options.onError(xhr);
                    }
                }
                options.onComplete(xhr);
                xhr = null;
            };
            var data = formatData(options);
            window.setTimeout(function () {
                xhr.send(data);
            }, 0);
            function getXMLHttpRequest(url) {
                var xhr = null;
                try {
                    if (url && !isLocal(url)) {
                        xhr = new XDomainRequest();
                    }
                    xhr = xhr || new ActiveXObject("Msxml2.XMLHTTP");
                } catch (e1) {
                    try {
                        xhr = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e2) {
                        xhr = new XMLHttpRequest();
                    }
                }
                return xhr;
            }

            function setRequestHeader(requestHeader, xhr) {
                for (var key in requestHeader) {
                    xhr.setRequestHeader(key, requestHeader[key]);
                }
                return this;
            }

            var isLocal = function (url) {
                var a = document.createElement('a');
                a.href = url;
                return a.hostname === window.location.hostname;
            };

            function isSuccess(xhr) {
                try {
                    return (!xhr.status && location.protocol === "file:")
                        || (xhr.status >= 200 && xhr.status < 300)
                        || xhr.status === 304
                        || (navigator.userAgent.indexOf("Safari") >= 0 && typeof xhr.status === "undefined");
                } catch (e) {
                    return false;
                }
            }

            function formatData(options) {
                if (options.data && typeof options.data === 'object') {
                    var dataStr = "";
                    options.data["_method"] = options.typeJson[options.type];
                    for (var key in options.data) {
                        dataStr += key + "=" + encodeURIComponent(options.data[key]) + "&";
                    }
                    if (dataStr) {
                        dataStr = dataStr.substr(0, dataStr.length - 1);
                    }
                    return dataStr;
                }
                return options.data;
            }
        };

        var load = function (className, node, tag) {
            className = className || "js-load";
            var targets = getElementsByClassName(className, node, tag);
            targets.forEach(function (target) {
                var url = target.getAttribute("data-url");
                var data = target.getAttribute("data-params");
                send(url, data, function (html) {
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

            function send(url, data, callback) {
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
                ajax({
                    type: "GET",
                    url: url,
                    async: true,
                    data: data,
                    timeout: 5000,
                    onSuccess: function (html) {
                        callback(html);
                    },
                    onComplete: function () {

                    },
                    onError: function () {

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
        };

        var TableLoad = TableLoad || (function () {
                var _instance;

                function constructor() {
                    return {
                        tableLoadJs: function (cls) {
                            var tables = document.getElementsByTagName("table");
                            var len = tables.length;
                            var i = 0;
                            if (!cls) {
                                cls = "table-load";
                            }
                            for (i = 0; i < len; i++) {
                                var table = tables[i];
                                var className = table.className;
                                if (cls === className) {
                                    this.tableLoadJsData(i, table);
                                }
                            }
                        },
                        tableLoadJsData: function (i, table) {
                            if (table.getAttribute("table-load-done")) {
                                return;
                            }
                            table.setAttribute("table-load-done", true);
                            var tBodies = table.tBodies;
                            var len = tBodies.length;
                            var tbody = null;
                            var j = 0;
                            for (j = 0; j < len; j++) {
                                var tb = tBodies[j];
                                var className = tb.className;
                                if ("table-load-tbody" === className) {
                                    tbody = tb;
                                }
                            }
                            if (tbody === null) {
                                return;
                            }
                            var tableId = table.getAttribute("id");
                            if (!tableId) {
                                tableId = "table-load-" + i;
                                table.setAttribute("id", tableId);
                            }
                            var url = table.getAttribute("data-url");
                            var html = tbody.innerHTML;
                            var jsonkey = table.getAttribute("data-jsonkey");

                            ajax({
                                type: "GET",
                                url: url,
                                async: true,
                                data: {"function": "table-load"},
                                timeout: 5000,
                                onSuccess: function (jsonData) {
                                    if (!jsonData) {
                                        return;
                                    }
                                    var data = jsonkey ? jsonData[jsonkey] : jsonData;
                                    var h = "";
                                    var len = data.length;
                                    var index = 0;
                                    for (index = 0; index < len; index++) {
                                        var json = data[index];
                                        json["_table_id"] = tableId;
                                        json["_table_index"] = i;
                                        json["_tr_index"] = index;
                                        json["_tr_number"] = index + 1;
                                        h += html.formatFromJson(json);
                                    }
                                    tbody.innerHTML = h;
                                },
                                onComplete: function () {

                                },
                                onError: function () {

                                }
                            });
                        }
                    };
                }

                return {
                    getInstance: function () {
                        if (!_instance) {
                            _instance = new constructor();
                        }
                        return _instance;
                    }

                };
            })();

        return {
            load: function (className, node, tag) {
                load(className, node, tag);
            },
            ajax: function (options) {
                ajax(options);
            },
            innerHtml: function (target, html) {
                innerHtml(target, html);
            },
            tableLoadJs: function (cls) {
                TableLoad.getInstance().tableLoadJs(cls);
            }
        };
    });
})(window, document, Object);