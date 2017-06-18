/***note.js封装笔记处理***/
//转移笔记操作
function sureMoveNote(){
	//获取请求参数
	//1.获取选中的笔记li及笔记ID
	var $li = $("#note_ul a.checked").parent();
	var noteId = $li.data("noteId");
	//2.获取转入的笔记本ID
	var bookId = $("#moveSelect").val();
	//TODO格式检查(检查转出和转入笔记本ID是否一致)
	//发送Ajax请求
	$.ajax({
		url:path+"/note/move.do",
		type:"post",
		data:{"noteId":noteId,"bookId":bookId},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//移除笔记列表li
				$li.remove();
				//提示成功
				alert(result.msg);
			}
		},
		error:function(){
			alert("转移笔记失败");
		}
	});
};
//删除笔记操作
function sureDeleteNote(){
	//获取请求参数
	var $li = $("#note_ul a.checked").parent();
	var noteId = $li.data("noteId");
	//发送Ajax请求
	$.ajax({
		url:path+"/note/delete.do",
		type:"post",
		data:{"noteId":noteId},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//删除列表中的笔记li
				$li.remove();
				//提示成功
				alert(result.msg);
			}
		},
		error:function(){
			alert("删除笔记失败");
		}
	});
};
//笔记菜单处理
function showNoteMenu(){
	$("#note_ul").on("click",".btn_slide_down",function(){
		//隐藏所有笔记菜单
		$("#note_ul div").hide();
		//显示当前点击的菜单
		var note_menu = 
			$(this).parents("li").find("div");
		note_menu.slideDown(1000);
		//设置笔记li选中效果
		$("#note_ul a").removeClass("checked");
		$(this).parent().addClass("checked");
		//阻止li和body的click事件冒泡
		return false;//返回false就可以阻止冒泡
	});
	//点击body范围,将笔记菜单隐藏
	$("body").click(function(){
		//隐藏所有笔记菜单
		$("#note_ul div").hide();
	});
};

//创建笔记
function sureAddNote(){
	//获取请求参数
	var title = $("#input_note").val().trim();
	var userId = getCookie("userId");
	var $li = $("#book_ul a.checked").parent();
	var bookId = $li.data("bookId");
	//格式检查
	var ok = true;
	if(title==""){//检查是否为空
		ok = false;
		$("#title_span").html("笔记标题为空");
	}
	if(userId==null){//检查是否失效
		ok = false;
		window.location.href="log_in.html";
	}
	//发送Ajax请求
	if(ok){
		$.ajax({
			url:path+"/note/add.do",
			type:"post",
			data:{"userId":userId,
				"bookId":bookId,"title":title},
			dataType:"json",
			success:function(result){
				if(result.status==0){
					var note = result.data;
					//关闭对话框(可省略)//closeAlertWindow();
					//创建一个笔记li元素
					var id = note.cn_note_id;
					var title = note.cn_note_title;
					createNoteLi(id,title);
					//弹出提示
					alert(result.msg);
				}
			},
			error:function(){
				alert("创建笔记失败");
			}
		});
	}
};

//更新笔记信息
function updateNote(){
	//获取请求参数
	var $li = $("#note_ul a.checked").parent();
	var noteId = $li.data("noteId");//笔记ID
	var noteTitle = 
		$("#input_note_title").val().trim();//笔记标题
	var noteBody = um.getContent();//笔记内容
	//发送Ajax请求
	$.ajax({
		url:path+"/note/update.do",
		type:"post",
		data:{"noteId":noteId,
			"title":noteTitle,"body":noteBody},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//更新标题
				var str = "";
				str+='<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>';
				str+= noteTitle;
				str+='<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down"><i class="fa fa-chevron-down"></i></button>';
				//将str替换到笔记li的a元素里
				$li.find("a").html(str);
				//提示成功
				alert(result.msg);
			}
		},
		error:function(){
			alert("保存笔记失败");
		}
	});
};
//根据笔记ID加载笔记信息
function loadNote(){
	//切换预览和编辑显示区域
	$("#pc_part_5").hide();//预览区
	$("#pc_part_3").show();//编辑区
	//设置选中效果
	$("#note_ul a").removeClass("checked");
	$(this).find("a").addClass("checked");
	//获取请求参数
	var noteId = $(this).data("noteId");
	//发送Ajax请求
	$.ajax({
		url:path+"/note/load.do",
		type:"post",
		data:{"noteId":noteId},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//获取返回的笔记标题
				var title = 
					result.data.cn_note_title;
				//获取返回的笔记内容
				var body = 
					result.data.cn_note_body;
				//设置到编辑区
				$("#input_note_title").val(title);//设置笔记标题
				um.setContent(body);//设置笔记内容
			}
		},
		error:function(){
			alert("加载笔记信息失败");
		}
	});
};
//根据笔记本ID加载笔记列表
function loadBookNotes(){
	//设置选中效果
	$("#book_ul a").removeClass("checked");
	$(this).find("a").addClass("checked");
	//获取请求参数
	var bookId = $(this).data("bookId");
	//发送Ajax请求 
	$.ajax({
		url:path+"/note/loadnotes.do",
		type:"post",
		data:{"bookId":bookId},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//切换列表显示区
				$("#pc_part_2").show();
				$("#pc_part_4").hide();
				$("#pc_part_6").hide();
				$("#pc_part_7").hide();
				$("#pc_part_8").hide();
				//生成笔记列表
				var notes = result.data;//获取笔记信息
				//清除原有笔记列表信息
				$("#note_ul li").remove();
				//$("#note_ul").empty();
				//循环处理
				for(var i=0;i<notes.length;i++){
					//获取笔记ID
					var noteId = notes[i].cn_note_id;
					//获取笔记标题
					var noteTitle = notes[i].cn_note_title;
					//创建一个笔记列表li元素
					createNoteLi(noteId,noteTitle);
				}
			}
		},
		error:function(){
			alert("加载笔记列表失败");
		}
	});
};

//创建一个笔记列表li元素
function createNoteLi(noteId,noteTitle){
	var sli = "";
	sli+='<li class="online">';
	sli+='  <a>';
	sli+='	<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>';
	sli+= noteTitle;
	sli+='	<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down"><i class="fa fa-chevron-down"></i></button>';
	sli+='  </a>';
	sli+='  <div class="note_menu" tabindex="-1">';
	sli+='	<dl>';
	sli+='		<dt><button type="button" class="btn btn-default btn-xs btn_move" title="移动至..."><i class="fa fa-random"></i></button></dt>';
	sli+='		<dt><button type="button" class="btn btn-default btn-xs btn_share" title="分享"><i class="fa fa-sitemap"></i></button></dt>';
	sli+='		<dt><button type="button" class="btn btn-default btn-xs btn_delete" title="删除"><i class="fa fa-times"></i></button></dt>';
	sli+='	</dl>';
	sli+='  </div>';
	sli+='</li>';
	//将笔记ID绑定到li元素上
	var $li = $(sli);
	$li.data("noteId",noteId);
	//将li元素添加笔记列表ul中
	$("#note_ul").append($li);
};

