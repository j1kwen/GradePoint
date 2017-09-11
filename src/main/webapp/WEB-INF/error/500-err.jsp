<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Error - 500</title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<!-- Bootstrap -->
	<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	<script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
</head>
<body>
<div class="container">
	<div class="jumbotron" style="margin-top: 50px;">
		<img src="<%=request.getContextPath() %>/img/img_err_500.png" alt="..." class="img-thumbnail" style="    display: block;
    position: absolute;
    right: 11%;
    top: 90px;">
	  <h1>500<img src="<%=request.getContextPath() %>/img/1311576941346.gif " style="margin-left: 10px;"/></h1>
	  <p>
	  	卧槽，完犊子了，发生了500错误，服务器可能炸了……
	  	<img src="<%=request.getContextPath() %>/img/20090603180114257.gif " />
	  </p>
	  <p>
	  	<a class="btn btn-info btn-lg" href="javascript:history.go(-1);" role="button">返回上一页</a>
	  </p>
	</div>
</div>
<footer class="footer" style="margin: 40px 20px;">
	<div class="container">
		<div class="text-center">
			Copyright © 2016-2017
			<strong><a href="mailto:763461297@qq.com">Shannon</a></strong>&nbsp;
			All Rights Reserved.
		</div>
		<div class="text-center">京ICP备17047375号-1
		</div>
	</div>
</footer>
</body>
</html>