//平台下拉框
function findPlatform(platformId) {
    $.ajax({
        url:url_+"/platform/getListPlatform.Action",
        type:'GET', //GET
        async:true,    //或false,是否异步
        data:{
        },
        timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){
        },
        success:function (data) {
            console.log(data);
            var content = "";
            $.each(data,function (index,ele) {
                if(platformId == ele.id){
                    content += "<option value="+ele.id+" selected>"+ele.name+"</option>"
                }else {
                    content += "<option value="+ele.id+">"+ele.name+"</option>"
                }
            });
            console.log(content);
            $("#platform_id").append(content);

        },
        error:function(xhr,textStatus){
        },
        complete:function(){
        }
    })
}
// 品种下拉框
function findVariety(varietyId) {
    $.ajax({
        url:url_+"/variety/getListVariety.Action",
        type:'GET', //GET
        async:true,    //或false,是否异步
        data:{
        },
        timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){
        },
        success:function (data) {
            var content = "";
            $.each(data,function (index,ele) {
                if(varietyId == ele.id){
                    content += "<option value="+ele.id+" selected>"+ele.varietyName+"</option>"
                }else {
                    content += "<option value="+ele.id+">"+ele.varietyName+"</option>"
                }
            });
            $("#variety_id").append(content);
        },
        error:function(xhr,textStatus){
        },
        complete:function(){
        }
    })
}

function cancel() {
    parent.layer.closeAll();
}


function setParameter(num,obj) {
    if(obj!=null){
    	findVariety(obj.variety.id);
        findPlatform(obj.platform.id);
    }else{
    	findVariety(-1);
        findPlatform(-1);
    }
    if(num==1) {
        $("#contractInfoId").val(obj.id);
        $("#contract_code").val(obj.contractCode);
        $("#contract_name").val(obj.contractName);
    }
}

$(function () {
    $("#test").click(function () {
        var platformId = $("#platform_id option:selected").val();
        var varietyId = $("#variety_id option:selected").val();
        var contractInfoId = $("#contractInfoId").val();
        var contractName = $("#contract_name").val();
        var contractCode = $("#contract_code").val();
        $.ajax({
            url: url_ + "/contractInfo/saveContractInfo.Action",
            type: 'post', //GET
            async: true,    //或false,是否异步
            data: {
            	contractInfoId: contractInfoId,
                platformId: platformId,
                varietyId: varietyId,
                contractName: contractName,
                contractCode: contractCode,
            },
            timeout: 5000,    //超时时间
            dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
            beforeSend: function (xhr) {
            },
            success: function (data) {
                if(data.success){
                    parent.layer.closeAll();
                    parent.layer.msg(data.msg);
                    parent.$("#contractInfoTable").bootstrapTable('refresh', {
                        silent: true//静默跟新
                    });
                }else {
                	parent.layer.msg(data.msg);
                }
            },
            error: function (xhr, textStatus) {
            },
            complete: function () {
            }
        })
    })
})