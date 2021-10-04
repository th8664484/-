<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
    <%@ include file="js.html" %>
    <style type="text/css">
    	#div {
    		margin-top: 10px;
    		margin-left:10px;
    	}
    </style>
    
    </head>
    <body>

    	<div id="div">
    		<a href="userImport.jsp" class="layui-btn layui-btn-normal">
    		<i class="layui-icon">&#xe655;</i>数据导入
    		</a>
    	</div>
        <table class="layui-table">
            <thead>
                <tr>
                    <th>编号</th>
                    <th>用户名</th>
                    <th>密码</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <!-- 循环组装 jstl+el-->
                <c:forEach items="${requestScope.info.data}" var="user">
                    <tr >
                        <td>${user.uid}</td>
                        <td>${user.uname}</td>
                        <td>${user.upass}</td>
                        <td>

                            <a href="edit?uid=${user.uid}&page=${requestScope.info.page}&rows=${requestScope.info.rows}" class="layui-btn layui-btn-warm layui-btn-sm">
                             <i class="layui-icon">&#xe642;</i> 编辑
                            </a>

                            <a href="delete?uid=${user.uid}" class="layui-btn layui-btn-danger layui-btn-sm">
                             <i class="layui-icon">&#xe640;</i> 删除
                            </a>

                            <button class="layui-btn layui-btn-primary layui-border-black layui-btn-sm" onclick="toRole('${user.uid}','${user.uname}')">
                                <i class="layui-icon">&#xe672;</i> 角色分配
                            </button>

                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <!-- 分页导航 -->
        <div id="page-tool"></div>
    </body>
</html>
<script>
layui.use('laypage', function(){
  var laypage = layui.laypage;
  
  //执行一个laypage实例
  laypage.render({
    elem: 'page-tool' //注意，这里的 test1 是 ID，不用加 # 号
    ,count:${requestScope.info.total} //数据总数，从服务端得到
    ,limit:${requestScope.info.rows} 
    ,limits:[1,2,5,8,10]
    ,curr:${requestScope.info.page}
    ,layout:['prev','page','next','limit']
    ,jump: function(obj, first){
        //obj包含了当前分页的所有参数，比如：
        // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
        // console.log(obj.limit); //得到每页显示的条数
        //首次不执行
        if(!first){
        	 location.href = 'findUser?page='+obj.curr+'&rows='+obj.limit ;
        	                
        }
      }
    	
  });
});
function toRole(uid,uname){
    layui.use(['layer'],function (){
        var layer = layui.layer;
        var $ = layui.$;
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("get","assignRoles?uid="+uid+"&uname="+uname,true);
        xmlHttp.send();
        xmlHttp.onreadystatechange = function () {
            if (xmlHttp.readyState == 4 ) {
                if(xmlHttp.status == 200){
                    doBack(xmlHttp.responseText);
                }else{
                    layer.msg('数据出现错误');
                }
            }
        }
        function doBack(responseText){
            layer.open({
                type:1,
                area:[600,500],
                title:'分配角色',
                content:responseText,
                btn:['保存','取消'],
                yes:function (){
                    var uid = document.getElementById("uid");
                    var rids = document.getElementsByName("rid");

                    var uid_value = uid.value;
                    var rid_value ='';//存储所有被选中的角色id
                    for (var i=0;i<rids.length;i++){
                        var rid = rids[i];
                        if (rid.checked == true){
                            rid_value += rid.value+',';
                        }
                    }
                    rid_value = rid_value.substring(0,rid_value.length-1);//去掉最后多余的【,】

                    //发送请求，传递uid和rids
                    $.ajax({
                        type:'post',//ajax请求类型
                        url:'setRolesForUser', //ajax请求命令
                        data:{uid:uid_value , rids:rid_value},//请求传递的参数，会自动拼装
                        synch:true,//设置异步的ajax请求，还是同步的ajax请求
                        success:function (result){
                            //响应成功后调用的回调函数
                            layer.alert('设置成功',function(){
                                layer.closeAll();//关闭所有的layer弹出层
                            });
                        }
                    });
                }
            });
        }

    });

}
</script>