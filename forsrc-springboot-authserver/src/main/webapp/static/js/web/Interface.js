/**
 var InterfaceA = new Interface('InterfaceA', ['methodA', 'methodB']);
 var A = function () {
    };
 A.prototype = {
    methodA: function () {
        alert("methodA")
    },
    methodB: function () {
        alert("methodB")
    }
    };
 var a = new A();
 Interface.ensureImplements(a, [InterfaceA]);
 a.methodA();
 */
;(function (window, document, Object) {
    'use strict';
    (function (window, factory) {
        var define = window.define || null;
        if (typeof define === 'function' && define.amd) {
            define('Interface', [], function () {
                return factory();
            });
        } else {
            window.Interface = factory();
        }
    })(window, function () {


        /**
         *
         var InterfaceA = new Interface('InterfaceA', ['methodA', 'methodB']);
         var A = function(){};
         A.prototype = {
            methodA : function(){...},
            methodB : function(){...}
         }
         var a = new A();
         Interface.ensureImplements(a, [InterfaceA, InterfaceB]);
         * @param name {String} the interface's name;
         * @param methods {String[]} the interface method's names;
         * @constructor
         */
        var Interface = Interface || function (name, methods) {
                if (arguments.length !== 2) {
                    throw new Error("Interface constructor called with " + arguments.length +
                        "arguments, but expected exactly 2.");
                }
                this.name = name;
                this.methods = [];
                var i = 0;
                var len = methods.length;
                for (; i < len; i++) {
                    if (typeof methods[i] !== 'string') {
                        throw new Error("Interface constructor expects method names to be "
                            + "passed in as a string.");
                    }
                    this.methods.push(methods[i]);
                }
                if (Interface._initialized) {
                    return;
                }

                /**
                 * checkMethods(a, InterfaceA)
                 * @param object {Interface} the instance of the Interface;
                 * @param interfaceClass {Interface} the Interface;
                 */
                Interface.prototype.checkMethods = function (object, interfaceClass) {
                    var j = 0;
                    var methodsLen = interfaceClass.methods.length;
                    for (; j < methodsLen; j++) {
                        var method = interfaceClass.methods[j];
                        if (!object[method] || typeof object[method] !== 'function') {
                            throw new Error("Function Interface.ensureImplements: object "
                                + "does not implement the '" + interfaceClass.name
                                + "' interface. Method '" + method + "' was not found.");
                        }
                    }
                };

                /**
                 * Interface.checkImplements(a, [InterfaceA, InterfaceB]);
                 * @param object {Interface} the instance of the Interface;
                 * @param interfaces {Interface[]} the interface classes;
                 */
                Interface.prototype.checkImplements = function (object, interfaces) {
                    if (arguments.length < 2) {
                        throw new Error("Function Interface.ensureImplements called with " +
                            arguments.length + "arguments, but expected at least 2.");
                    }
                    var i = 0;
                    var len = interfaces.length;
                    for (; i < len; i++) {
                        interfaces[i].check(object);
                    }
                };
                /**
                 * checkMethods(a, InterfaceA)
                 * @param object {Interface} the instance of the Interface;
                 */
                Interface.prototype.check = function (object) {
                    var __interface = this;
                    if (__interface.constructor !== Interface) {
                        throw new Error("Function Interface.ensureImplements expects arguments"
                            + " two and above to be instances of Interface.");
                    }
                    __interface.checkMethods(object, __interface);
                };
                Interface._initialized = true;
            };
        /**
         * Interface.ensureImplements(a, [InterfaceA, InterfaceB]);
         * @param object {Interface} the instance of the Interface;
         * @param interfaces {Interface[]} the interface classes;
         */
        Interface.ensureImplements = Interface.ensureImplements || function (object, interfaces) {
                iterator(interfaces, function (i, v) {
                    v.check(object);
                });
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
            };

        return Interface;
    });
})(window, document, Object);

