var url_="http://127.0.0.1:8080/sm";

/*
var jqxhr;
//设置ajax请求完成后运行的函数,
$.ajaxSetup({
    complete:function(){
        console.log("1");
        if("REDIRECT" == jqxhr.getResponseHeader("REDIRECT")){ //若HEADER中含有REDIRECT说明后端想重定向，
            console.log("2");
            alert("=======================");
            $(location).attr('href', 'index.html');
            var win = window;
            while(win != win.top){
                win = win.top;
            }
            win.location.href = jqxhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
        }
    }
});
*/

//ajax請求完成后Complete函數,全局控制處理一些業務
$(document).ajaxComplete(function(event,xhr,options){
    //判斷登陸失效重定向
    if("REDIRECT" == xhr.getResponseHeader("REDIRECT")){//若HEADER中含有REDIRECT说明后端想重定向，
        $(location).attr('href', 'index.html');
        var win = window;
        while(win != win.top){
            win = win.top;
        }
        //将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
        win.location.href = xhr.getResponseHeader("CONTENTPATH");
    }
    //
});