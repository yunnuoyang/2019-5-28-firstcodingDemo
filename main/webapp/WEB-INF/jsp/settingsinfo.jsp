<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2019/5/12
  Time: 20:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sh" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>数据字典信息</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/pintuer.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">
    <script src="<%=request.getContextPath()%>/js/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/js/pintuer.js"></script>
</head>
<body>
<sh:hasRole name="admin">
    <button id="add">添加课程</button>
</sh:hasRole>
<table class="table table-hover text-center" id="couseInfo">
    <tr>
    <tr>
        <th width="10%" hidden="hidden" id="id">id</th>
        <th width="15%">课程名</th>
        <th width="20%">授课老师</th>
        <sh:hasRole name="admin">
            <th width="15%">操作</th>
        </sh:hasRole>
    </tr>
    </tr>

</table>

<form hidden="hidden" action='insert' onsubmit='return check()' class='tab-body-bordered'>
    课程名称<input class='nav-justified' name='name' id='name'><br>
    教师名称<select class='nav-justified' name='tName' id='tName' style='width:100px'></select><br>
    <button type='submit' id='insert'>提交</button>
</form>
</body>
<script>
    (
        $.ajax({
            url: "getCurriculumInfo",
            success: function (data) {
                $.each(data, function (i, value) {

                    $("#couseInfo").append("<tr calss='hello'><td hidden='hidden'>" + value.id + "</td><td>" + value.name + "</td><td>" + value.tName + "</td><td><div class='button-group'>" +
                        // "<a  hidden='hidden'class='button border-main' href='javascript:void(0)' onclick='return modify("+value.id+")'><span class='icon-edit'></span> 修改</a>" +
                        "<sh:hasRole name="admin"><a class='button border-red' href='javascript:void(0)' onclick='return del(" + value.id + ")'><span class='icon-trash-o'></span> 删除</a> </sh:hasRole></div></td></tr>" + "<br>")
                })
            }
        })
    )

    function del(id) {
        if (confirm("您确认要删除此课程吗？")) {
            $.ajax({
                url: "delCourse",
                data: {
                    id: id
                },
                success: function (data) {
                    if (data == true) {
                        alert("删除成功")
                    }
                }
            })
        }

    }

    $("#add").click(function () {
        $("#add").hide()
        $("#couseInfo").hide()
        $("form").show()
    })

    $("#tNname").click(
        $.ajax({
            url: "getTeachers",
            success: function (data) {
                $.each(data, function (i, value) {
                    $("#tName").append("<option>" + value.name + "</option>")
                })
            }
        })
    )

    function check() {
        if ($("#name").val() == "" || $("#tName").val() == "") {
            alert("用户名或者密码为空，请重新输入！")
            return false;
        } else {
            $.ajax({
                url: "insert",
                data: {
                    name: $("#name").val(),
                    tName: $("#tName").val()
                },
                success: function (data) {
                    if (data ==false){
                        alert("添加课程失败")
                    }else{
                        alert("添加课程成功")
                    }
                        }
            })

        }
        return false;
    }

</script>
</html>
