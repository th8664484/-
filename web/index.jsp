<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
 <head>
 	<title>登录</title>
 	<meta charset="utf-8"/>
 	<link rel="stylesheet" type="text/css" href="layui/css/layui.css" />
    <script src="layui/layui.all.js"></script>
    <style type="text/css">
    	body{  		
    		background-image: url(imgs/login_pg.jpg);
    		background-size:100% 100%;
    	}
    	.layui-card{
    		width: 350px;
    		margin: 250px auto;
    		opacity:0.7;
    	}
    	tr{
    		height: 50px;
    	}
    </style>
    <script type="text/javascript">
    var no=1;
    function reloadCode(){
        //改换验证码，<img src="2.jpg">重新设置src属性
        var img = document.getElementById('codeImg');
        img.src='generatcheckcode?no='+no++;
    }
    </script>
 </head>
	<body>
		<c:if test="${requestScope.value != null }">
			<script type="text/javascript" language="javascript">
				layer.msg('${requestScope.value}');
  	    </script>
		</c:if>
		
		<form action="login" method="post">
			<div class="layui-card">
	        <div class="layui-card-header"><h2>用户登录</h2></div>
	        <div class="layui-card-body">
	          	<table>
					<tr>
						<td>用户名:</td>
						<td>
							<input type="text" name="uname" required  lay-verify="required" placeholder="请输入账号" autocomplete="off" class="layui-input">
						</td>
					</tr>
					<tr>
						<td>密&nbsp;&nbsp;&nbsp;码:</td>
						<td><input type="password" name="upass" value="" placeholder="请输入密码" class="layui-input" /></td>
					</tr>
					<tr>
						<td>验证码:</td>
						<td>
						<input style='width:100px;height:30px;' type="text"  name="code" placeholder="验证码" class="layui-input-inline" />
						<img src='generatcheckcode' id="codeImg" onclick='reloadCode()'/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center"> 
						<button class="layui-btn" lay-submit lay-filter="formDemo">登录</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="autoflag" >自动登录
						</td>
					</tr>
				</table>
	        </div>
	      </div>
		</form>

	</body>
</html>