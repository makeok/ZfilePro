import"./base.9447ff7a.js";import{a as C}from"./table.7b0ce08e.js";import"./checkbox.adc9a1be.js";import"./tooltip.afcd3f9d.js";import"./popper.34e4046d.js";import"./scrollbar.b58c5e8b.js";import{Z as b,a as h}from"./ZFormItem.85c758c5.js";import{E as g}from"./switch.0dde4485.js";import{i as v}from"./admin-storage.576b99eb.js";import{u as R}from"./storage-list.52ac7593.js";import{ac as w,r as x,o as E,a as i,f as S,u as o,b as k,e as m,z,j as r,ae as B,l as I}from"./index.a81894c3.js";import"./_Uint8Array.25475a91.js";import"./_initCloneObject.df1878fc.js";import"./index.3b295ed4.js";import"./debounce.ff28d2f1.js";import"./request.88c6336a.js";import"./event.807e9a54.js";import"./focus-trap.c21bbfc6.js";import"./index.b1a3ab43.js";import"./plugin-vue_export-helper.21dcd24c.js";import"./validator.4f1297a1.js";import"./sortable.esm.d7ef5fdd.js";import"./useCommon.373cb8ed.js";import"./index.9b197a07.js";import"./storage-copy.b5f50f89.js";import"./index.9f034646.js";import"./index.9dce3cd8.js";import"./index.1d04ede7.js";import"./scroll.c1d55f52.js";const se={__name:"index",setup(M){let p=w();const{cacheManageVisible:N,switchCacheEnableStatus:u,switchCacheAutoRefreshStatus:d,currentCacheManageId:f}=R(p),e=x(null),c=()=>{v(f.value).then(l=>{e.value=l.data})};return E(()=>{c()}),(l,t)=>{const n=g,s=b,_=h,V=C;return i(),S("div",null,[o(e)?(i(),k(_,{key:0,modelValue:o(e),"onUpdate:modelValue":t[4]||(t[4]=a=>B(e)?e.value=a:null)},{"form-sub-title":m(()=>[z(" \u6B64\u9875\u9762\u53EF\u4EE5\u7BA1\u7406\u60A8\u7684\u7F13\u5B58\u76F8\u5173\u8BBE\u7F6E\uFF0C\u5305\u62EC\u7F13\u5B58\u7684\u7BA1\u7406\u3001\u7F13\u5B58\u7684\u6E05\u7406\u3001\u7F13\u5B58\u7684\u7EDF\u8BA1\u7B49\u3002 ")]),default:m(()=>[r(s,{label:"\u5F00\u542F\u7F13\u5B58"},{default:m(()=>[r(n,{onChange:t[0]||(t[0]=a=>o(u)(o(e))),modelValue:o(e).enableCache,"onUpdate:modelValue":t[1]||(t[1]=a=>o(e).enableCache=a)},null,8,["modelValue"])]),_:1}),r(s,{label:"\u5F00\u542F\u7F13\u5B58\u81EA\u52A8\u5237\u65B0"},{default:m(()=>[r(n,{onChange:t[2]||(t[2]=a=>o(d)(o(e))),modelValue:o(e).autoRefreshCache,"onUpdate:modelValue":t[3]||(t[3]=a=>o(e).autoRefreshCache=a)},null,8,["modelValue"])]),_:1})]),_:1},8,["modelValue"])):I("",!0),r(V)])}}};export{se as default};
