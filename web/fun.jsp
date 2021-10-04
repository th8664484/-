<%@ page pageEncoding="UTF-8" language="java" %>
<html>
	<head>
	<%@ include file="js.html" %>
	</head>
	<body>
	
	<!-- 新建功能按钮 -->
	<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" style="margin: 10px 0 0 10px;" onclick="toAdd(-1,'根菜单')">
		<i class="layui-icon">&#xe61f;</i>新建
	</button>
	<!-- 数据展示位置 -->
	<div id="funGrid"></div>
	<!-- 弹出层中的表单模板 -->
	<form action=""  id="funAddForm" class="layui-form" style="display:none;padding-top:10px;">
		<div class="layui-form-item">
                <label class="layui-form-label">功能名称</label>
                <div class="layui-input-inline">
                    <input type="hidden" name="fid" id="fid" value="" />
                    <input type="text" name="fname" id="fname" required  lay-verify="required" placeholder="请输入名称" autocomplete="off" class="layui-input layui-input-inline">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">功能类别</label>
                <div  class="layui-input-inline">
                    <input type="radio" name="ftype" id="ftype1" value="1" title="菜单" checked>
                    <input type="radio" name="ftype" id="ftype2" value="2" title="按钮">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">功能链接(URL)</label>
                <div  class="layui-input-inline">
                    <input type="text" name="fhref" id="fhref" placeholder="请输入链接" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">功能范围</label>
                <div  class="layui-input-inline">
                    <input type="text" name="fauth" id="fauth" required  lay-verify="required" placeholder="请输入功能范围" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">所属父级</label>
                <div  class="layui-input-inline">
                    <input type="hidden" name="pid" id="pid" value="-1" />
                    <input type="text" name="pname" id="pname" readonly  autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
                    <button type="button" class="layui-btn layui-btn-primary" onclick="closeEditWin()">取消</button>
                </div>
            </div>
	    
	</form>
	<!-- 功能类型 处理后 显示的文本 -->
	<script type="text/javascript" id="typeText">
		{{# if(d.ftype==1){ }}
		    <span class="layui-badge">菜单</span>
		{{# }else{ }}
		    <span class="layui-badge layui-bg-green">按钮</span>
		{{# } }}
	</script>
	<!-- 操作  新建 编辑 删除 按钮 -->
	<script type="text/javascript" id="rowBtns">
		{{#   if(d.ftype==1){    }}
			<button type="button" class="layui-btn layui-btn-normal layui-btn-xs " onclick="toAdd('{{d.fid}}','{{d.fname}}')">
				<i class="layui-icon">&#xe61f;</i>新建
			</button>
		{{#   }else{     }}
			<button type="button" class="layui-btn layui-btn-normal layui-btn-xs layui-btn-disabled" >
				<i class="layui-icon">&#xe61f;</i>新建
			</button>
		{{#   }     }}
		<button type="button" class="layui-btn layui-btn-warm layui-btn-xs" onclick="modify('{{d.fid}}','{{d.fname}}','{{d.ftype}}','{{d.fhref}}','{{d.fauth}}','{{d.pid}}','{{d.pname}}')">
			<i class="layui-icon">&#xe642;</i>修改
		</button>
		<button type="button" class="layui-btn layui-btn-danger layui-btn-xs" onclick="detele('{{d.fid}}','{{d.ftype}}')">
			<i class="layui-icon">&#xe640;</i>删除
		</button>
		
	</script>

    </script>
    <!-- layui-js代码实现 -->
	<script>
          layui.config({
              base: 'treetable-lay/'
          }).use(['treeTable'], function () {
             var treeTable = layui.treeTable;
			  
             treeTable.render({
            	 elem:'#funGrid',//指定渲染位置
            	 url:'funAll',//使用ajax技术，请求表格中的数据
            	 cols:[[      //按照什么格式展示数据
            		 {type:'numbers'},
            		 {type:'checkbox'},
            		 {title:'功能名称',field:'fname'},
            		 {title:'功能类别',field:'ftype',templet:'#typeText'},
            		 {title:'请求url',field:'fhref'},
            		 {title:'权限范围',field:'fauth'},
            		 {title:'操作',templet:'#rowBtns'}
            	 ]],
            	 tree:{//设置查询数据之间的关系
            		 iconIndex:2,
            		 idName:'fid',
            		 pidName:'pid',
            		 isPidData:true,//按照id和pid的关系，实现子父级别
            	 }
             });
          });
          function toAdd(pid,pname) {
              var fun ={
                  "action":"funAdd",
                  "pid":pid,
                  "pname":pname,
                  "tit":'新建功能'
              };
			popupwin(fun)
		}
		  function modify(fid,fname,ftype,fhref,fauth,pid,pname){
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("post", "parent?fid="+pid, true);
            xmlHttp.send();
            xmlHttp.onreadystatechange = function () {
                if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                    var response = xmlHttp.responseText;
                    pname = response;
                    var fun ={
                        "action":"funUpdate",
                        "fid":fid,
                        "fname":fname,
                        "ftype":ftype== null?"":ftype,
                        "fhref":fhref== null?"":fhref,
                        "fauth":fauth=="undefined"?"":fauth,
                        "pid":pid,
                        "pname":pname,
                        "tit":'修改内容'
                    };
                    console.log(fun)
                    popupwin(fun)
                }
            }

        }
          function popupwin(obj){
            layui.use(['layer'],function(){
                var layer = layui.layer;
                var $ = layui.jquery;
                console.log(obj)
                if (Object.keys(obj).length >4 ){
                    $('#funAddForm').attr( 'action' ,obj.action);
                    $('#fid').val(obj.fid);
                    $('#fname').val(obj.fname);
                    $('#fhref').val(obj.fhref);
                    $('#fauth').val(obj.fauth);

                    var radio = document.getElementsByName("ftype");
                    var radioLength = radio.length;
                    for (var i = 0; i < radioLength; i++) {
                        if (obj.ftype == radio[i].value) {
                            $(radio[i]).next().click();
                        }
                    }
                    $('#pid').val(obj.pid);
                    $('#pname').val(obj.pname);
                }else {
                    $('#funAddForm').attr( 'action' ,obj.action);
                    $('#pid').val(obj.pid);
                    $('#pname').val(obj.pname);
                    // $('#fid').remove();

                }

                layer.open({
                    type:1,//5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                    area:[400],//宽高
                    title:obj.tit,
                    content:$('#funAddForm')
                });
            });
          }
          function closeEditWin(){
              layui.use('layer',function(){
                  var layer = layui.layer ;
                  layer.closeAll();
              });
          }
          function detele(fid,ftype){
              layer.msg('<a href='+"dfun?fid="+fid+"&ftype="+ftype+" "+'class="layui-btn layui-btn-danger">确认删除</a>', {icon: 0});
          }

    </script>
	</body>
</html>