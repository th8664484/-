<%@ page pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <%@ include file="js.html" %>
</head>
<body>
<!-- 新建功能按钮 -->
<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" style="margin: 10px 0 0 10px;" onclick="add()">
    <i class="layui-icon">&#xe61f;</i>添加
</button>
<!-- 角色展示位置 -->
<table id="roleGrid"></table>
<!-- 弹出层中的表单模板 -->
<form id="roleEditForm" class="layui-form" action="roleUpdate" style="display:none;padding-top:10px;">
    <div class="layui-form-item" id="prid">
        <label class="layui-form-label">角色编号</label>
        <div class="layui-input-inline">
            <input type="text" name="rid" id="rid"
                   readonly   placeholder="请输入名称"
                   autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">角色名称</label>
        <div  class="layui-input-inline">
            <input type="text" name="rname" id="rname" required placeholder="请输入名称" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">角色描述</label>
        <div  class="layui-input-inline">
            <input type="text" name="rdescription" id="rdescription" required  lay-verify="required" placeholder="请输入描述" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
            <button type="button" class="layui-btn layui-btn-primary" onclick="closeEditWin()">取消</button>
        </div>
    </div>

</form>

<!-- 操作  编辑 删除 按钮 -->
<script type="text/html" id="rowBtns">
    <button class="layui-btn layui-btn-xs layui-btn-warm" onclick="toEdit('{{d.rid}}','{{d.rname}}','{{d.rdescription}}')">
        <i class="layui-icon">&#xe642;</i> 编辑
    </button>

    <button class="layui-btn layui-btn-xs layui-btn-danger" onclick="detele('{{d.rid}}')">
        <i class="layui-icon">&#xe640;</i> 删除
    </button>

    <button class="layui-btn layui-btn-primary layui-border-black layui-btn-xs" onclick="toSetFun('{{d.rid}}','{{d.rname}}')">
        <i class="layui-icon">&#xe672;</i> 功能分配
    </button>
</script>

<script type="text/javascript">
    layui.use(['table'],function (){
        var table = layui.table;
        table.render({
            elem:'#roleGrid',
            url:'roleFindAll',//底层使用ajax请求数据
            cols:[[
                {title:'角色编号',field:'rid'},
                {title: '角色名称',field: 'rname'},
                {title: '角色描述',field: 'rdescription'},
                {title: '操作',templet:'#rowBtns'}
            ]],
            page:true
        });
    });
    function add(){
        var obj={
            "action":"roleAdd",
            "tit":"添加角色"
        }
        popupwin(obj);
    }
    function toEdit(rid,rname,rdescription){
        var obj={
            "action":"roleUpdate",
            "tit":"编辑角色",
            "rid":rid,
            "rname":rname,
            "rdescription":rdescription
        }
        popupwin(obj);
    }
    function popupwin(obj){
        layui.use(['layer'],function(){
            var layer = layui.layer;
            var $ = layui.jquery;
            $('#roleEditForm').attr( 'action' ,obj.action);
            if (Object.keys(obj).length > 3){
                $('#rid').val(obj.rid) ;
                $('#rname').val(obj.rname) ;
                $('#rdescription').val(obj.rdescription);
            }else{
                $("#prid").remove();
                $("input").val("");
            }
            layer.open({
                type:1,//5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                area:[400],//宽高
                title:obj.tit,
                content:$('#roleEditForm')
            });
        });
    }

    function closeEditWin(){
        layui.use('layer',function(){
            var layer = layui.layer ;
            layer.closeAll();
        });
    }
    function detele(rid){
        layer.msg('<a href='+"detele?rid="+rid+" "+'class="layui-btn layui-btn-danger">确认删除</a>', {icon: 0});
    }

    //分配功能
    function toSetFun(rid,rname){
        //弹出一个窗口， layui-layer弹出层
        layui.use('layer',function(){
            var layer = layui.layer ;
            var $ = layui.jquery ; //layui.$;

            $.ajax({
                type:'get',
                url:'setFuns.jsp',
                data:{rid:rid,rname:rname},
                synch:true,
                success:function(viewStr){

                    layer.open({
                        title:'分配功能',
                        area:['600','500'],
                        type:1,
                        content:viewStr, //ajax请求要展示的模板内容，同时原内容依然存在
                        btn:['保存','取消'],
                        yes:function(){
                            var cs = insTb.checkStatus();
                            var fids='';
                            for (var i=0;i<cs.length;i++){
                                var c = cs[i];
                                fids += c.fid+',';
                            }
                           $.ajax({
                               method:'post',
                               url:'setFuns',
                               data:{rid:rid,fids:fids},
                               synch: true,
                               success:function (){
                                   layer.alert('分配成功',function (){
                                      layer.closeAll();
                                   });
                               }
                           });
                        }
                    });

                }
            });
        });
    }
</script>
</body>
</html>
