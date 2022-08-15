package io.kyligence.notebook.console.service.hook;

import io.kyligence.notebook.console.NotebookConfig;
import io.kyligence.notebook.console.exception.ErrorCodeEnum;
import io.kyligence.notebook.console.exception.ZenException;
import io.kyligence.notebook.console.service.EngineService;
import io.kyligence.saas.iam.pojo.AuthInfo;
import io.kyligence.saas.iam.sdk.context.AuthContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Slf4j
public class ZenTenantHook implements BeforeExecutionHook {
    @Autowired
    private ExecutionHookRegistry registry;

    @PostConstruct
    public void init() {
        register();
    }

    /**
     * Call zen api to get aws role and bucket by tenant_id.
     * And pass them to byzer-lang
     * @param params
     */
    @Override
    public void run(EngineService.RunScriptParams params) {
        // Get tenant_id from Context
        String tenantId = Optional.ofNullable(AuthContextHolder.getContext())
                .map( a -> a.getTenantId() ).orElse(null);

        if(StringUtils.isBlank( tenantId )) {
            throw new ZenException(ErrorCodeEnum.TENANT_ID_NOT_FOUND);
        }
        String tenantIdStmt = "SET tenant_id = " + tenantId + ";";
        log.info("tenantIdStmt {}", tenantIdStmt );
        AuthInfo userInfo = AuthContextHolder.getContext();
        String pathPrefixStmt = "SET path_prefix = "
                + NotebookConfig.getInstance().getUserHome() + "/" + userInfo.getEntityId()
                + ";";
        log.info("pathPrefixStmt {}", pathPrefixStmt);

        String originalSql = params.getAll().get("sql");
        String newSql = tenantIdStmt + System.lineSeparator()
                + pathPrefixStmt + System.lineSeparator()
                + originalSql;
        params.getAll().put("sql", newSql);

    }

    @Override
    public String getName() {
        return ZenTenantHook.class.getName();
    }

    @Override
    public void register() {
        registry.registerBeforeExecutionHook(this);
    }
}
