!function(){var e=document.createElement("style");e.innerHTML=".expression-item[data-v-103a6950] .el-input__wrapper{width:100%}@media (min-width: 640px){.expression-item[data-v-103a6950] .el-input__wrapper{width:9rem}}@media (min-width: 768px){.expression-item[data-v-103a6950] .el-input__wrapper{width:12rem}}@media (min-width: 1024px){.expression-item[data-v-103a6950] .el-input__wrapper{width:16rem}}@media (min-width: 1280px){.expression-item[data-v-103a6950] .el-input__wrapper{width:20rem}}.zfile-admin-password-form[data-v-103a6950] .z-form-sub-title{max-width:100%}.zfile-admin-password-form .rules-tips[data-v-103a6950]{padding:.25rem;font-size:.875rem;line-height:1.25rem;font-weight:700}.zfile-admin-password-form .rules-tips-link[data-v-103a6950]{margin-top:.5rem}.zfile-admin-password-form .rules-tips-link[data-v-103a6950]>:not([hidden])~:not([hidden]){--tw-space-x-reverse: 0;margin-right:calc(1.25rem * var(--tw-space-x-reverse));margin-left:calc(1.25rem * calc(1 - var(--tw-space-x-reverse)))}.zfile-admin-password-form .rules-tips-link svg[data-v-103a6950]{height:1rem;line-height:1.25rem;vertical-align:text-bottom}\n",document.head.appendChild(e),System.register(["./base-legacy.ba5b7a3a.js","./loading-legacy.a92cda73.js","./button-legacy.96a0013c.js","./tooltip-legacy.7c89f687.js","./popper-legacy.2c9263bf.js","./input-legacy.d4357d07.js","./alert-legacy.cdb63236.js","./SvgIcon-legacy.040a47d9.js","./index-legacy.559a5434.js","./ZFormItem-legacy.eb663a6a.js","./admin-storage-legacy.ee536f65.js","./common-legacy.4e7090ed.js","./sortable.esm-legacy.a39a4582.js","./request-legacy.a1774e71.js","./index-legacy.7381ad7d.js","./route-block-legacy.9534e494.js","./plugin-vue_export-helper-legacy.7bb61c33.js","./CheckBadgeIcon-legacy.21039039.js","./FolderIcon-legacy.6bda3b14.js","./index-legacy.dafda260.js","./directive-legacy.1a2f693e.js","./focus-trap-legacy.a75696a1.js","./index-legacy.e449b062.js","./index-legacy.bd66d165.js","./scroll-legacy.0e94e88d.js","./validator-legacy.df5a4111.js","./event-legacy.d3c9bc11.js"],(function(e){"use strict";var n,a,t,i,r,o,l,s,c,u,d,f,p,m,g,v,w,h,y,b,x,j,k,_,I,z,V,E,C,P,U,D,q,B,H,L,S,Z,M,T,A,F;return{setters:[function(e){n=e.x},function(){},function(){},function(){},function(e){a=e.E},function(){},function(e){t=e.E},function(e){i=e._},function(e){r=e.a,o=e.f,l=e.y,s=e.r,c=e.aa,u=e.ac,d=e.o,f=e.Z,p=e.D,m=e.u,g=e.b,v=e.e,w=e.j,h=e.A,y=e.bu,b=e.z,x=e.O,j=e.P,k=e.bv,_=e.a3,I=e.bg,z=e.af,V=e.ag},function(e){E=e.Z,C=e.a},function(e){P=e.p,U=e.q,D=e.i},function(e){q=e.u},function(e){B=e.S},function(e){H=e.E},function(e){L=e.E},function(e){S=e.b},function(e){Z=e._},function(e){M=e.r},function(e){T=e.r},function(e){A=e.E},function(e){F=e.v},function(){},function(){},function(){},function(){},function(){},function(){}],execute:function(){function G(e,n){return r(),o("svg",{xmlns:"http://www.w3.org/2000/svg",fill:"none",viewBox:"0 0 24 24","stroke-width":"1.5",stroke:"currentColor","aria-hidden":"true","data-slot":"icon"},[l("path",{"stroke-linecap":"round","stroke-linejoin":"round",d:"M15.75 5.25a3 3 0 0 1 3 3m3 0a6 6 0 0 1-7.029 5.912c-.563-.097-1.159.026-1.563.43L10.5 17.25H8.25v2.25H6v2.25H2.25v-2.818c0-.597.237-1.17.659-1.591l6.499-6.499c.404-.404.527-1 .43-1.563A6 6 0 1 1 21.75 8.25Z"})])}var O=s([]),W=s(!1);function J(e,n){var a=n.params.storageId,t=function(){var e=document.querySelector(".z-form-body");B.create(e,{draggable:".expression-item",onEnd:function(e){if(e.oldIndex!==e.newIndex){var n=O.value.splice(e.oldIndex-1,1)[0];O.value.splice(e.newIndex-1,0,n)}}})},i=function(){O.value.push({description:"表达式 - "+(O.value.length+1),expression:"",password:"",storageId:a})};return{loading:W,loadPasswordData:function(){P(a).then((function(e){O.value=e.data,0===O.value.length&&i(),t()}))},passwordList:O,addPasswordItem:i,deletePasswordItem:function(e){O.value.splice(e,1)},savePasswordData:function(){if(!O.value.find((function(e){if(q.isEmpty(e.description)||q.isEmpty(e.expression)||q.isEmpty(e.password))return H.warning("请检查数据填写是否完整"),!0}))){W.value=!0;for(var n=0;n<O.value.length;n++){O.value[n].id=n+1}U(a,O.value).then((function(){L.confirm("保存成功, 是否返回存储源列表？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"success",callback:function(n){"confirm"===n&&e.push("/admin/storage-list")}})})).finally((function(){W.value=!1}))}}}}var K=function(e){return z("data-v-103a6950"),e=e(),V(),e},N={class:"flex justify-items-center"},Q=K((function(){return l("div",{class:"rules-tips"},"Glob 表达式规则：",-1)})),R=K((function(){return l("div",{class:"rules-tips"},[l("b",null,"单层根目录加密"),b(": 如 "),l("span",{class:"select-all code"},"/"),b(" 表示根路径下需要密码访问.")],-1)})),X=K((function(){return l("div",{class:"rules-tips"},[l("b",null,"单层子目录加密"),b(": 如 "),l("span",{class:"select-all code"},"/music/*"),b(" 表示根目录下的 music 文件夹需要密码访问, 子文件夹不加密.")],-1)})),Y=K((function(){return l("div",{class:"rules-tips"},[l("b",null,"嵌套子目录加密"),b(": 如 "),l("span",{class:"select-all code"},"/music/**"),b(" 表示根目录下的 music 文件夹及其所有子文件夹都需要密码访问.")],-1)})),$=K((function(){return l("div",{class:"rules-tips"},"注：系统匹配到第一个符合的规则就会取密码进行校验，并返回结果，所以请调整好规则顺序，下方规则可进行拖拽排序。",-1)})),ee={class:"rules-tips-link"},ne={target:"_blank",class:"link",href:"http://www.ruanyifeng.com/blog/2018/09/bash-wildcards.html"},ae=K((function(){return l("span",null,"参考文章 (Wikipedia)",-1)})),te={target:"_blank",class:"link",href:"http://www.ruanyifeng.com/blog/2018/09/bash-wildcards.html"},ie={target:"_blank",class:"link",href:"https://github.com/whinc/blog/issues/18"},re={class:"sm:flex sm:space-x-2 sm:border-b-0 sm:pb-0 border-b pb-2"},oe={__name:"[storageId]",setup:function(e){var z=c(),V=u(),P=z.params.storageId,U=J(V,z),q=U.loading,B=U.loadPasswordData,H=U.passwordList,L=U.addPasswordItem,S=U.deletePasswordItem,Z=U.savePasswordData;d((function(){B(),W()}));var O=s(),W=function(){D(P).then((function(e){e.data.type=e.data.type.key,O.value=e.data}))};return function(e,s){var c=i,u=f("router-link"),d=t,z=A,V=a,P=n,U=F;return p((r(),g(C,{model:m(H),class:"zfile-admin-password-form"},{"form-title":v((function(){var e;return[l("div",N,[w(u,{to:"/admin/storage-list"},{default:v((function(){return[w(c,{class:"inline mr-2 cursor-pointer",name:"file-type-back"})]})),_:1}),l("span",null,"密码文件夹（"+h(null===(e=m(O))||void 0===e?void 0:e.name)+"）",1)])]})),"form-sub-title":v((function(){return[w(d,{closable:!1,type:"info"},{default:v((function(){return[Q,R,X,Y,$,l("div",ee,[l("a",ne,[w(m(y),{class:"inline mr-1"}),ae]),l("a",te,[w(m(y),{class:"inline mr-1"}),b("参考文章 (阮一峰)")]),l("a",ie,[w(m(y),{class:"inline mr-1"}),b("参考文章 (Github)")])])]})),_:1})]})),footer:v((function(){return[w(P,{type:"primary",size:"default",icon:m(M),onClick:m(Z)},{default:v((function(){return[b("保存设置")]})),_:1},8,["icon","onClick"])]})),default:v((function(){return[(r(!0),o(x,null,j(m(H),(function(e,n){return r(),g(E,{required:!0,key:e,class:"expression-item"},{default:v((function(){return[l("div",re,[w(V,{content:"此处可填写表达书描述，用于辅助记忆，防止时间过长后不知道表达式含义.",placement:"top"},{default:v((function(){return[w(z,{"prefix-icon":m(k),placeholder:"请输入表达式描述",modelValue:e.description,"onUpdate:modelValue":function(n){return e.description=n}},null,8,["prefix-icon","modelValue","onUpdate:modelValue"])]})),_:2},1024),w(z,{"prefix-icon":m(T),placeholder:"请输入表达式",modelValue:e.expression,"onUpdate:modelValue":function(n){return e.expression=n},modelModifiers:{trim:!0}},null,8,["prefix-icon","modelValue","onUpdate:modelValue"]),w(z,{"prefix-icon":m(G),placeholder:"请输入密码",type:"password","show-password":"",modelValue:e.password,"onUpdate:modelValue":function(n){return e.password=n}},null,8,["prefix-icon","modelValue","onUpdate:modelValue"]),w(P,{type:"danger",onClick:function(e){return m(S)(n)},icon:m(_)},null,8,["onClick","icon"])])]})),_:2},1024)})),128)),w(E,null,{default:v((function(){return[w(P,{type:"primary",size:"default",icon:m(I),onClick:m(L)},{default:v((function(){return[b("添加更多")]})),_:1},8,["icon","onClick"])]})),_:1})]})),_:1},8,["model"])),[[U,m(q)]])}}};"function"==typeof S&&S(oe);e("default",Z(oe,[["__scopeId","data-v-103a6950"]]))}}}))}();