!function(){function t(n){return t="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t},t(n)}System.register(["./base-legacy.ba5b7a3a.js","./_Uint8Array-legacy.be503b59.js"],(function(n){"use strict";var r,e,o,u,c,f,i,a,y;return{setters:[function(t){r=t.H,e=t.M,o=t.am,u=t.as},function(t){c=t.m,f=t.i,i=t.o,a=t.p,y=t.U}],execute:function(){n({a:function(t,n){var r=-1,e=t.length;n||(n=Array(e));for(;++r<e;)n[r]=t[r];return n},b:function(t,n){if(n)return t.slice();var r=t.length,e=j?j(r):new t.constructor(r);return t.copy(e),e},c:function(t,n,r,u){var c=!r;r||(r={});var f=-1,i=n.length;for(;++f<i;){var a=n[f],y=u?u(r[a],t[a],a,r,t):void 0;void 0===y&&(y=t[a]),c?e(r,a,y):o(r,a,y)}return r},d:function(t,n){var r=n?h(t.buffer):t.buffer;return new t.constructor(r,t.byteOffset,t.length)},e:h,i:function(t){return"function"!=typeof t.constructor||c(t)?{}:l(v(t))},k:function(t){return f(t)?i(t,!0):function(t){if(!r(t))return function(t){var n=[];if(null!=t)for(var r in Object(t))n.push(r);return n}(t);var n=c(t),e=[];for(var o in t)("constructor"!=o||!n&&b.call(t,o))&&e.push(o);return e}(t)}});var s=Object.create,l=function(){function t(){}return function(n){if(!r(n))return{};if(s)return s(n);t.prototype=n;var e=new t;return t.prototype=void 0,e}}();var b=Object.prototype.hasOwnProperty;var p=a(Object.getPrototypeOf,Object),v=n("g",p),d="object"==t(n)&&n&&!n.nodeType&&n,m=d&&"object"==("undefined"==typeof module?"undefined":t(module))&&module&&!module.nodeType&&module,g=m&&m.exports===d?u.Buffer:void 0,j=g?g.allocUnsafe:void 0;function h(t){var n=new t.constructor(t.byteLength);return new y(n).set(new y(t)),n}}}}))}();
