import{x as S}from"./base.9447ff7a.js";import{E as B,a as D}from"./form-item.c881aa55.js";/* empty css               */import"./tooltip.afcd3f9d.js";import{E as N}from"./popper.34e4046d.js";import{E as j}from"./link.78f066a7.js";import{E as F}from"./image-viewer.adc9414e.js";/* empty css              */import{a as p,f as V,y as l,ac as L,r as d,o as Z,j as o,e as s,u as e,b8 as $,b9 as T,b as y,I as A,l as H,z as x,ba as J,h as O,bb as G,af as K,ag as P}from"./index.a81894c3.js";import{_ as Q}from"./zfile-basic.8b129fdf.js";import{l as W,c as X,a as Y,b as ee}from"./login.ca9e5137.js";import{i as te}from"./install.0554d8be.js";import{_ as ae}from"./plugin-vue_export-helper.21dcd24c.js";import{r as oe}from"./CheckBadgeIcon.5c38551d.js";import{E as k}from"./request.88c6336a.js";import{E as le}from"./index.9dce3cd8.js";import"./castArray.7c80ce00.js";import"./index.b1a3ab43.js";import"./_Uint8Array.25475a91.js";import"./_initCloneObject.df1878fc.js";import"./focus-trap.c21bbfc6.js";import"./debounce.ff28d2f1.js";import"./scroll.c1d55f52.js";import"./event.807e9a54.js";function ie(m,f){return p(),V("svg",{xmlns:"http://www.w3.org/2000/svg",fill:"none",viewBox:"0 0 24 24","stroke-width":"1.5",stroke:"currentColor","aria-hidden":"true","data-slot":"icon"},[l("path",{"stroke-linecap":"round","stroke-linejoin":"round",d:"m2.25 15.75 5.159-5.159a2.25 2.25 0 0 1 3.182 0l5.159 5.159m-1.5-1.5 1.409-1.409a2.25 2.25 0 0 1 3.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 0 0 1.5-1.5V6a1.5 1.5 0 0 0-1.5-1.5H3.75A1.5 1.5 0 0 0 2.25 6v12a1.5 1.5 0 0 0 1.5 1.5Zm10.5-11.25h.008v.008h-.008V8.25Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"})])}const v=m=>(K("data-v-50a55352"),m=m(),P(),m),se={class:"h-full min-h-screen bg-gray-100 text-gray-900 flex justify-center items-center"},re={class:"h-full lg:h-auto max-w-screen-lg bg-white shadow-xl sm:rounded-xl flex flex-1"},ne=v(()=>l("div",{class:"flex-1 bg-gray-50 text-center hidden lg:flex rounded-l-xl"},[l("div",{class:"m-12 xl:m-16 w-full bg-contain bg-center bg-no-repeat zfile-login-logo"})],-1)),de={class:"w-full lg:w-1/2 xl:w-1/2 mt-10 p-6 sm:p-12"},me={class:"mt-0 flex flex-col items-center"},fe={class:"text-2xl mt-0 xl:text-3xl font-extrabold box animate__animated animate__fadeInDown"},ue={class:"w-full flex-1"},pe=G('<div class="my-12 relative" data-v-50a55352><div class="absolute inset-0 flex items-center" aria-hidden="true" data-v-50a55352><div class="w-full border-t text-gray-600" data-v-50a55352></div></div><div class="relative flex justify-center text-sm" data-v-50a55352><span class="px-2 bg-white text-sm leading-none text-gray-600 tracking-wide font-medium" data-v-50a55352>\u8D26\u53F7\u767B\u5F55</span></div></div>',1),ce={class:"flex space-x-5 w-full"},_e=v(()=>l("br",null,null,-1)),ge=v(()=>l("a",{target:"_blank",class:"text-blue-400",href:"https://docs.zfile.vip/question#reset-pwd"},"\u70B9\u51FB\u53EF\u524D\u5F80\u6587\u6863\u67E5\u770B\u64CD\u4F5C\u65B9\u5F0F",-1)),xe={__name:"login",setup(m){let f=L(),c=d(!1),h=d(),i=d({username:"",password:"",verifyCode:"",verifyCodeUUID:""});const I=a=>{window.open(a)};let C=d({username:[{required:!0,message:"\u8D26\u53F7\u4E0D\u80FD\u4E3A\u7A7A",trigger:["change","blur"]}],password:[{required:!0,message:"\u5BC6\u7801\u4E0D\u80FD\u4E3A\u7A7A",trigger:["change","blur"]}]});const E=()=>{h.value.validate(a=>{a?(c.value=!0,Y(i.value).then(t=>{window.localStorage.setItem("zfile-token",t.data.token),window.localStorage.setItem("zfile-user",JSON.stringify(t.data.user)),k({message:"\u767B\u5F55\u6210\u529F",type:"success",duration:1e3,onClose(){f.push("/")}})}).catch(()=>{c.value=!1,g()})):k.warning("\u8BF7\u8F93\u5165\u8D26\u53F7\u5BC6\u7801!")})},_=d("");W().then(a=>{_.value=a.data});const w=d({}),g=()=>{ee().then(a=>{w.value=a.data,i.value.verifyCodeUUID=a.data.uuid})};return g(),Z(()=>{te().then(a=>{a.data||f.push("/install")}),X().then(a=>{a.data&&f.push("/admin")})}),(a,t)=>{const u=le,n=B,U=F,z=j,q=N,M=S,R=D;return p(),V("div",se,[l("div",re,[ne,l("div",de,[l("div",me,[l("h1",fe,[l("img",{class:"w-16 cursor-pointer",onClick:t[0]||(t[0]=r=>I("https://github.com/zhaojun1998/zfile")),src:Q})]),l("div",ue,[pe,o(R,{onSubmit:t[6]||(t[6]=O(()=>{},["prevent"])),ref_key:"loginFormRef",ref:h,rules:e(C),model:e(i),class:"zfile-login-form",size:"large"},{default:s(()=>[o(n,{class:"box animate__animated animate__fadeInUp",prop:"username"},{default:s(()=>[o(u,{placeholder:"\u8D26\u53F7","prefix-icon":e($),modelValue:e(i).username,"onUpdate:modelValue":t[1]||(t[1]=r=>e(i).username=r),modelModifiers:{trim:!0}},null,8,["prefix-icon","modelValue"])]),_:1}),o(n,{class:"box animate__animated animate__fadeInUp",prop:"password"},{default:s(()=>[o(u,{placeholder:"\u5BC6\u7801",type:"password","show-password":"","prefix-icon":e(T),modelValue:e(i).password,"onUpdate:modelValue":t[2]||(t[2]=r=>e(i).password=r),modelModifiers:{trim:!0}},null,8,["prefix-icon","modelValue"])]),_:1}),e(_)==="2fa"?(p(),y(n,{key:0,class:"box animate__animated animate__fadeInUp",prop:"verifyCode"},{default:s(()=>[o(u,{placeholder:"\u53CC\u56E0\u7D20\u8BA4\u8BC1\u9A8C\u8BC1\u7801","prefix-icon":e(A),modelValue:e(i).verifyCode,"onUpdate:modelValue":t[3]||(t[3]=r=>e(i).verifyCode=r),modelModifiers:{trim:!0}},null,8,["prefix-icon","modelValue"])]),_:1})):e(_)==="image"?(p(),y(n,{key:1,class:"box animate__animated animate__fadeInUp",prop:"verifyCode"},{default:s(()=>{var r;return[l("div",ce,[o(u,{class:"flex-1",placeholder:"\u8BF7\u8F93\u5165\u9A8C\u8BC1\u7801","prefix-icon":e(ie),modelValue:e(i).verifyCode,"onUpdate:modelValue":t[4]||(t[4]=b=>e(i).verifyCode=b),modelModifiers:{trim:!0}},null,8,["prefix-icon","modelValue"]),o(U,{src:(r=e(w))==null?void 0:r.imgBase64,onClick:t[5]||(t[5]=b=>g())},null,8,["src"])])]}),_:1})):H("",!0),o(n,{class:"box animate__animated animate__fadeInUp float-right"},{default:s(()=>[o(q,{class:"item",effect:"dark",placement:"left"},{content:s(()=>[x(" \u5C06\u914D\u7F6E\u6587\u4EF6 application.properties \u4E2D zfile.debug \u4FEE\u6539\u4E3A true, \u91CD\u542F\u540E\u8BBF\u95EE\u9996\u9875\u5373\u53EF\u91CD\u7F6E\u5BC6\u7801 "),_e,ge]),default:s(()=>[o(z,{icon:e(J),underline:!1,class:"zfile-float-right"},{default:s(()=>[x("\u5FD8\u8BB0\u5BC6\u7801")]),_:1},8,["icon"])]),_:1})]),_:1}),o(n,{class:"clear-right"},{default:s(()=>[o(M,{loading:e(c),class:"w-full mb-0","native-type":"submit",type:"primary",icon:e(oe),onClick:E},{default:s(()=>[x("\u767B\u5F55")]),_:1},8,["loading","icon"])]),_:1})]),_:1},8,["rules","model"])])])])])])}}};var Te=ae(xe,[["__scopeId","data-v-50a55352"]]);export{Te as default};
