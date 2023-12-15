package ru.softlab.efr.services.insurance.services;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.pojo.Result;
import ru.softlab.efr.services.insurance.pojo.StatusModel;
import ru.softlab.efr.services.insurance.pojo.Transition;

import javax.annotation.PostConstruct;

import java.time.LocalDate;

import static ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode.*;

/**
 * Сервис для построения статусной модели
 */
@Service
public class StatusModelBuilder {

    private StatusModel defaultStatusModel;

    @PostConstruct
    public void init() {
        defaultStatusModel = getDefaultModel4RshbInsLife();
    }


    public StatusModel getModelByProgramKind(ProgramKind programKind) {
        if (programKind == null) return defaultStatusModel;
        if (programKind == ProgramKind.KSP) {
            return getModelForKSP();
        }
        if (programKind == ProgramKind.HOME) {
            return getDefaultModel4RshbIns();
        }
        if (programKind == ProgramKind.SMS) {
            return getSmsModel();
        }
        return defaultStatusModel;
    }

    private StatusModel getSmsModel() {
        StatusModel statusModel = new StatusModel();
        //---Переходы в статус оформлен
        statusModel.addTransition(null, new Transition(MADE));
        //---Переходы из статуса оформлен
        statusModel.addTransition(MADE, new Transition(CRM_IMPORTED));
        statusModel.addTransition(MADE, new Transition(DRAFT, Right.SET_DRAFT_FROM_MADE));

        //---Переходы из статуса Выгружен в CRM (=Не оплачен)
        statusModel.addTransition(CRM_IMPORTED, new Transition(PAYED, Right.SET_PAYED_AND_FINISHED_STATUS));
        statusModel.addTransition(CRM_IMPORTED, new Transition(DRAFT, Right.SET_DRAFT_FROM_CRM_IMPORTED));

        statusModel.addTransition(DRAFT, new Transition(MADE));
        statusModel.addTransition(DRAFT, new Transition(CRM_IMPORTED));

        //---Переходы из статуса Оплачен
        statusModel.addTransition(PAYED, new Transition(CANCELED, Right.SET_PAYED_AND_FINISHED_STATUS));

        return statusModel;
    }

    public StatusModel getDefaultModel4RshbIns() {
        StatusModel statusModel = new StatusModel();
        //---Переходы в статус не оплачен
        statusModel.addTransition(null, new Transition(NOT_PAYED));
        //---Переходы из статуса не оплачен
        statusModel.addTransition(NOT_PAYED, new Transition(DRAFT));
        statusModel.addTransition(NOT_PAYED, new Transition(NOT_ACCEPTED));
        statusModel.addTransition(NOT_PAYED, new Transition(PAYED));

        //---Переходы из статуса Черновик
        statusModel.addTransition(DRAFT, new Transition(NOT_PAYED));

        //---Переходы из статуса Не акцептован
        statusModel.addTransition(NOT_ACCEPTED, new Transition());

        //---Переходы из статуса Оплачен
        statusModel.addTransition(PAYED, new Transition(CHANGING_APPLICATION_RECEIVED));
        statusModel.addTransition(PAYED, new Transition(FINISHED));

        //---Переходы из статуса Получено заявление на изменение договора
        statusModel.addTransition(CHANGING_APPLICATION_RECEIVED, new Transition(PAYED));
        statusModel.addTransition(CHANGING_APPLICATION_RECEIVED, new Transition(REVOKED));

        //---Переходы из статуса Аннулирован
        statusModel.addTransition(REVOKED, new Transition());

        //---Переходы из статуса Окончен
        statusModel.addTransition(FINISHED, new Transition());
        return statusModel;
    }

    public StatusModel getModelForKSP() {
        StatusModel kspStatusMode = getDefaultModel4RshbInsLife();
        //для КЦ из черновика в Проект
        Transition transition = kspStatusMode.getTransition(DRAFT, PROJECT);
        transition.addRight(Right.CREATE_CONTRACT_IN_CALL_CENTER);
        //для КСП надо же из Черновика сразу в Оформлен для всех кроме КЦ
        transition = new Transition(MADE);
        transition.addRightExcluded(Right.CREATE_CONTRACT_IN_CALL_CENTER);
        kspStatusMode.addTransition(DRAFT, transition);
        kspStatusMode.deleteTransition(PROJECT, MADE_NOT_COMPLETED);
        kspStatusMode.addTransition(PROJECT, transition);
        return kspStatusMode;
    }

    private StatusModel getDefaultModel4RshbInsLife() {
        StatusModel statusModel = new StatusModel();
        //---Переходы в статус черновик
        statusModel.addTransition(null, new Transition(DRAFT));
        //---Переходы из статуса черновик
        statusModel.addTransition(DRAFT, new Transition(PROJECT));
        statusModel.addTransition(DRAFT, new Transition(CLIENT_REFUSED));
        //---Переходы из статуса Проект
        statusModel.addTransition(PROJECT, new Transition(DRAFT));
        Transition transition = new Transition(MADE_NOT_COMPLETED);
        transition.addRightExcluded(Right.CREATE_CONTRACT_IN_CALL_CENTER);
        statusModel.addTransition(PROJECT, transition);
        statusModel.addTransition(PROJECT, new Transition(CLIENT_REFUSED));
        //---Переходы из статуса Оформление не завершено
        statusModel.addTransition(MADE_NOT_COMPLETED, new Transition(MADE));
        statusModel.addTransition(MADE_NOT_COMPLETED, new Transition(REVOKED_REPLACEMENT, Right.EDIT_USERS));
        //---Переходы из статуса Оформлен
        statusModel.addTransition(MADE, new Transition(NEED_WITHDRAW_APPLICATION));
        statusModel.addTransition(MADE, new Transition(WITHDRAW_APPLICATION_RECEIVED));
        statusModel.addTransition(MADE, new Transition(CHANGING_APPLICATION_RECEIVED));
        statusModel.addTransition(MADE, new Transition(REVOKED, Right.EDIT_USERS));
        statusModel.addTransition(MADE, new Transition(REVOKED_REPLACEMENT, Right.EDIT_USERS));

        transition = new Transition(CANCELLATION_APPLICATION_RECEIVED);
        transition.setBefore(insurance -> {
            if (insurance.getStartDate() != null && (insurance.getStartDate().isBefore(LocalDate.now()) || insurance.getStartDate().isEqual(LocalDate.now())) ) {
                return Result.success();
            } else {
                return Result.fail("Период охлаждения еще не прошел");
            }
        });
        statusModel.addTransition(MADE, transition);

        transition = new Transition(REFUSING_APPLICATION_RECEIVED);
        transition.setBefore(insurance -> {
            LocalDate startDate = insurance.getStartDate();
            if (insurance.getStartDate() == null || startDate.isAfter(LocalDate.now())) {
                return Result.success();
            } else {
                return Result.fail("Период охлаждения еще не прошел");
            }
        });
        statusModel.addTransition(MADE, transition);

        statusModel.addTransition(MADE, new Transition(FINISHED));
        //Переходы из статуса Требуется заявление о выплате
        statusModel.addTransition(NEED_WITHDRAW_APPLICATION, new Transition(WITHDRAW_APPLICATION_RECEIVED));
        //Переходы из статуса Получено заявление о выплате
        statusModel.addTransition(WITHDRAW_APPLICATION_RECEIVED, new Transition(PAYMENT_FULFILLED, Right.EDIT_USERS));
        //Переходы из статуса Выплата произведена
        statusModel.addTransition(PAYMENT_FULFILLED, new Transition(MADE, Right.EDIT_USERS));
        statusModel.addTransition(PAYMENT_FULFILLED, new Transition(FINISHED, Right.EDIT_USERS));
        //Переходы из статуса Аннулирован
        statusModel.addTransition(REVOKED, new Transition(MADE, Right.EDIT_USERS));
        //Переходы из статуса Получено заявление на изменение договора
        statusModel.addTransition(CHANGING_APPLICATION_RECEIVED, new Transition(MADE, Right.EDIT_USERS));
        //Переходы из статуса Получено заявление об отказе
        statusModel.addTransition(REFUSING_APPLICATION_RECEIVED, new Transition(CANCELED_IN_HOLD_PERIOD, Right.EDIT_USERS));
        statusModel.addTransition(REFUSING_APPLICATION_RECEIVED, new Transition(MADE, Right.EDIT_USERS));
        //Переходы из статуса Получено заявление о расторжении договора
        statusModel.addTransition(CANCELLATION_APPLICATION_RECEIVED, new Transition(CANCELED, Right.EDIT_USERS));
        statusModel.addTransition(CANCELLATION_APPLICATION_RECEIVED, new Transition(MADE, Right.EDIT_USERS));
        //Переходы из статуса Расторгнут
        statusModel.addTransition(CANCELED, new Transition(MADE, Right.EDIT_USERS));
        //Переходы из статуса Расторгнут в период охлаждения
        statusModel.addTransition(CANCELED_IN_HOLD_PERIOD, new Transition(MADE, Right.EDIT_USERS));
        //Переходы из статуса Отказ клиента
        statusModel.addTransition(CLIENT_REFUSED, new Transition());
        //Переходы из статуса Окончен
        statusModel.addTransition(FINISHED, new Transition());
        //Переходы из статуса Анулирование по замене
        statusModel.addTransition(REVOKED_REPLACEMENT, new Transition());
        return statusModel;
    } 
}
