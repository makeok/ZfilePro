var V=Object.defineProperty,$=Object.defineProperties;var z=Object.getOwnPropertyDescriptors;var b=Object.getOwnPropertySymbols;var M=Object.prototype.hasOwnProperty,O=Object.prototype.propertyIsEnumerable;var C=(l,t,a)=>t in l?V(l,t,{enumerable:!0,configurable:!0,writable:!0,value:a}):l[t]=a,g=(l,t)=>{for(var a in t||(t={}))M.call(t,a)&&C(l,a,t[a]);if(b)for(var a of b(t))O.call(t,a)&&C(l,a,t[a]);return l},E=(l,t)=>$(l,z(t));import{d as w,az as P,r as j,c as d,a as n,b as r,e as f,D as F,y as h,g as i,u as e,m as _,l as c,f as p,i as S,z as T,A as u,O as q,j as G,E as H,T as J}from"./index.a81894c3.js";import{b as K,a6 as L,a7 as N,a8 as Q,a as R,E as B,_ as U,w as W}from"./base.9447ff7a.js";const X=["light","dark"],Y=K({title:{type:String,default:""},description:{type:String,default:""},type:{type:String,values:L(N),default:"info"},closable:{type:Boolean,default:!0},closeText:{type:String,default:""},showIcon:Boolean,center:Boolean,effect:{type:String,values:X,default:"light"}}),Z={close:l=>l instanceof MouseEvent},x=w({name:"ElAlert"}),ee=w(E(g({},x),{props:Y,emits:Z,setup(l,{emit:t}){const a=l,{Close:A}=Q,m=P(),o=R("alert"),y=j(!0),v=d(()=>N[a.type]),D=d(()=>[o.e("icon"),{[o.is("big")]:!!a.description||!!m.default}]),I=d(()=>({[o.is("bold")]:a.description||m.default})),k=s=>{y.value=!1,t("close",s)};return(s,te)=>(n(),r(J,{name:e(o).b("fade"),persisted:""},{default:f(()=>[F(h("div",{class:i([e(o).b(),e(o).m(s.type),e(o).is("center",s.center),e(o).is(s.effect)]),role:"alert"},[s.showIcon&&e(v)?(n(),r(e(B),{key:0,class:i(e(D))},{default:f(()=>[(n(),r(_(e(v))))]),_:1},8,["class"])):c("v-if",!0),h("div",{class:i(e(o).e("content"))},[s.title||s.$slots.title?(n(),p("span",{key:0,class:i([e(o).e("title"),e(I)])},[S(s.$slots,"title",{},()=>[T(u(s.title),1)])],2)):c("v-if",!0),s.$slots.default||s.description?(n(),p("p",{key:1,class:i(e(o).e("description"))},[S(s.$slots,"default",{},()=>[T(u(s.description),1)])],2)):c("v-if",!0),s.closable?(n(),p(q,{key:2},[s.closeText?(n(),p("div",{key:0,class:i([e(o).e("close-btn"),e(o).is("customed")]),onClick:k},u(s.closeText),3)):(n(),r(e(B),{key:1,class:i(e(o).e("close-btn")),onClick:k},{default:f(()=>[G(e(A))]),_:1},8,["class"]))],64)):c("v-if",!0)],2)],2),[[H,y.value]])]),_:3},8,["name"]))}}));var se=U(ee,[["__file","alert.vue"]]);const ne=W(se);export{ne as E};
