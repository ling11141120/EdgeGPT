package com.xxxx.crm.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Tony on 2016/8/23.
 */
public class LoginUserUtil {

    /**
     * 从cookie中获取userId
     * @param request
     * @return userId（获取不到返回 0）
     */
    public static int releaseUserIdFromCookie(HttpServletRequest request) {
        try {
            String userIdStr = CookieUtil.getCookieValue(request, "userIdStr");

            if (StringUtils.isBlank(userIdStr)) {
                // Cookie 不存在或为空，直接返回 0
                return 0;
            }

            Integer userId = UserIDBase64.decoderUserID(userIdStr);
            return userId != null ? userId : 0;

        } catch (Exception e) {
            // 如果发生解析异常，返回 0（或可抛出自定义异常）
            System.out.println("LoginUserUtil 解析 userId 异常：" + e.getMessage());
            return 0;
        }
    }
}
