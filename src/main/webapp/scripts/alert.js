/***alert.js对话框处理脚本函数***/
//弹出转移笔记对话框
function alertMoveNoteWindow(){
	$("#can").load("alert/alert_move.html",function(){
		//显示对话框背景
		$(".opacity_bg").show();
		//加载对话框中的option笔记本项
		var lis = $("#book_ul li");
		//循环获取笔记本信息
		for(var i=0;i<lis.length;i++){
			var $li = $(lis[i]);//将li转成jQuery对象
			var bookId = $li.data("bookId");
			var bookName = $li.text().trim();
			//拼一个option
			var opt = "";
			opt+='<option value="'+bookId+'">';
			opt+=bookName;
			opt+='</option>';
			//将option添加到select中
			$("#moveSelect").append(opt);
		}
	});
	
};
//确认删除笔记对话框
function alertDeleteNoteWindow(){
	$("#can").load("alert/alert_delete_note.html");
	$(".opacity_bg").show();
}
//关闭对话框
function closeAlertWindow(){
	$("#can").html("");//清空对话框内容
	$(".opacity_bg").hide();//隐藏背景色
};

//弹出创建笔记本对话框
function alertAddBookWindow(){
	//弹出添加笔记本对话框
	$("#can").load("alert/alert_notebook.html");
	//显示对话框灰色背景(使用class="opacity_bg"选择)
	$(".opacity_bg").show();
};

//弹出创建笔记对话框
function alertAddNoteWindow(){
	//判断是否有笔记本选中
	var $li = $("#book_ul a.checked").parent();
	if($li.length==0){
		alert("请选择笔记本");
	}else{//弹出对话框
		$("#can").load("alert/alert_note.html");
		$(".opacity_bg").show();
	}

};


