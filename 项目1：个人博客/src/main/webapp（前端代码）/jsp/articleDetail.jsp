<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<title>博客详情</title>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- HTTP 1.1 -->
	<meta http-equiv="pragma" content="no-cache">
	<!-- HTTP 1.0 -->
	<meta http-equiv="cache-control" content="no-cache">
	<!-- Prevent caching at the proxy server -->
	<meta http-equiv="expires" content="0">
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
	<!-- jQuery -->
	<script type="text/javascript" src="static/jquery/jquery-1.12.4.min.js"></script>
	<!-- bootstrap -->
	<link href="static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<script type="text/javascript" src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
	<!-- bootstrap validator -->
	<link href="static/bootstrapvalidator/css/bootstrapValidator.min.css" rel="stylesheet">
	<script type="text/javascript" src="static/bootstrapvalidator/js/bootstrapValidator.min.js"></script>
	
	<link rel="stylesheet" href="static/font-awesome-4.7.0/css/font-awesome.css">
	
	<!-- UEditor -->
    <script type="text/javascript" src="static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="static/ueditor/ueditor.all.js"></script>
    
    <link href="static/css/app.css?v=1" rel="stylesheet">
</head>
<body>
	<div class="container">
		<form id="articleForm">
			<input id="articleId" name="id" type="hidden">
			<div class="form-group" style="margin: 50px auto 0 auto; padding: 0;">
				<label for="article_title"><label style="color: red;">*</label>标题</label>
				<input type="text" class="form-control" id="article_title" name="title" 
					placeholder="文章标题" data-bv-notempty="true" data-bv-notempty-message="不能为空">
			</div>
			
			<div class="form-group">
				<script id="ueditor_content" name="content" type="text/plain"></script>
				<div id="contentError" class="form-group has-feedback has-error">
				   <small class="help-block" data-bv-validator="notEmpty" data-bv-for="content" data-bv-result="INVALID" style="">
				       <span id="contentErrorInfo"></span>                                
				   </small>
				</div>
			</div>

			<div class="modal fade"  id="tip_success" role="dialog" data-backdrop="false"  aria-hidden="true">
			    <div class="modal-dialog">
			        <div class="modal-content">
			            <p class="text-center mb-0">
			                <i class="fa fa-check-circle text-success mr-1" aria-hidden="true"></i>
			                提交成功
			            </p>
			        </div>
			    </div>
			</div>
			<!-- Modal -->
			<div class="modal fade" id="tip_failed" tabindex="-1" role="dialog"
				aria-labelledby="tip_failed_label">
				<div class="modal-dialog" role="document">
					<div id="fail-modal-content" class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="tip_failed_label">错误提示</h4>
						</div>
						<div class="modal-body">
				            <p id="tip_failed_message" class="text-center mb-0">
				                <i class="fa fa-check-circle text-danger mr-1" aria-hidden="true"></i>
	                	提交失败
				            </p>
							<div id="tip_failed_dev_div">
								<a class="btn btn-primary" role="button" data-toggle="collapse"
									href="#tip_failed_trace" aria-expanded="false"
									aria-controls="tip_failed_trace">查看错误信息</a>
								<div class="collapse" id="tip_failed_trace" style="word-break:break-all; word-wrap:break-word;"></div>
							</div>
						</div>
						
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
			<div class="btn-group" role="group">
				<button id="btn_clear" type="button" class="btn btn-danger">清除</button>
				<button id="btn_submit" type="submit" class="btn btn-success">提交</button>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
var ue = UE.getEditor('ueditor_content', {
    initialFrameHeight: 400, //设置编辑器高度
    UEDITOR_HOME_URL: "static/ueditor/",
    scaleEnabled: true
});
var domUtils = UE.dom.domUtils;
ue.addListener('ready',function(){
	domUtils.on(ue.body,"keyup",function(){
		var content = ue.getContent().trim();
		if (content.length > 0){
			$('#articleForm').bootstrapValidator('disableSubmitButtons', false);
			$("#contentErrorInfo").text("");
		}
	});
});

var articleId = '<%=request.getParameter("id")%>';
var type = '<%=request.getParameter("type")%>';
function articleSubmit(){
	var uri = '#';
	var json = {
		title : $("#article_title").val(),
		content : ue.getContent()
	};

	// 新增博客文章，提交数据为：title,content,userAccount
    // 修改博客文章，提交数据：title,content,id
	if(type=="add"){
		uri = "articleAdd";
		json.id = '<%=request.getParameter("id")%>';
	}else if(type=="update"){
		uri = "articleUpdate";
		json.id = articleId;
	}

	$.ajax({
		type:'post',
		url:uri,
		// ajax默认的请求类型
// 		contentType: "application/x-www-form-urlencoded",
// 		data:json,
		contentType: "application/json",
		data:JSON.stringify(json),
// 		contentType: "multipart/form-data",
		dataType:'json', // 返回值类型
		success:function(data){
			$('#articleForm').bootstrapValidator('disableSubmitButtons', false);
			if(data.success){
				$('#fail-modal-content').css("width","200px");
				$('#tip_success').modal('show');
				setTimeout(function() {
					$("#tip_success").modal("hide");
				}, 1200);
			}else{
				$('#fail-modal-content').css("width","500px");
				$('#tip_failed').modal('show');
				var msg = '<i class="fa fa-check-circle text-danger mr-1" aria-hidden="true"></i>';
				if(typeof data.message === 'string'  &&  data.message.length  >  0){
					$("#tip_failed_message").html(msg+data.message);
				}else{
					$("#tip_failed_message").html(msg+"服务器内部错误");
				}
				if(typeof data.stackTrace === 'string'  &&  data.stackTrace.length  >  0){
					$("#tip_failed_dev_div").show();
					$("#tip_failed_trace").html('<div class="well">'+data.stackTrace+'</div>');
				}else{
					$("#tip_failed_dev_div").hide();
				}
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$('#articleForm').bootstrapValidator('disableSubmitButtons', false);
			$('#fail-modal-content').css("width","200px");
			$('#tip_failed').modal('show');
			if (jqXHR.status == 404){
				$("#tip_failed_message").html('<i class="fa fa-check-circle text-danger mr-1" aria-hidden="true"></i>找不到资源');
				$("#tip_failed_dev_div").hide();
			}
			console.log("jqXHR={\n"+jqXHR.status+",\n"+jqXHR.statusText+",\n"+jqXHR.responseText
					+"},\ntextStatus="+textStatus+",\nerrorThrown="+errorThrown);
		}
	});
}


$(function() {

	if(type == "add"){
		$("title").html("新增博客文章");
	}else if(type == "update"){
		$("title").html("修改博客文章");
		$.get('articleDetail?id='+articleId, function(json){
			if(json.success){
				var data = json.data;
				$("#articleId").val(data.id);
				$("#article_title").val(data.title);
				ue.ready(function () {
					ue.setContent(data.content);
				});
			}else{
				var message = "获取文章失败："+json.message;
				alert(message);
			}

		});
	}

	$("#btn_clear").click(function(e){
		ue.setContent('');
	});

	$("#articleForm").submit(function(ev){
		ev.preventDefault();
		$('#articleForm').bootstrapValidator('disableSubmitButtons', false);
	});
	$("#btn_submit").click(function(){
		var bootstrapValidator = $("#articleForm").data('bootstrapValidator');
		bootstrapValidator.validate();
		if(bootstrapValidator.isValid()){
			var content=ue.getContent().trim();
			if (content.length==0){
				$("#contentErrorInfo").text("文章内容不能为空");
				ue.focus(true);
				return;
			}
			articleSubmit();
		} else{
			return;
		}
	});

	$('#articleForm').bootstrapValidator();

});
</script>
</html>