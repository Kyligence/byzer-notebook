package io.kyligence.notebook.console.controller;

import io.kyligence.notebook.console.bean.dto.Response;
import io.kyligence.notebook.console.bean.dto.UserInfoDTO;
import io.kyligence.notebook.console.service.UserService;
import io.kyligence.notebook.console.support.Permission;
import io.kyligence.saas.iam.pojo.AuthInfo;
import io.kyligence.saas.iam.sdk.context.AuthContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Validated
@RestController
@RequestMapping("api")
@Api("The documentation about operations on user")
public class UserController {

    public static final String IAM_TOKEN = "IAM_TOKEN";

    @Autowired
    private UserService userService;

    @Autowired
    private NotebookHelper notebookHelper;



    @ApiOperation("import sample")
    @PostMapping("/import/sample")
    @Permission
    protected void postSignup() {
        AuthInfo userInfo = AuthContextHolder.getContext();
        log.info("skip create sample for: " + userInfo.getUsername());
        notebookHelper.createSampleDemo(userInfo.getUsername());
    }


    @ApiOperation("Get User Info")
    @GetMapping("/user/me")
    @Permission
    public Response<UserInfoDTO> getUserInfo() {
        AuthInfo userInfo = AuthContextHolder.getContext();
        return new Response<UserInfoDTO>().data(UserInfoDTO.valueOf(userInfo));
    }

    @ApiOperation("user logout")
    @DeleteMapping("/user/authentication")
    @Permission
    public Response<String> logout(HttpServletResponse resp) {
        Cookie cookie = new Cookie(IAM_TOKEN, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
        return new Response<String>().data("");
    }


}
