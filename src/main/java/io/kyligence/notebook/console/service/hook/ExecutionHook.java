package io.kyligence.notebook.console.service.hook;

import io.kyligence.notebook.console.service.EngineService;

public interface ExecutionHook {
    void run(EngineService.RunScriptParams params);
    String getName();
    void register();
}
