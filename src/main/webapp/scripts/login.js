//主处理
$(function(){
	//给登录按钮绑定单击处理
	$("#login").click(userLogin);
	//给注册按钮绑定单击处理
	$("#regist_button").click(userRegist);
});
//用户注册
function userRegist(){
	//清除提示信息
	$("#warning_1 span").html("");
	$("#warning_2 span").html("");
	$("#warning_3 span").html("");
	//获取请求参数
	var name = 
		$("#regist_username").val().trim();
	var nick = $("#nickname").val().trim();
	var password = 
		$("#regist_password").val().trim();
	var final_password = 
		$("#final_password").val().trim();
	//检查格式
	var ok = true;
	//检测用户名
	if(name==""){
		ok = false;
		$("#warning_1 span").html("用户名为空");
		$("#warning_1").show();
	}
	//检测密码
	if(password==""){
		ok = false;
		$("#warning_2 span").html("密码为空");
		$("#warning_2").show();
	}else	if(password.length>0&&password.length<6){
		ok = false;
		$("#warning_2 span").html("密码长度小于6位");
		$("#warning_2").show();
	}
	//检测确认密码
	if(final_password==""){
		ok = false;
		$("#warning_3 span").html("确认密码为空");
		$("#warning_3").show();
	}else if(final_password!=password){
		ok = false;
		$("#warning_3 span").html("与密码不一致");
		$("#warning_3").show();
	}
	//通过检测发送ajax请求
	if(ok){
		$.ajax({
			url:path+"/user/add.do",
			type:"post",
			data:{"name":name,"nick":nick,
				"password":password},
			dataType:"json",
			success:function(result){
				if(result.status==0){//成功
					alert(result.msg);//提示注册成功
					$("#back").click();//返回到登录界面
				}else if(result.status==1){//用户名被占用
					$("#warning_1 span").html(result.msg);
					$("#warning_1").show();
				}
			},
			error:function(){
				alert("注册失败");
			}
		});
	}
};
//用户登录
function userLogin(){
	//清除原有消息
	$("#count_span").html("");
	$("#password_span").html("");
	//获取请求参数
	var name = $("#count").val().trim();
	var password = $("#password").val().trim();
	//检测格式
	var ok = true;//是否通过校验
	if(name==""){
		$("#count_span").html("用户名为空");
		ok = false;
	}
	if(password==""){
		$("#password_span").html("密码为空");
		ok = false;
	}
	//发送Ajax请求
	if(ok){//(通过格式检测)
		$.ajax({
			url:path+"/user/login.do",
			type:"post",
			data:{"name":name,"password":password},
			dataType:"json",
			success:function(result){
				//result是服务器返回的JSON结果
				//{"status":xx,"msg":xx,"data":xx}
				if(result.status==0){//成功
					//将用户信息写入Cookie
					var userId = result.data.cn_user_id;
					addCookie("userId",userId,2);
					window.location.href="edit.html";
				}else if(result.status==1){//用户名错
					$("#count_span").html(result.msg);
				}else if(result.status==2){//密码错
					$("#password_span").html(result.msg);
				}
			},
			error:function(){
				alert("登录失败");
			}
		});
	}
};