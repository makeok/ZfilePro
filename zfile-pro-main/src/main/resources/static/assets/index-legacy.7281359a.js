System.register(["./index-legacy.559a5434.js"],(function(e){"use strict";var n;return{setters:[function(e){n=e.K}],execute:function(){e("v",{beforeMount:function(e,t){var o,u,i=t.value,r=n(i)?{}:i,c=r.interval,a=void 0===c?100:c,d=r.delay,s=void 0===d?600:d,v=function(){return n(i)?i():i.handler()},f=function(){u&&(clearTimeout(u),u=void 0),o&&(clearInterval(o),o=void 0)};e.addEventListener("mousedown",(function(e){0===e.button&&(f(),v(),document.addEventListener("mouseup",(function(){return f()}),{once:!0}),u=setTimeout((function(){o=setInterval((function(){v()}),a)}),s))}))}})}}}));
