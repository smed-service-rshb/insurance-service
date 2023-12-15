package ru.softlab.efr.services.insurance.stubs;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.services.ShareService;
@Primary
@Service
public class ShareServiceStub extends ShareService {

    @Override
    protected String getPath() {
        return ShareServiceStub.class.getResource("/share.xlsx").getPath();
    }

    @Override
    protected boolean needReplicate() {
        return true;
    }
}
