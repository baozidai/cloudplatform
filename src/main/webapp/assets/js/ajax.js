$.ajaxSetup({
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
    complete:function(XMLHttpRequest,textStatus){
        //通过XMLHttpRequest取得响应头，sessionStatus，
        var sessionStatus=XMLHttpRequest.getResponseHeader("sessionStatus");
        if(sessionStatus=="timeout"){
            //如果超时就处理 ，指定要跳转的页面(比如登陆页)
            console.log("身份过期，请重新登录");
            window.location.href= '/index.html';
        }else if(sessionStatus=="nosession"){
            console.log("未登录，请重新登录");
            window.location.href= '/index.html';
        }
    }
});