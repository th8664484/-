<%@ page pageEncoding="utf-8" %>
<div class="layui-card">
    <div class="layui-card-header" style="padding: 10px;text-align: center">
        角色编号：<input class="layui-input layui-input-inline" value="${param.rid}" id="set_rid" name="set_rid" style="width:150px;" />
        角色名称：<input class="layui-input layui-input-inline" value="${param.rname}" id="set_rname" name="set_rname" style="width:150px;" />
    </div>
    <div class="layui-card-body">
        <!-- layui-treetable组件装载数据 -->
        <div id="funGrid"></div>

        <!-- 功能类型 处理后 显示的文本 -->
        <script type="text/html" id="typeText">
            {{#  if(d.ftype == 1){      }}
            <span class="layui-badge layui-bg-green">菜单</span>
            {{#  }else{                 }}
            <span class="layui-badge">按钮</span>
            {{#  }                      }}
        </script>

    </div>
</div>
<script>
    var insTb ;
    layui.config({
        base: 'treetable-lay/' //指定treetable组件所在的文件夹目录
    }).use(['treeTable'], function () { //use() 使用指定的layui组件
        var treeTable = layui.treeTable;
        var $ = layui.$;
        insTb = treeTable.render({
            elem:'#funGrid', //指定渲染的位置
            url:'funAll', //使用ajax技术，请求表格中要展示的数据
            cols:[[     // 告诉组件按照什么样的表格格式取展示数据(第1个展示什么，第2个展示什么。。。)
                {type:'numbers'},     // <td>
                {type:'checkbox'},     // <td>
                {title:'功能名称',field:'fname'},     // <td>
            ]],
            tree:{ // 设置查询数据之间的子父关系
                iconIndex: 2, //指定展开合并图标出现在哪一列
                idName:'fid',
                pidName:'pid',
                isPidData:true, //表示按照id和pid的关系，实现子父级别
            },
            done:function (){
                //当treetable表格数据加载完毕时调用的函数
                //数据库查询当前角色上一次分配的功能编号
                //需要js代码发请求 location.href , window.open() , ajax
                $.ajax({
                    type:'get',
                    url:'findFidByRole',
                    data:{ rid:$("#set_rid").val() },
                    synch:true,
                    success:function(result){
                        // [1,2,3,4,5,6,7]
                        console.log(result);
                        insTb.setChecked(result);

                        // "[1,2,3,4,5,6,7]" -> [1,2,3,4,5,6,7]
                        //JSON.parse(result) ;
                    },
                    dataType:'json' //告诉jquery，将响应回来的字符串json反序列化
                });
            }
        });
    });
</script>