"use strict";
/**
 * var myRsa = MyRsa(n, e, d);
 * var myRsa = MyRsa(n, e, d, true);
 * @param n {String}
 * @param e {Number}
 * @param d {String}
 * @param isNewInstance {boolean} true|false; def: false;
 * @returns {MyRsa|*}
 * @constructor
 */
function MyRsa(n, e, d, isNewInstance) {
    if (!n) {
        throw new Error("Error --> n: is null; e: " + e + "; d: " + d);
    }
    if (!isNewInstance && MyRsa.instance) {
        return MyRsa.instance;
    }
    //this.n = n;
    //this.e = e;
    //this.d = d;
    this.bin = typeof(n) === "string" ? new BigInteger(n) : n;
    this.bie = typeof(e) === "string" ? new BigInteger(e) : e;
    this.bid = typeof(d) === "string" ? new BigInteger(d) : d;
    this.BLOCK_SIZE = 102;
    if (MyRsa._initialized) {
        //return MyRsa.instance;
    }

    /**
     *
     * @param bigInteger {BigInteger}
     * @returns {BigInteger} encrypted bigInteger;
     */
    MyRsa.prototype.encryptBigInteger = function (bigInteger) {
        var __this = this;
        return bigInteger.modPow(__this.bie, __this.bin);
    };

    /**
     * decryptBigInteger(encryptionBigInteger)
     * @param encryptionBigInteger {BigInteger}
     * @returns {BigInteger} decryption bigInteger;
     */
    MyRsa.prototype.decryptBigInteger = function (encryptionBigInteger) {
        var __this = this;
        return encryptionBigInteger.modPow(__this.bid, __this.bin);
    };

    /**
     * encrypt(string)
     * @param string
     * @returns {String} encrypted string;
     */
    MyRsa.prototype.encrypt = function (string) {
        var __this = this;
        var __msgBigInteger = __this.string2BigInteger(string);
        var __encrypt = __this.encryptBigInteger(__msgBigInteger).toString();
        return Base64.encode(__encrypt);
    };

    /**
     * decrypt(base64EncryptedString)
     * @param base64EncryptedString
     * @returns {String} decrypted string;
     */
    MyRsa.prototype.decrypt = function (base64EncryptedString) {
        var __decode = Base64.decode(base64EncryptedString);
        var __encryptedBigInteger = new BigInteger(__decode);
        var __this = this;
        var __decrypt = __this.decryptBigInteger(__encryptedBigInteger);
        return __this.bigInteger2String(__decrypt);
    };

    /**
     * string2BigInteger(string)
     * @param string
     * @returns {BigInteger} base64 encoded bigInteger;
     */
    MyRsa.prototype.string2BigInteger = function (string) {
        var __msg = Base64.encode(string);

        return this.toBigInteger(__msg);
    };

    /**
     * bigInteger2String(number)
     * @param number {string}
     * @returns {String} base64 decoded string;
     */
    MyRsa.prototype.bigInteger2String = function (bigInteger) {
        var __str = this.toStr(bigInteger);
        return Base64.decode(__str);
    };

    MyRsa.prototype.toBigInteger = function (string) {
        var __msg = string;
        var __numberString = "1";
        var __c = '';
        var __asc = "";
        var __len = __msg.length;
        var i = 0;
        for (; i < __len; ++i) {
            __c = __msg.charAt(i);
            __asc = __c.charCodeAt(0) + "";
            if (__asc.length <= 2) {
                __numberString += "0";
            }
            __numberString += __asc;
        }
        return new BigInteger(__numberString);
    };


    MyRsa.prototype.string2BigIntegers = function (string) {
        var __base64 = Base64.encode(string);
        var __this = this;

        var __length = Math.ceil(__base64.length * 1.0 / __this.BLOCK_SIZE);

        var __bigIntegers = [];
        var __text = "";
        var __start = 0;
        for (var i = 0; i < __length - 1; i++) {
            __start = i * __this.BLOCK_SIZE;
            __text = __base64.substring(__start, __start + __this.BLOCK_SIZE);
            __bigIntegers.push(__this.toBigInteger(__text));
        }
        if (__length == 1) {
            __bigIntegers.push(__this.toBigInteger(__base64));
        }
        if (__length > 1) {
            __start = (__length - 1) * __this.BLOCK_SIZE;
            __text = __base64.substring(__start, __base64.length);
            __bigIntegers.push(__this.toBigInteger(__text));
        }

        return __bigIntegers;
    };

    MyRsa.prototype.toStr = function (bigInteger) {
        var __numberString = bigInteger.toString();
        __numberString = __numberString.substring(1, __numberString.length);
        var __message = "";
        var __blockString = "";
        var __block = 0;
        var __length = __numberString.length;
        var i = 0;
        for (; i < __length; i += 3) {
            __blockString = __numberString.substring(i, i + 3);
            __block = parseInt(__blockString);
            __message += String.fromCharCode(__block);
        }
        return __message.toString();
    };

    MyRsa.prototype.bigIntegers2String = function (bigIntegers) {
        var __this = this;
        var __length = bigIntegers.length;

        var __text = "";
        var i = 0;
        for (; i < __length; i++) {
            __text += __this.toStr(bigIntegers[i]);
        }

        return Base64.decode(__text);
    };

    MyRsa.prototype.encrypt2 = function (plaintext) {
        var __this = this;

        var __bigIntegers = __this.string2BigIntegers(plaintext);

        var __text = "";
        var __length = __bigIntegers.length;
        var i = 0;
        for (; i < __length; i++) {
            __text += "" + __this.encryptBigInteger(__bigIntegers[i]).toString()
                + "$";
        }

        return Base64.encode(__text.substring(0, __text.length - 1));
    };

    MyRsa.prototype.decrypt2 = function (base64EncryptedString) {
        var __decode = Base64.decode(base64EncryptedString);

        var __this = this;
        var __texts = __decode.split("$");
        var __bigIntegers = [];
        var __length = __texts.length;
        var __bigInteger = null;
        for (var i = 0; i < __length; i++) {
            __bigInteger = new BigInteger(__texts[i]);
            __bigIntegers.push(__this.decryptBigInteger(__bigInteger));
        }
        return __this.bigIntegers2String(__bigIntegers);
    };


    MyRsa._initialized = true;
    MyRsa.instance = this;
}