layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    // 渲染表格
    var tableIns = table.render({
        elem: '#saleChanceList',
        url: ctx + '/sale_chance/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "saleChanceListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'chanceSource', title: '机会来源', align: "center"},
            {field: 'customerName', title: '客户名称', align: 'center'},
            {field: 'cgjl', title: '成功几率', align: 'center'},
            {field: 'overview', title: '概要', align: 'center'},
            {field: 'linkMan', title: '联系人', align: 'center'},
            {field: 'linkPhone', title: '联系电话', align: 'center'},
            {field: 'description', title: '描述', align: 'center'},
            {field: 'createMan', title: '创建人', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'uname', title: '指派人', align: 'center'},
            {field: 'assignTime', title: '分配时间', align: 'center'},
            {
                field: 'state',
                title: '分配状态',
                align: 'center',
                templet: function (d) {
                    return formatterState(d.state);
                }
            },
            {
                field: 'devResult',
                title: '开发状态',
                align: 'center',
                templet: function (d) {
                    return formatterDevResult(d.devResult);
                }
            },
            {title: '操作', templet: '#saleChanceListBar', fixed: "right", align: "center", minWidth: 150}
        ]]
    });

    // 多条件搜索
    $("#searchBtn").on("click", function () {
        alert("点击了搜索按钮！"); // 测试是否绑定上
        console.log("ctx=", ctx);  // 打印 ctx 是否为空

        table.reload('saleChanceListTable', {
            url: ctx + '/sale_chance/list', // 使用 ctx 路径
            where: {
                customerName: $("#customerName").val(),
                createMan: $("#createMan").val(),
                state: $("#state").val()
            },
            page: {
                curr: 1
            }
        });
    });

    // 工具栏事件
    table.on('toolbar(saleChances)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case "add":
                openAddOrUpdateSaleChanceDialog();
                break;
            case "del":
                delSaleChance(checkStatus.data);
                break;
        }
    });

    // 行监听事件
    table.on("tool(saleChances)", function (obj) {
        console.log("obj=", obj)
        var layEvent = obj.event;
        if (layEvent === "edit") {
            openAddOrUpdateSaleChanceDialog(obj.data.id);
        } else if (layEvent === "del") {
            layer.confirm('确定删除当前数据？', {icon: 3, title: "机会数据管理"}, function (index) {
                $.post(ctx + "/sale_chance/delete", {ids: obj.data.id}, function (data) {
                    if (data.code == 200) {
                        layer.msg("你真牛逼！",{icon: 6});
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg, {icon: 5});
                    }
                });
            });
        }
    });

    // 打开添加或修改窗口
    function openAddOrUpdateSaleChanceDialog(sid) {
        var url = ctx + "/sale_chance/toSaleChancePage";
        var title = "营销机会管理-机会添加";
        if (sid) {
            url += "?saleChanceId=" + sid;
            title = "营销机会管理-机会更新";
        }
        layui.layer.open({
            title: title,
            type: 2,
            area: ["700px", "560px"],
            maxmin: true,
            content: url
        });
    }

    // 批量删除
    function delSaleChance(datas) {
        if (datas.length == 0) {
            layer.msg("请选择删除记录!", {icon: 5});
            return;
        }
        layer.confirm('确定删除选中的机会数据？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            var ids = "ids=";
            for (var i = 0; i < datas.length; i++) {
                ids += datas[i].id;
                if (i < datas.length - 1) {
                    ids += "&ids=";
                }
            }
            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/delete",
                data: ids,
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg, {icon: 5});
                    }
                }
            });
        });
    }

    // 状态格式化
    function formatterState(state) {
        if (state == 0) {
            return "<div style='color:yellow'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color:green'>已分配</div>";
        } else {
            return "<div style='color:red'>未知</div>";
        }
    }

    // 开发状态格式化
    function formatterDevResult(value) {
        if (value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (value == 1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if (value == 2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if (value == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>";
        }
    }

});
