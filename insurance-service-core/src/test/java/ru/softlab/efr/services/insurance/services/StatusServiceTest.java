package ru.softlab.efr.services.insurance.services;

import org.junit.Before;
import org.junit.Test;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode.*;

public class StatusServiceTest {

    private StatusService statusService = new StatusService(null);
    private PrincipalDataImpl principalData;
    private PrincipalDataImpl adminPrincipalData;

    @Before
    public void init() {
        principalData = new PrincipalDataImpl();
        adminPrincipalData = new PrincipalDataImpl();
        adminPrincipalData.setRights(Collections.singletonList(Right.EDIT_USERS));
        StatusModelBuilder statusModelBuilder = new StatusModelBuilder();
        statusModelBuilder.init();
        statusService.setStatusModelBuilder(statusModelBuilder);
    }

    @Test
    public void getTransitionForDraft() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(DRAFT), principalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(PROJECT));
        assertTrue(statusCodes.contains(CLIENT_REFUSED));
    }

    @Test
    public void getTransitionForProject() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(PROJECT), principalData);
        assertEquals(statusCodes.size(), 3);
        assertTrue(statusCodes.contains(DRAFT));
        assertTrue(statusCodes.contains(MADE_NOT_COMPLETED));
        assertTrue(statusCodes.contains(CLIENT_REFUSED));
    }

    @Test
    public void getTransitionForMadeNotCompleted() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(MADE_NOT_COMPLETED), principalData);
        assertEquals(statusCodes.size(), 1);
        assertTrue(statusCodes.contains(MADE));
    }

    @Test
    public void getTransitionForMadeNotCompletedForAdmin() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(MADE_NOT_COMPLETED), adminPrincipalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(MADE));
        assertTrue(statusCodes.contains(REVOKED_REPLACEMENT));
    }

    @Test
    public void getTransitionForMadeWithNotEnoughRights() {
        Insurance insurance = getInsuranceWithStatus(MADE);
        insurance.setStartDate(LocalDate.now().minusDays(1));
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(insurance, principalData);
        assertEquals(5, statusCodes.size());
        assertTrue(statusCodes.contains(NEED_WITHDRAW_APPLICATION));
        assertTrue(statusCodes.contains(WITHDRAW_APPLICATION_RECEIVED));
        assertTrue(statusCodes.contains(CANCELLATION_APPLICATION_RECEIVED));
        assertTrue(statusCodes.contains(FINISHED));
        assertTrue(statusCodes.contains(CHANGING_APPLICATION_RECEIVED));
    }

    @Test
    public void getTransitionForMadeWithEnoughRights() {
        List<Right> rights = new ArrayList<>();
        rights.add(Right.EDIT_USERS);
        principalData.setRights(rights);
        Insurance insurance = getInsuranceWithStatus(MADE);
        insurance.setStartDate(LocalDate.now().minusDays(1));
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(insurance, principalData);
        assertEquals(7, statusCodes.size());
        assertTrue(statusCodes.contains(NEED_WITHDRAW_APPLICATION));
        assertTrue(statusCodes.contains(WITHDRAW_APPLICATION_RECEIVED));
        assertTrue(statusCodes.contains(CHANGING_APPLICATION_RECEIVED));
        assertTrue(statusCodes.contains(REVOKED));
        assertTrue(statusCodes.contains(CANCELLATION_APPLICATION_RECEIVED));
        assertTrue(statusCodes.contains(FINISHED));
        assertTrue(statusCodes.contains(REVOKED_REPLACEMENT));
    }


    @Test
    public void getTransitionForNeedWithdrawApplication() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(NEED_WITHDRAW_APPLICATION), principalData);
        assertEquals(statusCodes.size(), 1);
        assertTrue(statusCodes.contains(WITHDRAW_APPLICATION_RECEIVED));
    }


    @Test
    public void getTransitionForWithdrawApplicationReceived() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(WITHDRAW_APPLICATION_RECEIVED), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void getTransitionForWithdrawApplicationReceivedForAdmin() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(WITHDRAW_APPLICATION_RECEIVED), adminPrincipalData);
        assertEquals(statusCodes.size(), 1);
        assertTrue(statusCodes.contains(PAYMENT_FULFILLED));
    }

    @Test
    public void getTransitionForPaymentFulfilled() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(PAYMENT_FULFILLED), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void getTransitionForPaymentFulfilledForAdmin() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(PAYMENT_FULFILLED), adminPrincipalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(MADE));
        assertTrue(statusCodes.contains(FINISHED));
    }

    @Test
    public void getTransitionForRevoked() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(REVOKED), adminPrincipalData);
        assertEquals(statusCodes.size(), 1);
        assertTrue(statusCodes.contains(MADE));
    }

    @Test
    public void getTransitionForChanginApplicationReceived() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(CHANGING_APPLICATION_RECEIVED), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void getTransitionForChanginApplicationReceivedForAdmin() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(CHANGING_APPLICATION_RECEIVED), adminPrincipalData);
        assertEquals(statusCodes.size(), 1);
        assertTrue(statusCodes.contains(MADE));
    }

    @Test
    public void getTransitionForRefusingApplicationReceived() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(REFUSING_APPLICATION_RECEIVED), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void getTransitionForRefusingApplicationReceivedForAdmin() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(REFUSING_APPLICATION_RECEIVED), adminPrincipalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(CANCELED_IN_HOLD_PERIOD));
        assertTrue(statusCodes.contains(MADE));
    }

    @Test
    public void getTransitionForCancellationApplicationReceived() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(CANCELLATION_APPLICATION_RECEIVED), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void getTransitionForCancellationApplicationReceivedForAdmin() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(CANCELLATION_APPLICATION_RECEIVED), adminPrincipalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(MADE));
        assertTrue(statusCodes.contains(CANCELED));
    }

    @Test
    public void getTransitionForCanceled() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(CANCELED), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void getTransitionForCanceledInHoldPeriod() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getInsuranceWithStatus(CANCELED_IN_HOLD_PERIOD), principalData);
        assertEquals(statusCodes.size(), 0);
    }

    @Test
    public void testTransitionFromProjectToMadeForKC() {
        Insurance insurance = getInsuranceWithStatus(PROJECT);
        List<Right> rights = new ArrayList<>();
        rights.add(Right.CREATE_CONTRACT_IN_CALL_CENTER);
        principalData.setRights(rights);
        assertFalse(statusService.isTransitionAvailable(insurance, principalData, MADE).isSuccess());
    }

    @Test
    public void testTransitionFromProjectToMadeNotCompletedForAllExceptKC() {
        Insurance insurance = getInsuranceWithStatus(PROJECT);
        assertTrue(statusService.isTransitionAvailable(insurance, principalData, MADE_NOT_COMPLETED).isSuccess());
    }


    @Test
    public void testTransitionFromMadeNotCompletedToMadeForAllExceptKC() {
        Insurance insurance = getInsuranceWithStatus(MADE_NOT_COMPLETED);
        assertTrue(statusService.isTransitionAvailable(insurance, principalData, MADE).isSuccess());
    }


    //-------Тесты для КСП
    @Test
    public void testKSPTransitionsFromDraftForAllExceptKC() {
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getKSPInsuranceWithStatus(DRAFT), principalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(MADE));
        assertTrue(statusCodes.contains(CLIENT_REFUSED));
    }

    @Test
    public void testKSPTransitionsFromDraftForKC() {
        List<Right> rights = new ArrayList<>();
        rights.add(Right.CREATE_CONTRACT_IN_CALL_CENTER);
        principalData.setRights(rights);
        Set<InsuranceStatusCode> statusCodes = statusService.getAvailableStatuses(getKSPInsuranceWithStatus(DRAFT), principalData);
        assertEquals(statusCodes.size(), 2);
        assertTrue(statusCodes.contains(PROJECT));
        assertTrue(statusCodes.contains(CLIENT_REFUSED));
    }

    /*

    @Test
    public void availableTransition(){
        assertTrue(statusService.isAvailableTransition(null, InsuranceStatusCode.DRAFT));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.PROJECT));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.CLIENT_REFUSED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.DRAFT));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.CLIENT_REFUSED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.MADE));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.REVOKED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.CANCELED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.FINISHED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.MADE));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.FINISHED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.MADE));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.MADE));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.CANCELED));
        assertTrue(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.MADE));
    }

    @Test
    public void notAvailableTransitionForNewInsurance(){
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.MADE));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(null, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForDraft(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.MADE));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.DRAFT, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForProject(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PROJECT, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForMade(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.MADE, InsuranceStatusCode.PAYMENT_FULFILLED));
    }


    @Test
    public void notAvailableTransitionForRevoced(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REVOKED, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForChangingApplicationReceived(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.WITHDRAW_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForNeedWithdrowApplication(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.MADE));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.NEED_WITHDRAW_APPLICATION, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForPaymentFulfilled(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.PAYMENT_FULFILLED, InsuranceStatusCode.CANCELED));
    }

    @Test
    public void notAvailableTransitionForRefusingApplicationReceived(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.MADE));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForCencelationApplicationReceived(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.MADE));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void notAvailableTransitionForCenseled(){
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.CLIENT_REFUSED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.DRAFT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.PROJECT));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.CHANGING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.NEED_WITHDRAW_APPLICATION));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.PAYMENT_FULFILLED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.REVOKED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.REFUSING_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.CANCELLATION_APPLICATION_RECEIVED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.CANCELED));
        assertFalse(statusService.isAvailableTransition(InsuranceStatusCode.CANCELED, InsuranceStatusCode.FINISHED));
    }

    @Test
    public void getNextStatusForNull() throws Exception{
        insurance.getStatus().setCode(null);
        assertEquals(statusService.getStatus(insurance, principalData, InsuranceStatusCode.DRAFT), InsuranceStatusCode.DRAFT);
    }

    @Test
    public void getNextStatusFromDraftToProject() throws Exception{
        insurance.getStatus().setCode(InsuranceStatusCode.DRAFT);
        assertEquals(statusService.getStatus(insurance, principalData, InsuranceStatusCode.PROJECT), InsuranceStatusCode.PROJECT);
    }

    @Test
    public void getNextStatusFromDraftToClientRefused() throws Exception{
        insurance.getStatus().setCode(InsuranceStatusCode.DRAFT);
        assertEquals(statusService.getStatus(insurance, principalData, InsuranceStatusCode.CLIENT_REFUSED), InsuranceStatusCode.CLIENT_REFUSED);
    }

    @Test(expected = NotSupportedException.class)
    public void getNextStatusFromClientRefused() throws Exception{
        insurance.getStatus().setCode(InsuranceStatusCode.CLIENT_REFUSED);
        statusService.getStatus(insurance, principalData, InsuranceStatusCode.PROJECT);
    }

    @Test
    public void getNextStatusFromProjectToClientRefused() throws Exception{
        insurance.getStatus().setCode(InsuranceStatusCode.PROJECT);
        assertEquals(statusService.getStatus(insurance, principalData, InsuranceStatusCode.CLIENT_REFUSED), InsuranceStatusCode.CLIENT_REFUSED);
    }

    @Test
    public void getNextStatusFromProjectToMade() throws Exception{
        insurance.getStatus().setCode(InsuranceStatusCode.PROJECT);
        assertEquals(statusService.getStatus(insurance, principalData, InsuranceStatusCode.MADE), InsuranceStatusCode.MADE);
    }
*/

    private Insurance getInsuranceWithStatus(InsuranceStatusCode statusCode) {
        Insurance insurance = new Insurance();
        InsuranceStatus status = new InsuranceStatus();
        status.setCode(statusCode);
        status.setName(statusCode.getNameStatus());
        insurance.setStatus(status);
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        insurance.setProgramSetting(programSetting);
        return insurance;
    }

    private Insurance getKSPInsuranceWithStatus(InsuranceStatusCode statusCode) {
        Insurance insurance = getInsuranceWithStatus(statusCode);
        insurance.getProgramSetting().getProgram().setType(ProgramKind.KSP);
        return insurance;
    }
}
