"use strict";
/**
 * var myDes = MyDes(key);
 * var myDes = MyDes(key, true);
 * @param key {String}
 * @param isNewInstance {boolean} true|false; def: false;
 * @returns {MyDes|*}
 * @constructor
 */
function MyDes(key, isNewInstance) {
    if (!key) {
        throw new Error("Error --> key: " + key + ";");
    }
    if (!isNewInstance && MyDes.instance) {
        return MyDes.instance;
    }
    this.key = CryptoJS.enc.Utf8.parse(key);
    this.config = {
        "mode": CryptoJS.mode.ECB,
        "padding": CryptoJS.pad.Pkcs7
    };
    if (MyDes._initialized) {
        //return MyDes.instance;
    }

    /**
     * encrypt(string)
     * @param string {String}
     * @returns {String} encrypted string;
     */
    MyDes.prototype.encrypt = function (string) {
        var __this = this;
        var __encryptedString = CryptoJS.DES.encrypt(string, __this.key, __this.config);
        return __encryptedString.toString();
    };

    /**
     * decrypt(encryptedString)
     * @param encryptedString {String}
     * @returns {*} decrypted string;
     */
    MyDes.prototype.decrypt = function (encryptedString) {
        var __this = this;
        var decryptedString = CryptoJS.DES.decrypt(
            {"ciphertext": CryptoJS.enc.Base64.parse(encryptedString)},
            __this.key,
            __this.config);
        return decryptedString.toString(CryptoJS.enc.Utf8);
    };

    MyDes._initialized = true;
    MyDes.instance = this;
}