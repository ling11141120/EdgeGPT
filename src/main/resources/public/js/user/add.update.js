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

        // 根据id是否有值来判断是添加操作还是更新操作
        if (userId) { // 如果id有值，说明是更新操作
            url = ctx + "/user/update"; // <--- 修正：调用更新接口
        } else { // 如果id没有值，说明是添加操作
            url = ctx + "/user/add"; // <--- 修正：调用添加接口
        }

        // 发送POST请求
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    // 关闭所有iframe层
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5}); // 错误信息
            }
        });
        return false; // 阻止表单跳转
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