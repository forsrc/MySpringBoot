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
var TABLELOAD = TABLELOAD || (function () {
        var _instance;

        function constructor() {
            return {
                tableLoad: function (cls) {
                    if (!cls) {
                        cls = ".table-load";
                    }
                    var _this = this;
                    $(cls).each(function (i, table) {
                        _this.tableLoadData(i, table);
                    });
                },
                tableLoadData: function (i, table) {
                    var $table = $(table);
                    if ($table.attr("table-load-done")) {
                        return;
                    }
                    $table.attr("table-load-done", true);
                    var tableId = $table.attr("id");
                    if (!tableId) {
                        tableId = "table-load-" + i;
                        $table.attr("id", tableId);
                    }
                    $table.data("table-load-index", i);
                    var url = $table.attr("data-url");
                    var tbody = $(".table-load-tbody", $table);
                    var html = tbody.html();
                    var jsonkey = $table.attr("data-jsonkey");
                    $.getJSON(url, function (jsonData) {
                        var data = jsonkey ? jsonData[jsonkey] : jsonData;
                        var h = "";
                        $.each(data, function (index, json) {
                            json["_table_id"] = tableId;
                            json["_table_index"] = i;
                            json["_tr_index"] = index;
                            json["_tr_number"] = index + 1;
                            h += html.formatFromJson(json);
                        });
                        tbody.html(h);
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