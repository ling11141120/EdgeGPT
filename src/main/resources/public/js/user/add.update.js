layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer, // 优化了layer的获取方式
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    /**
     * 添加或更新用户
     */
    form.on("submit(addOrUpdateUser)", function (data) {
        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});

        // 获取隐藏域id的值
        var userId = $("input[name='id']").val();
        var url;

        // 1. 获取选中的角色ID，拼接成字符串
        var roleIds = layui.formSelects.value('selectId', 'val'); // selectId 是你 formSelects 的 lay-filter

        // 2. 添加到 data.field 中
        data.field.roleIds = roleIds.join(","); // 逗号分隔字符串 "1,2,3"

        // 根据id是否有值来判断是添加操作还是更新操作
        if (userId) {
            url = ctx + "/user/update";
        } else {
            url = ctx + "/user/add";
        }

        // 发送POST请求
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
        return false;
    });


    //加载下拉框
    var userId=$("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?userId="+userId,
        //⾃定义返回数据中name的key, 默认 name
        keyName: 'roleName',
        //⾃定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
        parent.layer.close(index); // 再执行关闭
    });
});