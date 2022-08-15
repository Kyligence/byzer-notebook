package io.kyligence.notebook.console.service.hook;

import io.kyligence.notebook.console.service.EngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExecutionHookRegistry {
    private Set<BeforeExecutionHook> beforeExecutionHooks = new HashSet<>();

    public boolean registerBeforeExecutionHook(BeforeExecutionHook beforeHook) {
        if( beforeHook != null ) {
            log.info("Adding BeforeExecutionHook {}", beforeHook.getName() );
            return beforeExecutionHooks.add(beforeHook);
        }
        else {
            log.warn("Failed to add BeforeExecutionHook {}", beforeHook.getName() );
            return false;
        }

    }

    public void runBeforeExecutionHooks(EngineService.RunScriptParams params ) {
        for (BeforeExecutionHook beforeExecutionHook : beforeExecutionHooks) {
            log.info("Running BeforeExecutionHook {}", beforeExecutionHook.getName());
            beforeExecutionHook.run(params);
        }
    }
}
