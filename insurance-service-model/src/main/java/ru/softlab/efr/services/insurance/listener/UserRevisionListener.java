/*
 * MIT License
 *
 * Copyright (c) 2017 JUAN CALVOPINA M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

/**
 * This class sets the userId attribute in the Audit table
 */
package ru.softlab.efr.services.insurance.listener;

import org.hibernate.envers.RevisionListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.model.db.AuditEnversInfo;

/**
 * Listener for hibernate envers
 */
@Component
public class UserRevisionListener implements RevisionListener, ApplicationContextAware {

    /**
     * Ссылка на spring-контекст приложения.
     * Поле специально сделано статическим, по той причине, что экземпляр UserRevisionListener создаёт Hibernate Envers,
     * который ничего не знает про Spring.
     */
    private static ApplicationContext ac;

    @Override
    public void newRevision(Object revisionEntity) {
        AuditEnversInfo auditEnversInfo = (AuditEnversInfo) revisionEntity;
        if (ac != null) {
            PrincipalDataSource principalDataSource = ac.getBean(PrincipalDataSource.class);
            if (principalDataSource != null && principalDataSource.getPrincipalData() != null) {
                auditEnversInfo.setUserId(principalDataSource.getPrincipalData().getId());
            }
        }
    }

    @java.lang.SuppressWarnings("squid:S2696")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ac = applicationContext;
    }
}
