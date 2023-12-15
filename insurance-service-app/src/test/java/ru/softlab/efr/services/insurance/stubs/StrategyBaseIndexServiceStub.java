package ru.softlab.efr.services.insurance.stubs;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.services.StrategyBaseIndexService;

@Primary
@Service
public class StrategyBaseIndexServiceStub extends StrategyBaseIndexService {

    @Override
    protected String getPathOrDefault() {
        return ShareServiceStub.class.getResource("/baseIndex.xlsx").getPath();
    }

    @Override
    protected boolean needReplicate() {
        return true;
    }
}
