package ru.softlab.efr.services.insurance.converter;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.StatementEntity;
import ru.softlab.efr.services.insurance.model.rest.Statement;

@Service
public class StatementConverter {

    public Statement convertToStatement(StatementEntity statementEntity) {
        Statement statement = new Statement();
        statement.setId(statementEntity.getId());
        statement.setCode(statementEntity.getInsuranceStatus().getCode().toString());
        statement.setInfo(statementEntity.getComment());
        statement.setStatementCompleteStatus(statementEntity.getStatementCompleteStatus());
        return statement;
    }

}
