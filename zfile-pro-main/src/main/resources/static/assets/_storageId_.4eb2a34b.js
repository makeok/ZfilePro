import{x as T}from"./base.9447ff7a.js";/* empty css                *//* empty css               */import"./tooltip.afcd3f9d.js";import{E as j}from"./popper.34e4046d.js";/* empty css              */import{E as A}from"./alert.73507778.js";import{_ as H}from"./SvgIcon.bb4e9d6f.js";import{a as h,f as D,y as e,r as S,aa as N,ac as $,o as G,Z as O,D as W,u as t,b as P,e as l,j as s,A as J,bu as y,z as n,O as K,P as Q,bv as X,a3 as Y,bg as ee,af as oe,ag as te}from"./index.a81894c3.js";import{Z as B,a as se}from"./ZFormItem.85c758c5.js";import{p as ae,q as le,i as re}from"./admin-storage.576b99eb.js";import{u as I}from"./common.3ffdf4de.js";import{S as ne}from"./sortable.esm.d7ef5fdd.js";import{E as ie}from"./request.88c6336a.js";import{E as de}from"./index.9f034646.js";import{b as C}from"./route-block.89f12ae7.js";import{_ as ce}from"./plugin-vue_export-helper.21dcd24c.js";import{r as pe}from"./CheckBadgeIcon.5c38551d.js";import{r as ue}from"./FolderIcon.c78a493a.js";import{E as me}from"./index.9dce3cd8.js";import{v as _e}from"./directive.8a34351b.js";import"./focus-trap.c21bbfc6.js";import"./index.b1a3ab43.js";import"./index.1d04ede7.js";import"./scroll.c1d55f52.js";import"./validator.4f1297a1.js";import"./event.807e9a54.js";function fe(d,m){return h(),D("svg",{xmlns:"http://www.w3.org/2000/svg",fill:"none",viewBox:"0 0 24 24","stroke-width":"1.5",stroke:"currentColor","aria-hidden":"true","data-slot":"icon"},[e("path",{"stroke-linecap":"round","stroke-linejoin":"round",d:"M15.75 5.25a3 3 0 0 1 3 3m3 0a6 6 0 0 1-7.029 5.912c-.563-.097-1.159.026-1.563.43L10.5 17.25H8.25v2.25H6v2.25H2.25v-2.818c0-.597.237-1.17.659-1.591l6.499-6.499c.404-.404.527-1 .43-1.563A6 6 0 1 1 21.75 8.25Z"})])}let a=S([]),E=S(!1);function ge(d,m){let _=m.params.storageId;const v=()=>{ae(_).then(i=>{a.value=i.data,a.value.length===0&&f(),w()})},w=()=>{const i=document.querySelector(".z-form-body");ne.create(i,{draggable:".expression-item",onEnd:o=>{if(o.oldIndex===o.newIndex)return;const c=a.value.splice(o.oldIndex-1,1)[0];a.value.splice(o.newIndex-1,0,c)}})},b=()=>{if(!a.value.find(o=>{if(I.isEmpty(o.description)||I.isEmpty(o.expression)||I.isEmpty(o.password))return ie.warning("\u8BF7\u68C0\u67E5\u6570\u636E\u586B\u5199\u662F\u5426\u5B8C\u6574"),!0})){E.value=!0;for(let o=0;o<a.value.length;o++){let c=a.value[o];c.id=o+1}le(_,a.value).then(()=>{de.confirm("\u4FDD\u5B58\u6210\u529F, \u662F\u5426\u8FD4\u56DE\u5B58\u50A8\u6E90\u5217\u8868\uFF1F","\u63D0\u793A",{confirmButtonText:"\u786E\u5B9A",cancelButtonText:"\u53D6\u6D88",type:"success",callback:o=>{o==="confirm"&&d.push("/admin/storage-list")}})}).finally(()=>{E.value=!1})}},f=()=>{a.value.push({description:"\u8868\u8FBE\u5F0F - "+(a.value.length+1),expression:"",password:"",storageId:_})};return{loading:E,loadPasswordData:v,passwordList:a,addPasswordItem:f,deletePasswordItem:i=>{a.value.splice(i,1)},savePasswordData:b}}const u=d=>(oe("data-v-103a6950"),d=d(),te(),d),he={class:"flex justify-items-center"},ve=u(()=>e("div",{class:"rules-tips"},"Glob \u8868\u8FBE\u5F0F\u89C4\u5219\uFF1A",-1)),we=u(()=>e("div",{class:"rules-tips"},[e("b",null,"\u5355\u5C42\u6839\u76EE\u5F55\u52A0\u5BC6"),n(": \u5982 "),e("span",{class:"select-all code"},"/"),n(" \u8868\u793A\u6839\u8DEF\u5F84\u4E0B\u9700\u8981\u5BC6\u7801\u8BBF\u95EE.")],-1)),be=u(()=>e("div",{class:"rules-tips"},[e("b",null,"\u5355\u5C42\u5B50\u76EE\u5F55\u52A0\u5BC6"),n(": \u5982 "),e("span",{class:"select-all code"},"/music/*"),n(" \u8868\u793A\u6839\u76EE\u5F55\u4E0B\u7684 music \u6587\u4EF6\u5939\u9700\u8981\u5BC6\u7801\u8BBF\u95EE, \u5B50\u6587\u4EF6\u5939\u4E0D\u52A0\u5BC6.")],-1)),xe=u(()=>e("div",{class:"rules-tips"},[e("b",null,"\u5D4C\u5957\u5B50\u76EE\u5F55\u52A0\u5BC6"),n(": \u5982 "),e("span",{class:"select-all code"},"/music/**"),n(" \u8868\u793A\u6839\u76EE\u5F55\u4E0B\u7684 music \u6587\u4EF6\u5939\u53CA\u5176\u6240\u6709\u5B50\u6587\u4EF6\u5939\u90FD\u9700\u8981\u5BC6\u7801\u8BBF\u95EE.")],-1)),ke=u(()=>e("div",{class:"rules-tips"},"\u6CE8\uFF1A\u7CFB\u7EDF\u5339\u914D\u5230\u7B2C\u4E00\u4E2A\u7B26\u5408\u7684\u89C4\u5219\u5C31\u4F1A\u53D6\u5BC6\u7801\u8FDB\u884C\u6821\u9A8C\uFF0C\u5E76\u8FD4\u56DE\u7ED3\u679C\uFF0C\u6240\u4EE5\u8BF7\u8C03\u6574\u597D\u89C4\u5219\u987A\u5E8F\uFF0C\u4E0B\u65B9\u89C4\u5219\u53EF\u8FDB\u884C\u62D6\u62FD\u6392\u5E8F\u3002",-1)),ye={class:"rules-tips-link"},Ie={target:"_blank",class:"link",href:"http://www.ruanyifeng.com/blog/2018/09/bash-wildcards.html"},Ee=u(()=>e("span",null,"\u53C2\u8003\u6587\u7AE0 (Wikipedia)",-1)),Se={target:"_blank",class:"link",href:"http://www.ruanyifeng.com/blog/2018/09/bash-wildcards.html"},Ve={target:"_blank",class:"link",href:"https://github.com/whinc/blog/issues/18"},Pe={class:"sm:flex sm:space-x-2 sm:border-b-0 sm:pb-0 border-b pb-2"},q={__name:"[storageId]",setup(d){let m=N(),_=$(),v=m.params.storageId;const{loading:w,loadPasswordData:b,passwordList:f,addPasswordItem:V,deletePasswordItem:i,savePasswordData:o}=ge(_,m);G(()=>{b(),R()});const c=S(),R=()=>{re(v).then(g=>{g.data.type=g.data.type.key,c.value=g.data})};return(g,Be)=>{const U=H,z=O("router-link"),F=A,x=me,L=j,k=T,M=_e;return W((h(),P(se,{model:t(f),class:"zfile-admin-password-form"},{"form-title":l(()=>{var r;return[e("div",he,[s(z,{to:"/admin/storage-list"},{default:l(()=>[s(U,{class:"inline mr-2 cursor-pointer",name:"file-type-back"})]),_:1}),e("span",null,"\u5BC6\u7801\u6587\u4EF6\u5939\uFF08"+J((r=t(c))==null?void 0:r.name)+"\uFF09",1)])]}),"form-sub-title":l(()=>[s(F,{closable:!1,type:"info"},{default:l(()=>[ve,we,be,xe,ke,e("div",ye,[e("a",Ie,[s(t(y),{class:"inline mr-1"}),Ee]),e("a",Se,[s(t(y),{class:"inline mr-1"}),n("\u53C2\u8003\u6587\u7AE0 (\u962E\u4E00\u5CF0)")]),e("a",Ve,[s(t(y),{class:"inline mr-1"}),n("\u53C2\u8003\u6587\u7AE0 (Github)")])])]),_:1})]),footer:l(()=>[s(k,{type:"primary",size:"default",icon:t(pe),onClick:t(o)},{default:l(()=>[n("\u4FDD\u5B58\u8BBE\u7F6E")]),_:1},8,["icon","onClick"])]),default:l(()=>[(h(!0),D(K,null,Q(t(f),(r,Z)=>(h(),P(B,{required:!0,key:r,class:"expression-item"},{default:l(()=>[e("div",Pe,[s(L,{content:"\u6B64\u5904\u53EF\u586B\u5199\u8868\u8FBE\u4E66\u63CF\u8FF0\uFF0C\u7528\u4E8E\u8F85\u52A9\u8BB0\u5FC6\uFF0C\u9632\u6B62\u65F6\u95F4\u8FC7\u957F\u540E\u4E0D\u77E5\u9053\u8868\u8FBE\u5F0F\u542B\u4E49.",placement:"top"},{default:l(()=>[s(x,{"prefix-icon":t(X),placeholder:"\u8BF7\u8F93\u5165\u8868\u8FBE\u5F0F\u63CF\u8FF0",modelValue:r.description,"onUpdate:modelValue":p=>r.description=p},null,8,["prefix-icon","modelValue","onUpdate:modelValue"])]),_:2},1024),s(x,{"prefix-icon":t(ue),placeholder:"\u8BF7\u8F93\u5165\u8868\u8FBE\u5F0F",modelValue:r.expression,"onUpdate:modelValue":p=>r.expression=p,modelModifiers:{trim:!0}},null,8,["prefix-icon","modelValue","onUpdate:modelValue"]),s(x,{"prefix-icon":t(fe),placeholder:"\u8BF7\u8F93\u5165\u5BC6\u7801",type:"password","show-password":"",modelValue:r.password,"onUpdate:modelValue":p=>r.password=p},null,8,["prefix-icon","modelValue","onUpdate:modelValue"]),s(k,{type:"danger",onClick:p=>t(i)(Z),icon:t(Y)},null,8,["onClick","icon"])])]),_:2},1024))),128)),s(B,null,{default:l(()=>[s(k,{type:"primary",size:"default",icon:t(ee),onClick:t(V)},{default:l(()=>[n("\u6DFB\u52A0\u66F4\u591A")]),_:1},8,["icon","onClick"])]),_:1})]),_:1},8,["model"])),[[M,t(w)]])}}};typeof C=="function"&&C(q);var so=ce(q,[["__scopeId","data-v-103a6950"]]);export{so as default};