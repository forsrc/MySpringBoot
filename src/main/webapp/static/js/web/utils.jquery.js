"use strict";

/**
 *
 * $("#form").form2json();
 * @returns {JSON}
 */
$.fn.form2json = function () {
    var json = {};
    var array = this.serializeArray();
    $.each(array, function () {
        if (!json[this.name]) {
            json[this.name] = this.value !== undefined ? this.value : '';
            return
        }

        if (!json[this.name].push) {
            json[this.name] = [json[this.name]];
        }
        json[this.name].push(this.value !== undefined ? this.value : '');
    });
    return json;
};