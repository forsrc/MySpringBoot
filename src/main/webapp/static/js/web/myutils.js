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
        return {
        };
    });
})(window, document, Object);