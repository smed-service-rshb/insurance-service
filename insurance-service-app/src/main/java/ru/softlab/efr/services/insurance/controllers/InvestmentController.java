package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.config.Permissions;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.model.db.StrategyProperty;
import ru.softlab.efr.services.insurance.model.enums.StrategyType;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.ShareService;
import ru.softlab.efr.services.insurance.services.StrategyBaseIndexService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InvestmentController implements InvestmentApi {

    @Autowired
    private ShareService shareService;

    @Autowired
    private StrategyBaseIndexService baseIndexService;

    @Autowired
    private InsuranceService insuranceService;

    @Override
    @HasPermission(Permissions.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<SharesDataRs> getShare(@NotNull @Valid @RequestParam(value = "contractId") Long contractId) throws Exception {
        // TODO Оптимизировать количество запросов к БД. Вероятно, можно обойтись одним запросом к БД.
        Insurance insurance = insuranceService.findById(contractId);
        Strategy strategy = insurance != null ? insurance.getStrategy() : null;
        if (strategy == null || strategy.getStrategyType() != StrategyType.COUPON) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(new SharesDataRs(
                shareService.getShareWithQuoteByDate(strategy.getId(), insurance.getStartDate())
                        .stream().map(InvestmentController::convert)
                        .collect(Collectors.toList())));
    }

    @Override
    @HasPermission(Permissions.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<byte[]> getShareReport(@NotNull @Valid @RequestParam(value = "contractId") Long contractId) throws Exception {
        Insurance insurance = insuranceService.findById(contractId);
        Strategy strategy = insurance != null ? insurance.getStrategy() : null;
        if (strategy == null || strategy.getStrategyType() != StrategyType.COUPON) {
            return ResponseEntity.badRequest().build();
        }
        byte[] excel = shareService.getExcelReport(insurance);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="
                        + URLEncoder.encode("Отчет о котировках акций.xlsx", "UTF-8").replaceAll("\\+", " "))
                .body(excel);
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> loadShare() throws Exception {
        shareService.runUpdate();
        return ResponseEntity.ok().build();
    }


    @HasPermission(Permissions.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<InvestmentIncomeDataRs> getInvestmentIncome(@RequestParam(value = "contractId") Long contractId) throws Exception {
        // TODO Оптимизировать количество запросов к БД. Вероятно, можно обойтись одним запросом к БД.
        Insurance insurance = insuranceService.findById(contractId);
        Strategy strategy = insurance != null ? insurance.getStrategy() : null;
        if (strategy == null ||
                !(strategy.getStrategyType() == StrategyType.CLASSIC || strategy.getStrategyType() == StrategyType.LOCOMOTIVE)
                || strategy.getStrategyProperties().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        LocalDate startDate = insurance.getStartDate();
        LocalDate endDate = LocalDate.now();
        InvestmentIncomeDataRs response = new InvestmentIncomeDataRs();
        StrategyType type = strategy.getStrategyType();
        if (strategy.getStrategyProperties().size() == 1) {
            List<BaseIndex> indexes = baseIndexService.getIndexByStrategyAndDateBetween(strategy.getId(), startDate, endDate);
            response.addIncomesSetItem(new InvestmentIncomeDataRsIncomesSet(strategy.getId(), strategy.getName(),
                    strategy.getStrategyProperties().get(0).getTicker(),
                    type == StrategyType.LOCOMOTIVE ? baseIndexService.getLocomotiveIncome(indexes) : "",
                    baseIndexService.calculateIncomes(insurance, indexes, type)));
        } else {
            strategy.getStrategyProperties().forEach(i -> {
                List<BaseIndex> indexes = baseIndexService.getIndexByStrategyAndDateBetween(i.getBaseIndexSource().getId(), startDate, endDate);
                response.addIncomesSetItem(new InvestmentIncomeDataRsIncomesSet(i.getBaseIndexSource().getId(), i.getBaseIndexSource().getName(),
                        i.getTicker(),
                        type == StrategyType.LOCOMOTIVE ? baseIndexService.getLocomotiveIncome(indexes) : "",
                        baseIndexService.calculateIncomes(insurance, indexes, type)));
            });
        }

        return ResponseEntity.ok(response);
    }

    @Override
    @HasPermission(Permissions.CLIENT_VIEW_CONTRACT)
    public ResponseEntity<byte[]> getInvestmentIncomeReport(@NotNull @Valid @RequestParam(value = "contractId") Long contractId,
                                                            @NotNull @Valid @RequestParam(value = "strategyId") Long strategyId) throws Exception {
        Insurance insurance = insuranceService.findById(contractId);
        Strategy strategy = insurance != null ? insurance.getStrategy() : null;
        if (strategy == null ||
                !(strategy.getStrategyType() == StrategyType.CLASSIC || strategy.getStrategyType() == StrategyType.LOCOMOTIVE)
                || strategy.getStrategyProperties().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        byte[] excel;
        if (strategy.getStrategyProperties().size() == 1 && strategy.getId().equals(strategyId)) {
            excel = baseIndexService.getExcelReport(insurance, strategy);
        } else {
            StrategyProperty strategyProperty = strategy.getStrategyProperties()
                    .stream()
                    .filter(i -> i.getBaseIndexSource() != null && i.getBaseIndexSource().getId().equals(strategyId))
                    .findFirst().orElse(null);
            if (strategyProperty != null) {
                excel = baseIndexService.getExcelReport(insurance, strategyProperty.getBaseIndexSource());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="
                        + URLEncoder.encode("Отчет по базовым активам.xlsx", "UTF-8").replaceAll("\\+", " "))
                .body(excel);
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> loadBaseIndex() throws Exception {
        baseIndexService.runUpdate();
        return ResponseEntity.ok().build();
    }

    public static SharesData convert(ShareEntity shareEntity) {
        SharesData share = new SharesData();
        share.setId(shareEntity.getId());
        share.setName(shareEntity.getName());
        share.setDescription(shareEntity.getDescription());
        if (shareEntity.getQuotes() != null && !shareEntity.getQuotes().isEmpty()) {
            BigDecimal startQuoteValue = shareEntity.getQuotes().iterator().next().getValue();
            share.setQuotes(shareEntity.getQuotes().stream().map(quote -> {
                QuoteData quoteData = new QuoteData();
                quoteData.setDate(quote.getDate());
                quoteData.setValue(quote.getValue());
                quoteData.setRelativeValue(quote.getValue()
                        .divide(startQuoteValue, 4, RoundingMode.HALF_UP)
                        .subtract(BigDecimal.ONE)
                        .multiply(BigDecimal.valueOf(100)));
                return quoteData;
            }).collect(Collectors.toList()));
        }
        return share;
    }

}
