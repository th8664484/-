<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
	<%@ include file="js.html" %>
	</head>
	<body>
		<div class="layui-card">
		  <div class="layui-card-header">编辑</div>
		  <div class="layui-card-body">
		    <form  action="modifyuser" method="post">
		    <input type="hidden" name="page" value="${requestScope.page }">
		    <input type="hidden" name="rows" value="${requestScope.rows }">
		     <c:forEach begin="0" end="4" step="1" varStatus="i">
		    	<div class="layui-form-item">
				    <label class="layui-form-label"> ${requestScope.str[i.index][0]}</label>
				    <div class="layui-input-inline">
				      <input type="text" name="${requestScope.str[i.index][1] }" value="${requestScope.str[i.index][2]}"   lay-verify="required" placeholder=""  class="layui-input">
				    </div>
			  </div>
            </c:forEach>
			  
			  <div class="layui-form-item">
			    <div class="layui-input-block">
			      <button class="layui-btn" >立即提交</button>
			      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
			    </div>
			  </div>
			</form>
		    
		  </div>
		</div>
	</body>
</html>