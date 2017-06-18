/****book.js封装笔记本处理****/
//创建笔记本
function sureAddBook(){
	//获取请求参数
	var userId = getCookie("userId");
	var bookName = $("#input_notebook").val().trim();
	//检测格式
	var ok = true;
	if(userId==null){
		window.location.href="log_in.html";
		ok = false;
	}
	if(bookName==""){
		$("#name_span").html("笔记本名为空");
		ok = false;
	}
	//发送Ajax请求
	if(ok){
		$.ajax({
			url:path+"/book/add.do",
			type:"post",
			data:{"userId":userId,"bookName":bookName},
			dataType:"json",
			success:function(result){
				if(result.status==0){
					var book = result.data;
					var id = book.cn_notebook_id;
					var name = book.cn_notebook_name;
					//关闭对话框
					closeAlertWindow();
					//添加一个笔记本li
					createBookLi(id,name);
					//提示消息
					alert(result.msg);
				}
			},
			error:function(){
				alert("创建笔记本失败");
			}
		});
	}
};
//根据用户ID查询显示笔记本列表
function loadUserBooks(){
	//获取Cookie中的userId值
	var userId = getCookie("userId");
	if(userId==null){//未找到
		window.location.href="log_in.html";
	}else{//登录过使用userId做其他操作
		//1.发送Ajax请求加载笔记本列表
		$.ajax({
			url:path+"/book/loadbooks.do",
			type:"post",
			data:{"userId":userId},
			dataType:"json",
			success:function(result){
				if(result.status==0){
					//获取返回的笔记本集合
					var books = result.data;
					//循环集合
					for(var i=0;i<books.length;i++){
						//获取笔记本ID
						var bookId = books[i].cn_notebook_id;
						//获取笔记本名称
						var bookName = books[i].cn_notebook_name;
						//创建一个笔记本列表项<li>元素
						createBookLi(bookId,bookName);
					}
				}
			},
			error:function(){
				alert("加载笔记本列表失败");
			}
		});
	}
};

//创建一个笔记本li元素
function createBookLi(bookId,bookName){
	var sli = "";
	sli+='<li class="online">';
	sli+='  <a>';
	sli+='    <i class="fa fa-book" title="online" rel="tooltip-bottom">';
	sli+='    </i>';
	sli+=	bookName;
	sli+='  </a>';
    sli+='</li>';
   var $li = $(sli);//将sli字符串转成jQuery对象li元素
   $li.data("bookId",bookId);//将值与jQuery对象元素绑定
	//将li元素添加到笔记本ul列表区
   $("#book_ul").append($li);
};


