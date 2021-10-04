<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
    <%@ include file="js.html" %>
    <style type="text/css">
    	.layui-card-body{
    		width:300px;
    		background-color: gray;
    	}
    	li {
    		margin: 10px;
    	}
    </style>
    </head>
    <body>
		<div class="layui-card">
			  <div class="layui-card-header">用户数据导入</div>
			  <div class="layui-card-body">
			    <form action="userimport" method="post" enctype="multipart/form-data">
			      <ul>
				      <li><input type="file" name="excel" /></li>
				      <li><input type="submit" value="保存" class="layui-btn layui-btn-sm"> 
				      <i style="color: red;">${requestScope.value}</i>
				      </li>
			      </ul>		    	
			    </form>
			  </div>
		</div>
    </body>
</html>