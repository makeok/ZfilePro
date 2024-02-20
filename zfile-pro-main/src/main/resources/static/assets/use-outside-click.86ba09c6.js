var W=Object.defineProperty,K=Object.defineProperties;var V=Object.getOwnPropertyDescriptors;var w=Object.getOwnPropertySymbols;var j=Object.prototype.hasOwnProperty,A=Object.prototype.propertyIsEnumerable;var $=(e,t,n)=>t in e?W(e,t,{enumerable:!0,configurable:!0,writable:!0,value:n}):e[t]=n,b=(e,t)=>{for(var n in t||(t={}))j.call(t,n)&&$(e,n,t[n]);if(w)for(var n of w(t))A.call(t,n)&&$(e,n,t[n]);return e},E=(e,t)=>K(e,V(t));var O=(e,t)=>{var n={};for(var r in e)j.call(e,r)&&t.indexOf(r)<0&&(n[r]=e[r]);if(e!=null&&w)for(var r of w(e))t.indexOf(r)<0&&A.call(e,r)&&(n[r]=e[r]);return n};import{v as L,L as F}from"./directive.8a34351b.js";import{a as G,f as q,y as X,aJ as J,U as Y,O as z,q as Q,p as Z,W as ee,aF as T,r as te,c as ne}from"./index.a81894c3.js";const je={install(e){e.directive("loading",L),e.config.globalProperties.$loading=F},directive:L,service:F};function Ae(e,t){return G(),q("svg",{xmlns:"http://www.w3.org/2000/svg",fill:"none",viewBox:"0 0 24 24","stroke-width":"1.5",stroke:"currentColor","aria-hidden":"true","data-slot":"icon"},[X("path",{"stroke-linecap":"round","stroke-linejoin":"round",d:"M6 18 18 6M6 6l12 12"})])}function x(e,t,...n){if(e in t){let o=t[e];return typeof o=="function"?o(...n):o}let r=new Error(`Tried to handle "${e}" but there is no handler defined. Only defined handlers are: ${Object.keys(t).map(o=>`"${o}"`).join(", ")}.`);throw Error.captureStackTrace&&Error.captureStackTrace(r,x),r}var re=(e=>(e[e.None=0]="None",e[e.RenderStrategy=1]="RenderStrategy",e[e.Static=2]="Static",e))(re||{}),oe=(e=>(e[e.Unmount=0]="Unmount",e[e.Hidden=1]="Hidden",e))(oe||{});function Le(l){var u=l,{visible:e=!0,features:t=0,ourProps:n,theirProps:r}=u,o=O(u,["visible","features","ourProps","theirProps"]);var i;let c=M(r,n),a=Object.assign(o,{props:c});if(e||t&2&&c.static)return N(a);if(t&1){let s=(i=c.unmount)==null||i?0:1;return x(s,{[0](){return null},[1](){return N(E(b({},o),{props:E(b({},c),{hidden:!0,style:{display:"none"}})}))}})}return N(a)}function N({props:e,attrs:t,slots:n,slot:r,name:o}){var l,u;let v=le(e,["unmount","static"]),{as:i}=v,c=O(v,["as"]),a=(l=n.default)==null?void 0:l.call(n,r),s={};if(r){let p=!1,m=[];for(let[d,h]of Object.entries(r))typeof h=="boolean"&&(p=!0),h===!0&&m.push(d);p&&(s["data-headlessui-state"]=m.join(" "))}if(i==="template"){if(a=I(a!=null?a:[]),Object.keys(c).length>0||Object.keys(t).length>0){let[p,...m]=a!=null?a:[];if(!ie(p)||m.length>0)throw new Error(['Passing props on "template"!',"",`The current component <${o} /> is rendering a "template".`,"However we need to passthrough the following props:",Object.keys(c).concat(Object.keys(t)).map(f=>f.trim()).filter((f,y,B)=>B.indexOf(f)===y).sort((f,y)=>f.localeCompare(y)).map(f=>`  - ${f}`).join(`
`),"","You can apply a few solutions:",['Add an `as="..."` prop, to ensure that we render an actual element instead of a "template".',"Render a single element as the child so that we can forward the props onto that element."].map(f=>`  - ${f}`).join(`
`)].join(`
`));let d=M((u=p.props)!=null?u:{},c),h=J(p,d);for(let f in d)f.startsWith("on")&&(h.props||(h.props={}),h.props[f]=d[f]);return h}return Array.isArray(a)&&a.length===1?a[0]:a}return Y(i,Object.assign({},c,s),{default:()=>a})}function I(e){return e.flatMap(t=>t.type===z?I(t.children):[t])}function M(...e){if(e.length===0)return{};if(e.length===1)return e[0];let t={},n={};for(let r of e)for(let o in r)o.startsWith("on")&&typeof r[o]=="function"?(n[o]!=null||(n[o]=[]),n[o].push(r[o])):t[o]=r[o];if(t.disabled||t["aria-disabled"])return Object.assign(t,Object.fromEntries(Object.keys(n).map(r=>[r,void 0])));for(let r in n)Object.assign(t,{[r](o,...l){let u=n[r];for(let i of u){if(o instanceof Event&&o.defaultPrevented)return;i(o,...l)}}});return t}function le(e,t=[]){let n=Object.assign({},e);for(let r of t)r in n&&delete n[r];return n}function ie(e){return e==null?!1:typeof e.type=="string"||typeof e.type=="object"||typeof e.type=="function"}let ae=0;function ue(){return++ae}function Fe(){return ue()}var se=(e=>(e.Space=" ",e.Enter="Enter",e.Escape="Escape",e.Backspace="Backspace",e.Delete="Delete",e.ArrowLeft="ArrowLeft",e.ArrowUp="ArrowUp",e.ArrowRight="ArrowRight",e.ArrowDown="ArrowDown",e.Home="Home",e.End="End",e.PageUp="PageUp",e.PageDown="PageDown",e.Tab="Tab",e))(se||{});function D(e){var t;return e==null||e.value==null?null:(t=e.value.$el)!=null?t:e.value}let C=Symbol("Context");var ce=(e=>(e[e.Open=1]="Open",e[e.Closed=2]="Closed",e[e.Closing=4]="Closing",e[e.Opening=8]="Opening",e))(ce||{});function ke(){return de()!==null}function de(){return Q(C,null)}function Te(e){Z(C,e)}var fe=Object.defineProperty,pe=(e,t,n)=>t in e?fe(e,t,{enumerable:!0,configurable:!0,writable:!0,value:n}):e[t]=n,k=(e,t,n)=>(pe(e,typeof t!="symbol"?t+"":t,n),n);class me{constructor(){k(this,"current",this.detect()),k(this,"currentId",0)}set(t){this.current!==t&&(this.currentId=0,this.current=t)}reset(){this.set(this.detect())}nextId(){return++this.currentId}get isServer(){return this.current==="server"}get isClient(){return this.current==="client"}detect(){return typeof window=="undefined"||typeof document=="undefined"?"server":"client"}}let P=new me;function H(e){if(P.isServer)return null;if(e instanceof Node)return e.ownerDocument;if(e!=null&&e.hasOwnProperty("value")){let t=D(e);if(t)return t.ownerDocument}return document}let S=["[contentEditable=true]","[tabindex]","a[href]","area[href]","button:not([disabled])","iframe","input:not([disabled])","select:not([disabled])","textarea:not([disabled])"].map(e=>`${e}:not([tabindex='-1'])`).join(",");var ve=(e=>(e[e.First=1]="First",e[e.Previous=2]="Previous",e[e.Next=4]="Next",e[e.Last=8]="Last",e[e.WrapAround=16]="WrapAround",e[e.NoScroll=32]="NoScroll",e))(ve||{}),he=(e=>(e[e.Error=0]="Error",e[e.Overflow=1]="Overflow",e[e.Success=2]="Success",e[e.Underflow=3]="Underflow",e))(he||{}),we=(e=>(e[e.Previous=-1]="Previous",e[e.Next=1]="Next",e))(we||{});function U(e=document.body){return e==null?[]:Array.from(e.querySelectorAll(S)).sort((t,n)=>Math.sign((t.tabIndex||Number.MAX_SAFE_INTEGER)-(n.tabIndex||Number.MAX_SAFE_INTEGER)))}var _=(e=>(e[e.Strict=0]="Strict",e[e.Loose=1]="Loose",e))(_||{});function R(e,t=0){var n;return e===((n=H(e))==null?void 0:n.body)?!1:x(t,{[0](){return e.matches(S)},[1](){let r=e;for(;r!==null;){if(r.matches(S))return!0;r=r.parentElement}return!1}})}function Ie(e){let t=H(e);ee(()=>{t&&!R(t.activeElement,0)&&ye(e)})}var ge=(e=>(e[e.Keyboard=0]="Keyboard",e[e.Mouse=1]="Mouse",e))(ge||{});typeof window!="undefined"&&typeof document!="undefined"&&(document.addEventListener("keydown",e=>{e.metaKey||e.altKey||e.ctrlKey||(document.documentElement.dataset.headlessuiFocusVisible="")},!0),document.addEventListener("click",e=>{e.detail===1?delete document.documentElement.dataset.headlessuiFocusVisible:e.detail===0&&(document.documentElement.dataset.headlessuiFocusVisible="")},!0));function ye(e){e==null||e.focus({preventScroll:!0})}let be=["textarea","input"].join(",");function Ee(e){var t,n;return(n=(t=e==null?void 0:e.matches)==null?void 0:t.call(e,be))!=null?n:!1}function Oe(e,t=n=>n){return e.slice().sort((n,r)=>{let o=t(n),l=t(r);if(o===null||l===null)return 0;let u=o.compareDocumentPosition(l);return u&Node.DOCUMENT_POSITION_FOLLOWING?-1:u&Node.DOCUMENT_POSITION_PRECEDING?1:0})}function Me(e,t){return Ne(U(),t,{relativeTo:e})}function Ne(e,t,{sorted:n=!0,relativeTo:r=null,skipElements:o=[]}={}){var l;let u=(l=Array.isArray(e)?e.length>0?e[0].ownerDocument:document:e==null?void 0:e.ownerDocument)!=null?l:document,i=Array.isArray(e)?n?Oe(e):e:U(e);o.length>0&&i.length>1&&(i=i.filter(d=>!o.includes(d))),r=r!=null?r:u.activeElement;let c=(()=>{if(t&5)return 1;if(t&10)return-1;throw new Error("Missing Focus.First, Focus.Previous, Focus.Next or Focus.Last")})(),a=(()=>{if(t&1)return 0;if(t&2)return Math.max(0,i.indexOf(r))-1;if(t&4)return Math.max(0,i.indexOf(r))+1;if(t&8)return i.length-1;throw new Error("Missing Focus.First, Focus.Previous, Focus.Next or Focus.Last")})(),s=t&32?{preventScroll:!0}:{},v=0,p=i.length,m;do{if(v>=p||v+p<=0)return 0;let d=a+v;if(t&16)d=(d+p)%p;else{if(d<0)return 3;if(d>=p)return 1}m=i[d],m==null||m.focus(s),v+=c}while(m!==u.activeElement);return t&6&&Ee(m)&&m.select(),2}function g(e,t,n){P.isServer||T(r=>{document.addEventListener(e,t,n),r(()=>document.removeEventListener(e,t,n))})}function Se(e,t,n){P.isServer||T(r=>{window.addEventListener(e,t,n),r(()=>window.removeEventListener(e,t,n))})}function De(e,t,n=ne(()=>!0)){function r(l,u){if(!n.value||l.defaultPrevented)return;let i=u(l);if(i===null||!i.getRootNode().contains(i))return;let c=function a(s){return typeof s=="function"?a(s()):Array.isArray(s)||s instanceof Set?s:[s]}(e);for(let a of c){if(a===null)continue;let s=a instanceof HTMLElement?a:D(a);if(s!=null&&s.contains(i)||l.composed&&l.composedPath().includes(s))return}return!R(i,_.Loose)&&i.tabIndex!==-1&&l.preventDefault(),t(l,i)}let o=te(null);g("pointerdown",l=>{var u,i;n.value&&(o.value=((i=(u=l.composedPath)==null?void 0:u.call(l))==null?void 0:i[0])||l.target)},!0),g("mousedown",l=>{var u,i;n.value&&(o.value=((i=(u=l.composedPath)==null?void 0:u.call(l))==null?void 0:i[0])||l.target)},!0),g("click",l=>{o.value&&(r(l,()=>o.value),o.value=null)},!0),g("touchend",l=>r(l,()=>l.target instanceof HTMLElement?l.target:null),!0),Se("blur",l=>r(l,()=>window.document.activeElement instanceof HTMLIFrameElement?window.document.activeElement:null),!0)}export{ke as C,je as E,Le as H,ve as N,Oe as O,Ne as P,ye as S,he as T,Ie as _,se as a,re as b,P as c,oe as d,Te as e,le as f,R as g,_ as h,ce as l,H as m,D as o,de as p,Ae as r,Fe as t,x as u,Me as v,Se as w,De as y};
