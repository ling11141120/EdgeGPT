layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();


    //用户退出操作
    $(".login-out").click(function () {
        layer.confirm('真的要退出吗？',{icon: 3, title: '提示'}, function (index) {
            layer.close(index);
            //清空cookie
            $.removeCookie("userIdStr",{domain:"localhost",path:"/"});
            $.removeCookie("userName",{domain:"localhost",path:"/"});
            $.removeCookie("trueName",{domain:"localhost",path:"/"});
            //$.removeCookie("token");
            window.parent.location.href=ctx+"/index";
        });
    })

});