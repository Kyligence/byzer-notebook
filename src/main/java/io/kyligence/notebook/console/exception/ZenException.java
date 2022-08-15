package io.kyligence.notebook.console.exception;

public class ZenException extends BaseException {

    public ZenException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getReportMsg());
        this.code = errorCodeEnum.getCode();
    }
}
