import{x as b}from"./base.9447ff7a.js";import{E as v}from"./dialog.8e75b1b4.js";import"./overlay.e553b113.js";/* empty css               */import{E as x,a as C}from"./form-item.c881aa55.js";/* empty css              */import{a as k,b as F,e as l,j as m,z as s,u as e,h as N,ae as w}from"./index.a81894c3.js";import{u as B}from"./storage-copy.b5f50f89.js";import{E as K}from"./index.9dce3cd8.js";import"./index.1d04ede7.js";import"./scroll.c1d55f52.js";import"./focus-trap.c21bbfc6.js";import"./refs.7263a917.js";import"./event.807e9a54.js";import"./castArray.7c80ce00.js";import"./index.b1a3ab43.js";import"./request.88c6336a.js";import"./_Uint8Array.25475a91.js";import"./_initCloneObject.df1878fc.js";import"./admin-storage.576b99eb.js";const Y={__name:"index",setup(R,{emit:u}){const{dialogVisible:a,saveLoading:d,formData:r,rules:f,storageCopyFormRef:_,submitForm:V}=B(),g=u;return(U,o)=>{const p=K,i=x,y=C,n=b,E=v;return k(),F(E,{title:"\u590D\u5236\u5B58\u50A8\u914D\u7F6E",modelValue:e(a),"onUpdate:modelValue":o[5]||(o[5]=t=>w(a)?a.value=t:null),width:"20%"},{footer:l(()=>[m(n,{onClick:o[3]||(o[3]=t=>a.value=!1)},{default:l(()=>[s("\u53D6 \u6D88")]),_:1}),m(n,{type:"primary",onClick:o[4]||(o[4]=t=>e(V)(g)),loading:e(d)},{default:l(()=>[s("\u786E \u5B9A")]),_:1},8,["loading"])]),default:l(()=>[m(y,{onSubmit:o[2]||(o[2]=N(()=>{},["prevent"])),model:e(r),rules:e(f),ref_key:"storageCopyFormRef",ref:_},{default:l(()=>[m(i,{label:"\u65B0\u5B58\u50A8\u6E90\u540D\u79F0",prop:"toName"},{default:l(()=>[m(p,{modelValue:e(r).toName,"onUpdate:modelValue":o[0]||(o[0]=t=>e(r).toName=t),placeholder:"\u8BF7\u8F93\u5165\u65B0\u5B58\u50A8\u6E90\u540D\u79F0"},null,8,["modelValue"])]),_:1}),m(i,{label:"\u65B0\u5B58\u50A8\u6E90\u522B\u540D",prop:"toKey"},{default:l(()=>[m(p,{modelValue:e(r).toKey,"onUpdate:modelValue":o[1]||(o[1]=t=>e(r).toKey=t),placeholder:"\u8BF7\u8F93\u5165\u65B0\u5B58\u50A8\u6E90\u522B\u540D"},null,8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue"])}}};export{Y as default};