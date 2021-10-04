<%@ page pageEncoding="utf-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 设计分配角色的页面 -->
    <div class="layui-card">
        <div class="layui-card-header">
            用户编号：<input class="layui-input layui-input-inline" value="${param.uid}" id="uid" name="uid" style="width:150px;" />
            用户名称：<input class="layui-input layui-input-inline" value="${param.uname}" id="uname" name="uname" style="width:150px;" />
        </div>
        <div class="layui-card-body">
            <table class="layui-table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="allbtn" onclick="checkAll()" /></th>
                    <th>角色编号</th>
                    <th>角色名称</th>
                    <th>角色描述</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${roles}" var="role">
                    <tr>
                        <td><input type="checkbox" name="rid" value="${role.rid}" <c:if test="${role.flag==1}">checked</c:if> /></td>
                        <td>${role.rid}</td>
                        <td>${role.rname}</td>
                        <td>${role.rdescription}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
<script type="text/javascript">
    function checkAll(){
        var cs = document.getElementsByName('rid');
        var btn = document.getElementById('allbtn');
        for (var i =0;i<cs.length;i++){
            var c = cs[i];
            c.checked=btn.checked;
        }
    }
</script>
