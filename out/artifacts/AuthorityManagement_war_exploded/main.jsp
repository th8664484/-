<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
  <meta charset="utf-8">
  <title>主页面</title> 
  <%@ include file="js.html" %>
  <script type="text/javascript">
  
  layui.use(['tree'], function(){
	  var tree = layui.tree;
	  var $ =layui.$;
	  //数据
	  $.ajax({
		  type:'get',
		  url:'findUserMenus',
		  synch:true,
		  success:function (menus){
			  //常规用法
			  tree.render({
				  elem: '#left-tree' //默认是点击节点可进行收缩
				  ,data: menus
			  });
		  },
		  dataType:'json'
	  });


  });

  </script>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
	  <div class="layui-header">
	    <div class="layui-logo">医院管理系统</div>
	    <!-- 头部区域（可配合layui已有的水平导航） -->
	    <ul class="layui-nav layui-layout-left">
	      <li class="layui-nav-item"><a href="">权限管理</a></li>
	    </ul>
	    <ul class="layui-nav layui-layout-right">
	      <li class="layui-nav-item">
	        <a href="javascript:;">
	          <img src="" class="layui-nav-img">
	          ${requestScope.uname } 
	        </a>
	        <dl class="layui-nav-child">
	          <dd><a href="">基本资料</a></dd>
	          <dd><a href="">安全设置</a></dd>
	        </dl>
	      </li>
	      <li class="layui-nav-item"><a href="logout">退出</a></li>
	    </ul>
	  </div>
	  
	  <div class="layui-side layui-bg-gray">
	    <div class="layui-side-scroll">
	      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
	      	<div id="left-tree" class="demo-tree demo-tree-box"></div>
	    </div>
	  </div>
	  
	  <div class="layui-body">
	    <!-- 内容主体区域 -->
	 		<!-- 内嵌子窗口  -->
            <iframe name="content" width="100%" height="100%" frameborder="0"/>
	  </div>
	  
	  <!-- <div class="layui-footer">
	    <-- 底部固定区域 --
	    © layui.com - 底部固定区域
	  </div>  -->
	</div>

</body>
</html>
