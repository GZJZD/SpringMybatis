// var url_="http://localhost:8080/sm";
$(function() {
	dateShow();

	//加载弹出层
	layui.use(['form', 'element'],
		function() {
			layer = layui.layer;
			element = layui.element;
		});

	//触发事件
	var tab = {
		tabAdd: function(title, url, id) {
			//新增一个Tab项
			element.tabAdd('xbs_tab', {
				title: title,
				content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="x-iframe"></iframe>',
				id: id
			})
		},
		tabDelete: function(othis) {
			//删除指定Tab项
			element.tabDelete('xbs_tab', '44'); //删除：“商品管理”

			othis.addClass('layui-btn-disabled');
		},
		tabChange: function(id) {
			//切换到指定Tab项
			element.tabChange('xbs_tab', id); //切换到：用户管理
		}
	};

	tableCheck = {
		init: function() {
			$(".layui-form-checkbox").click(function(event) {
				if($(this).hasClass('layui-form-checked')) {
					$(this).removeClass('layui-form-checked');
					if($(this).hasClass('header')) {
						$(".layui-form-checkbox").removeClass('layui-form-checked');
					}
				} else {
					$(this).addClass('layui-form-checked');
					if($(this).hasClass('header')) {
						$(".layui-form-checkbox").addClass('layui-form-checked');
					}
				}

			});
		},
		getData: function() {
			var obj = $(".layui-form-checked").not('.header');
			var arr = [];
			obj.each(function(index, el) {
				arr.push(obj.eq(index).attr('data-id'));
			});
			return arr;
		}
	}

	//开启表格多选
	tableCheck.init();

	$('.container .left_open i').click(function(event) {
		if($('.left-nav').css('left') == '0px') {
			$('.left-nav').animate({
				left: '-221px'
			}, 100);
			$('.page-content').animate({
				left: '0px'
			}, 100);
			$('.page-content-bg').hide();
		} else {
			$('.left-nav').animate({
				left: '0px'
			}, 100);
			$('.page-content').animate({
				left: '221px'
			}, 100);
			if($(window).width() < 768) {
				$('.page-content-bg').show();
			}
		}

	});

	$('.page-content-bg').click(function(event) {
		$('.left-nav').animate({
			left: '-221px'
		}, 100);
		$('.page-content').animate({
			left: '0px'
		}, 100);
		$(this).hide();
	});

	$('.layui-tab-close').click(function(event) {
		$('.layui-tab-title li').eq(0).find('i').remove();
	});

	//左侧菜单效果
	// $('#content').bind("click",function(event){
	$('.left-nav #nav li').click(function(event) {

		if($(this).children('.sub-menu').length) {
			if($(this).hasClass('open')) {
				$(this).removeClass('open');
				$(this).find('.nav_right').html('&#xe697;');
				$(this).children('.sub-menu').stop().slideUp();
				$(this).siblings().children('.sub-menu').slideUp();
			} else {
				$(this).addClass('open');
				$(this).children('a').find('.nav_right').html('&#xe6a6;');
				$(this).children('.sub-menu').stop().slideDown();
				$(this).siblings().children('.sub-menu').stop().slideUp();
				$(this).siblings().find('.nav_right').html('&#xe697;');
				$(this).siblings().removeClass('open');
			}
		} else {

			var url = $(this).children('a').attr('_href');
			var title = $(this).find('cite').html();
			var index = $('.left-nav #nav li').index($(this));

			for(var i = 0; i < $('.x-iframe').length; i++) {
				if($('.x-iframe').eq(i).attr('tab-id') == index + 1) {
					tab.tabChange(index + 1);
					event.stopPropagation();
					return;
				}
			};

			tab.tabAdd(title, url, index + 1);
			tab.tabChange(index + 1);
		}
		event.stopPropagation();

	})

})

/*弹出层*/
/*
    参数解释：
    title   标题
    url     请求的url
    id      需要操作的数据id
    w       弹出层宽度（缺省调默认值）
    h       弹出层高度（缺省调默认值）
*/
function common_details(id, title,url,w,h) {

	if(title == null || title == '') {
		title = false;
	};
	if(url == null || url == '') {
		url = "404.html";
	};
	if(w == null || w == '') {
		w = ($(window).width() * 0.9);
	};
	if(h == null || h == '') {
		h = ($(window).height() - 50);
	};
	layer.open({
		type: 2,
		area: [w + 'px', h + 'px'],
		fix: false, //不固定
		maxmin: true,
		shadeClose: true,
		shade: 0.4,
		title: title,
		content: url,
		btn: ['关闭']
	});
}

/*关闭弹出框口*/
function common_close() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

/**
 * 日期格式 
 */
function dateShow() {
	layui.use('laydate', function() {
		var laydate = layui.laydate;

		//常规用法
		laydate.render({
			elem: '#test1'
		});
		//常规用法
		laydate.render({
			elem: '#test2'
		});
        //常规用法
        laydate.render({
            elem: '#history-start-time'
        });
        //常规用法
        laydate.render({
            elem: '#history-end-time'
        });

		//国际版
		laydate.render({
			elem: '#test1-1',
			lang: 'en'
		});

		//年选择器
		laydate.render({
			elem: '#test2',
			type: 'year'
		});

		//年月选择器
		laydate.render({
			elem: '#test3',
			type: 'month'
		});

		//时间选择器
		laydate.render({
			elem: '#test4',
			type: 'time'
		});

		//日期时间选择器
		laydate.render({
			elem: '#test5',
			type: 'datetime'
		});

		//日期范围
		laydate.render({
			elem: '#test6',
			range: true
		});

		//年范围
		laydate.render({
			elem: '#test7',
			type: 'year',
			range: true
		});

		//年月范围
		laydate.render({
			elem: '#test8',
			type: 'month',
			range: true
		});

		//时间范围
		laydate.render({
			elem: '#test9',
			type: 'time',
			range: true
		});

		//日期时间范围
		laydate.render({
			elem: '#test10',
			type: 'datetime',
			range: true
		});

		//自定义格式
		laydate.render({
			elem: '#test11',
			format: 'yyyy年MM月dd日'
		});
		laydate.render({
			elem: '#test12',
			format: 'dd/MM/yyyy'
		});
		laydate.render({
			elem: '#test13',
			format: 'yyyyMMdd'
		});
		laydate.render({
			elem: '#test14',
			type: 'time',
			format: 'H点m分'
		});
		laydate.render({
			elem: '#test15',
			type: 'month',
			range: '~',
			format: 'yyyy-MM'
		});
		laydate.render({
			elem: '#test16',
			type: 'datetime',
			range: '到',
			format: 'yyyy年M月d日H时m分s秒'
		});

		//开启公历节日
		laydate.render({
			elem: '#test17',
			calendar: true
		});

		//自定义重要日
		laydate.render({
			elem: '#test18',
			mark: {
				'0-10-14': '生日',
				'0-12-31': '跨年' //每年的日期
					,
				'0-0-10': '工资' //每月某天
					,
				'0-0-15': '月中',
				'2017-8-15': '' //如果为空字符，则默认显示数字+徽章
					,
				'2099-10-14': '呵呵'
			},
			done: function(value, date) {
				if(date.year === 2017 && date.month === 8 && date.date === 15) { //点击2017年8月15日，弹出提示语
					layer.msg('这一天是：中国人民抗日战争胜利72周年');
				}
			}
		});

		//限定可选日期
		var ins22 = laydate.render({
			elem: '#test-limit1',
			min: '2016-10-14',
			max: '2080-10-14',
			ready: function() {
				ins22.hint('日期可选值设定在 <br> 2016-10-14 到 2080-10-14');
			}
		});

		//前后若干天可选，这里以7天为例
		laydate.render({
			elem: '#test-limit2',
			min: -7,
			max: 7
		});

		//限定可选时间
		laydate.render({
			elem: '#test-limit3',
			type: 'time',
			min: '09:30:00',
			max: '17:30:00',
			btns: ['clear', 'confirm']
		});

		//同时绑定多个
		lay('.test-item').each(function() {
			laydate.render({
				elem: this,
				trigger: 'click'
			});
		});

		//初始赋值
		laydate.render({
			elem: '#test19',
			value: '1989-10-14'
		});

		//选中后的回调
		laydate.render({
			elem: '#test20',
			done: function(value, date) {
				layer.alert('你选择的日期是：' + value + '<br>获得的对象是' + JSON.stringify(date));
			}
		});

		//日期切换的回调
		laydate.render({
			elem: '#test21',
			change: function(value, date) {
				layer.msg('你选择的日期是：' + value + '<br><br>获得的对象是' + JSON.stringify(date));
			}
		});
		//不出现底部栏
		laydate.render({
			elem: '#test22',
			showBottom: false
		});

		//只出现确定按钮
		laydate.render({
			elem: '#test23',
			btns: ['confirm']
		});

		//自定义事件
		laydate.render({
			elem: '#test24',
			trigger: 'mousedown'
		});

		//点我触发
		laydate.render({
			elem: '#test25',
			eventElem: '#test25-1',
			trigger: 'click'
		});

		//双击我触发
		lay('#test26-1').on('dblclick', function() {
			laydate.render({
				elem: '#test26',
				show: true,
				closeStop: '#test26-1'
			});
		});

		//日期只读
		laydate.render({
			elem: '#test27',
			trigger: 'click'
		});

		//非input元素
		laydate.render({
			elem: '#test28'
		});

		//墨绿主题
		laydate.render({
			elem: '#test29',
			theme: 'molv'
		});

		//自定义颜色
		laydate.render({
			elem: '#test30',
			theme: '#393D49'
		});

		//格子主题
		laydate.render({
			elem: '#test31',
			theme: 'grid'
		});

		//直接嵌套显示
		laydate.render({
			elem: '#test-n1',
			position: 'static'
		});
		laydate.render({
			elem: '#test-n2',
			position: 'static',
			lang: 'en'
		});
		laydate.render({
			elem: '#test-n3',
			type: 'month',
			position: 'static'
		});
		laydate.render({
			elem: '#test-n4',
			type: 'time',
			position: 'static'
		});
	});
}
/**
 *分页功能
 *  <div id="by-page"></div>  *ID对应
 */

layui.use(['laypage', 'layer'], function() {
	var laypage = layui.laypage,
		layer = layui.layer;

	//完整功能
	laypage.render({
		elem: 'by-page',
		count: 100,
		layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
		jump: function(obj) {
			// console.log(obj);
		}
	});

	//将一段数组分页展示	
});
/**
 * bootstrap-table 表單封裝
 */
//根据窗口调整表格高度


 function tableHight(table_id){
		$(window).resize(function() {
			$(table_id).bootstrapTable('resetView', {
				height: tableHeight()
			}) 	
		})
 }
/**
 * 表單數據遍歷
 * @ tableId 表單id
 * @method  提交方式 post  or get
 * @ url  請求地址
 * @uniqueId  主鍵id
 * @sortOrder 排序方式  asc  or desc
 * @columns   需要顯示的列
 */
function showByTableId(table_Id, method, url, unique_Id, sortOrder, columns) {
	$(table_Id).bootstrapTable({
		method: method,
		contentType: "application/x-www-form-urlencoded", //必须要有！！！！
        async:true,//异步加载
        striped: false, //是否显示行间隔色
		url: url,
		clickToSelect: true,
		uniqueId: unique_Id, //每一行的唯一标识，一般为主键列
		pagination: true, //是否显示分页（*）
		sortable: true, //是否启用排序
		sortOrder: sortOrder, //排序方式
		clickToSelect: true, // 是否启用点击选中行
		pageSize: 10, //单页记录数
		pageList: [50, 100], //分页步进值
		//			search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
		strictSearch: true,
		showColumns: false, //是否显示所有的列  
		showRefresh: false, //是否显示刷新按钮  
		showToggle: false, //是否显示详细视图和列表视图的切换按钮  
		cardView: false, //是否显示详细视图  
		detailView: false, //是否显示父子表 
		//			height:tableHeight(),//高度调整
		queryParams: function(params) {
			return {
				offset: params.offset, //页码
				limit: params.limit, //页面大小
				search: params.search, //搜索
				order: params.order, //排序
				ordername: params.sort, //排序
			};
		},
		buttonsAlign: 'right', //按钮对齐方式
		columns: columns,
		onLoadSuccess: function(data) {
			console.log(data);
		},
		onLoadError: function(status) {
			console.log(status);
			//                  showTips("数据加载失败！");
		},
		onDblClickRow: function(row, $element) {
			console.log(row);
			var id = row.id;
			EditViewById(id, 'view');
			// console.log(id);
		},

	});

}
//查询事件  
function SearchData(table_id) {
	$(table_id).bootstrapTable('refresh', {
	pageNumber: 1
	});
}

//編輯& 修改
function EditViewById(id,title,url,w,h) {
	console.log(id);
	//title, url, w, h
	common_details(id,title,url,w,h);

}

//批量删除  
function BtchDeleteBook(table_id ,url) {
	var opts = $('table_id').bootstrapTable('getSelections');
	if(opts == "") {
		alert("请选择要删除的数据");
	} else {
		var idArray = [];
		for(var i = 0; i < opts.length; i++) {
			idArray.push(opts[i].BookId);
		}
		if(confirm("确定要删除吗：" + idArray + "吗？")) {
			alert("执行删除操作");
		}
	}
	
}

function DeleteByIds(id) {
	alert(id);
}
function documentary_del(id){
	alert(id);
}
