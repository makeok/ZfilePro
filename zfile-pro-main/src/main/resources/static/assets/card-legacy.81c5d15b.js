!function(){function r(e){return r="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(r){return typeof r}:function(r){return r&&"function"==typeof Symbol&&r.constructor===Symbol&&r!==Symbol.prototype?"symbol":typeof r},r(e)}function e(r,e){var o=Object.keys(r);if(Object.getOwnPropertySymbols){var t=Object.getOwnPropertySymbols(r);e&&(t=t.filter((function(e){return Object.getOwnPropertyDescriptor(r,e).enumerable}))),o.push.apply(o,t)}return o}function o(r){for(var o=1;o<arguments.length;o++){var a=null!=arguments[o]?arguments[o]:{};o%2?e(Object(a),!0).forEach((function(e){t(r,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(r,Object.getOwnPropertyDescriptors(a)):e(Object(a)).forEach((function(e){Object.defineProperty(r,e,Object.getOwnPropertyDescriptor(a,e))}))}return r}function t(e,o,t){var a;return a=function(e,o){if("object"!=r(e)||!e)return e;var t=e[Symbol.toPrimitive];if(void 0!==t){var a=t.call(e,o||"default");if("object"!=r(a))return a;throw new TypeError("@@toPrimitive must return a primitive value.")}return("string"===o?String:Number)(e)}(o,"string"),(o="symbol"==r(a)?a:String(a))in e?Object.defineProperty(e,o,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[o]=t,e}var a=document.createElement("style");a.innerHTML=".el-card{--el-card-border-color: var(--el-border-color-light);--el-card-border-radius: 4px;--el-card-padding: 20px;--el-card-bg-color: var(--el-fill-color-blank);border-radius:var(--el-card-border-radius);border:1px solid var(--el-card-border-color);background-color:var(--el-card-bg-color);overflow:hidden;color:var(--el-text-color-primary);transition:var(--el-transition-duration)}.el-card.is-always-shadow{box-shadow:var(--el-box-shadow-light)}.el-card.is-hover-shadow:hover,.el-card.is-hover-shadow:focus{box-shadow:var(--el-box-shadow-light)}.el-card__header{padding:calc(var(--el-card-padding) - 2px) var(--el-card-padding);border-bottom:1px solid var(--el-card-border-color);box-sizing:border-box}.el-card__body{padding:var(--el-card-padding)}.el-card__footer{padding:calc(var(--el-card-padding) - 2px) var(--el-card-padding);border-top:1px solid var(--el-card-border-color);box-sizing:border-box}\n",document.head.appendChild(a),System.register(["./base-legacy.ba5b7a3a.js","./index-legacy.559a5434.js"],(function(r){"use strict";var e,t,a,n,d,l,i,c,s,b,u,f,y,p,v,g;return{setters:[function(r){e=r.b,t=r.d,a=r.a,n=r._,d=r.w},function(r){l=r.d,i=r.a,c=r.f,s=r.g,b=r.u,u=r.i,f=r.z,y=r.A,p=r.l,v=r.y,g=r.n}],execute:function(){var h=e({header:{type:String,default:""},footer:{type:String,default:""},bodyStyle:{type:t([String,Object,Array]),default:""},bodyClass:String,shadow:{type:String,values:["always","hover","never"],default:"always"}}),m=l({name:"ElCard"}),w=l(o(o({},m),{},{props:h,setup:function(r){var e=a("card");return function(r,o){return i(),c("div",{class:s([b(e).b(),b(e).is("".concat(r.shadow,"-shadow"))])},[r.$slots.header||r.header?(i(),c("div",{key:0,class:s(b(e).e("header"))},[u(r.$slots,"header",{},(function(){return[f(y(r.header),1)]}))],2)):p("v-if",!0),v("div",{class:s([b(e).e("body"),r.bodyClass]),style:g(r.bodyStyle)},[u(r.$slots,"default")],6),r.$slots.footer||r.footer?(i(),c("div",{key:1,class:s(b(e).e("footer"))},[u(r.$slots,"footer",{},(function(){return[f(y(r.footer),1)]}))],2)):p("v-if",!0)],2)}}}));r("E",d(n(w,[["__file","card.vue"]])))}}}))}();
