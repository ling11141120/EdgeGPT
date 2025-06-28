layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    // 进行登录操作
    // 监听保存按钮
    form.on('submit(saveBtn)', function(data){
        console.log(data.field);

        // 发送ajax请求
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                oldPassword:data.field.old_password,
                newPassword:data.field.new_password,
                repeatPassword:data.field.again_password
            },
            success:function (result) {
                if(result.code==200){
                    layer.msg("用户密码修改成功", {icon: 6},function () {
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/"});
                        $.removeCookie("userName",{domain:"localhost",path:"/"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/"});
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    layer.msg(result.msg, {icon: 5});
                }
            }
        })
    });

})