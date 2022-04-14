package io.kyligence.notebook.console.controller;

import io.kyligence.notebook.console.bean.dto.Response;
import io.kyligence.notebook.console.bean.dto.UserInfoDTO;
import io.kyligence.notebook.console.bean.dto.UserRegisterAndResetDTO;
import io.kyligence.notebook.console.bean.dto.req.UserJoinReq;
import io.kyligence.notebook.console.exception.ByzerException;
import io.kyligence.notebook.console.exception.ErrorCodeEnum;
import io.kyligence.notebook.console.service.UserService;
import io.kyligence.notebook.console.support.Permission;
import io.kyligence.notebook.console.util.Base64Utils;
import io.kyligence.notebook.console.support.EncryptUtils;
import io.kyligence.notebook.console.util.JacksonUtils;
import io.kyligence.notebook.console.util.WebUtils;
import io.kyligence.saas.iam.pojo.AuthInfo;
import io.kyligence.saas.iam.sdk.context.AuthContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Slf4j
@Validated
@RestController
@RequestMapping("api")
@Api("The documentation about operations on user")
public class UserController {

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



}
