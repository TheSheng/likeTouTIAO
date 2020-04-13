

/* tab 扫码二维码登录方式 */

var regTop=document.getElementById('reg-top');
var normal=document.getElementById('normal');
var nopw=document.getElementById('nopw');
var saoma=document.getElementById('qrcode');
var screen=document.getElementById('screen');
var rc=document.getElementById('rc');
var lc=document.getElementById('lc');
var sm=document.getElementById('sm');

var rcFlag=true;
var lcFlag=false;

normal.onclick=function(){
    rc.style.display="block";
    lc.style.display="none";
    sm.style.display="none";
    regTop.style.display="block";
    nopw.style.borderBottom="none";
    normal.style.borderBottom="1px solid #ff552e";
    nopw.style.color="#666";
    normal.style.color="#ff552e";
    rcFlag=true;
    lcFlag=false;

}

nopw.onclick=function(){
    rc.style.display="none";
    lc.style.display="block";
    sm.style.display="none";
    regTop.style.display="block";
    nopw.style.borderBottom="1px solid #ff552e";
    normal.style.borderBottom="none";
    nopw.style.color="#ff552e";
    normal.style.color="#666";
    rcFlag=false;
    lcFlag=true;
}

saoma.onclick=function(){
    rc.style.display="none";
    lc.style.display="none";
    sm.style.display="block";
    regTop.style.display="none";
}
screen.onclick=function(){
    regTop.style.display="block";
    sm.style.display="none";
    if(rcFlag){
        rc.style.display="block";
        return;
    }else{
        rc.style.display="none";
    }
    if(lcFlag){
        lc.style.display="block";
        return;
    }else{
        lc.style.display="none";
    }
}


