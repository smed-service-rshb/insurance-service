package ru.softlab.efr.services.insurance.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.converter.DictConverter;
import ru.softlab.efr.services.insurance.model.enums.PaymentMethod;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.model.db.Risk;
import ru.softlab.efr.services.insurance.model.rest.RiskData;
import ru.softlab.efr.services.insurance.services.RiskService;

/**
 * Контроллер для работы со справочником рисков.
 *
 * @author Kalantaev
 * @since 02.10.2018
 */
@RestController
public class RisksController implements RisksApi {

    private static final Logger LOGGER = Logger.getLogger(RisksController.class);

    @Autowired
    private RiskService riskService;

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> createRisk(@RequestBody RiskData createRiskRq) {
        Risk risk = DictConverter.convert(createRiskRq);
        riskService.create(risk);
        return ResponseEntity.ok().build();
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<RiskData> getRisk(@PathVariable("id") Long id) {
        Risk risk = riskService.findById(id);
        if (risk != null) {
            return ResponseEntity.ok(DictConverter.convert(risk));
        } else {
            LOGGER.error(String.format("Не найдена запись справочника рисков по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> putRisk(@PathVariable("id") Long id, @RequestBody RiskData updateRiskRq) {
        Risk risk = riskService.findById(id);
        if (risk == null) {
            LOGGER.error(String.format("Не найдена запись справочника рисков по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
        risk.setProgramKind(ProgramKind.valueOf(updateRiskRq.getProgram().name()));
        risk.setName(updateRiskRq.getName());
        risk.setFullName(updateRiskRq.getFullName());
        risk.setStartDate(updateRiskRq.getStartDate());
        risk.setEndDate(updateRiskRq.getEndDate());
        if (updateRiskRq.getPaymentMethod() != null) {
            risk.setPaymentMethod(PaymentMethod.valueOf(updateRiskRq.getPaymentMethod().toString()));
        }
        risk.setBenefitsInsured(updateRiskRq.isBenefitsInsured());

        riskService.update(risk);

        return ResponseEntity.ok().build();
    }

    @Override
    @HasPermission(Permissions.VIEW_DICT)
    public ResponseEntity<Page<RiskData>> riskList(Pageable pageable) {
        Page<Risk> risks = riskService.findAll(pageable);
        return ResponseEntity.ok(risks.map(DictConverter::convert));
    }
}
