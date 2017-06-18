/***share.js封装分享和收藏笔记操作***/
//按回车执行分享笔记搜索，默认加载第一页记录
function sureSearchShare(event){
	var code = event.keyCode;
	if(code==13){//回车键
		//清空原有搜索列表
		$("#search_ul li").remove();
		//获取请求参数
		var keyword = $("#search_note").val().trim();
		page = 1;//按新条件检索,显示第一页记录
		//发送Ajax请求
		loadPageShare(keyword,page);
	}
};

//按"更多..."按钮查询下一页记录
function moreSearchShare(){
	//获取请求参数
	var keyword = $("#search_note").val().trim();
	page = page+1;//计算要加载的下一页页数
	//发送Ajax请求加载数据列表
	loadPageShare(keyword,page);
};

//发送Ajax请求,分页加载搜索结果
function loadPageShare(keyword,page){
	$.ajax({
		url:path+"/share/search.do",
		type:"post",
		data:{"keyword":keyword,"page":page},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//解析返回JSON生成搜索结果列表
				var shares = result.data;
				for(var i=0;i<shares.length;i++){
					//获取分享ID
					var shareId = shares[i].cn_share_id;
					//获取分享标题
					var shareTitle= shares[i].cn_share_title;
					//创建一个li元素
					var sli = "";
					sli+='<li class="online">';
					sli+='  <a>';
					sli+='	<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>';
					sli+= shareTitle;
					sli+='	<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down"><i class="fa fa-star"></i></button>';
					sli+='  </a>';
					sli+='</li>';
					var $li = $(sli);
					$li.data("shareId",shareId);
					//添加到搜索结果列表ul中
					$("#search_ul").append($li);
				}
				//切换列表显示区
				$("#pc_part_2").hide();
				$("#pc_part_4").hide();
				$("#pc_part_6").show();
				$("#pc_part_7").hide();
				$("#pc_part_8").hide();
			}
		},
		error:function(){
			alert("搜索失败");
		}
	});
};

//分享笔记操作
function sureShareNote(){
	//获取请求参数
	var $li = $(this).parents("li");
	var noteId = $li.data("noteId");
	//发送Ajax请求
	$.ajax({
		url:path+"/share/add.do",
		type:"post",
		data:{"noteId":noteId},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//给笔记添加分享图标
				var noteTitle = $li.text();
				var str = "";
				str+='<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>';
				str+= noteTitle;
				str+='<i class="fa fa-sitemap"></i>';
				str+='<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down"><i class="fa fa-chevron-down"></i></button>';
				//将笔记li元素中的<a>的内容替换
				$li.find("a").html(str);
				//提示成功
				alert(result.msg);
			}
		},
		error:function(){
			alert("分享笔记失败");
		}
	});
};