package ru.softlab.efr.services.insurance.services.promocode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.insurance.services.promocode.models.PromocodeRegisterModel;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

@Service
public class PromocodeApiConnectorService {

    private static final Logger LOGGER = Logger.getLogger(PromocodeApiConnectorService.class);

    @Value("${tm.promo-code.server-address:https://telemed.medsovetnik.online}")
    private String serverAddress;

    @Value("${tm.promo-code.partner-id:FRONT}")
    private String partnerId;

    @Value("${tm.promo-code.partner-id.salt:cq1MuG2DehG6oQIN}")
    private String salt;

    private OperationLogService operationLogService;


    public PromocodeApiConnectorService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    public OperationLogEntry registerInTm(PromocodeRegisterModel promocodeRegisterModel) {

        String roteStringBuilder = "/api/partner/" + partnerId + "/generateProductCode";

        String urlParameters = "active=" + promocodeRegisterModel.getActiveStatus() +
                "&productCode=" + promocodeRegisterModel.getProductCode() +
                "&productId=" + promocodeRegisterModel.getProductId();

        String jsonBuilder = "{\n" +
                "  \"_hash\": \"" + getSHA512String(serverAddress + roteStringBuilder + "&" + urlParameters) + "\",\n" +
                "  \"productCode\": \"" + promocodeRegisterModel.getProductCode() + "\",\n" +
                "  \"productId\": \"" + promocodeRegisterModel.getProductId() + "\",\n" +
                "  \"active\": \"" + promocodeRegisterModel.getActiveStatus() + "\"\n" +
                "}";

        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("REGISTER_PROMO_CODE");
        operationLogEntry.setOperationDescription("Регистрация промокода");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);

        try {
            URL url = new URL(serverAddress + roteStringBuilder);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonBuilder);
                wr.flush();
                int responseCode = conn.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    LOGGER.warn(String.format("Сервер телемедицины возвратил ошибку %s", conn.getResponseCode()));
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getErrorStream())));
                    String output;
                    StringBuilder response = new StringBuilder();
                    while ((output = br.readLine()) != null) {
                        LOGGER.info(String.format("Ответ сервера телемедицины: %s", output));
                        response.append(output);
                    }
                    operationLogEntry.setOperationParameter("request", jsonBuilder);
                    operationLogEntry.setOperationParameter("response", response.toString());
                    operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);

                } else {
                    LOGGER.info(String.format("Объект %s успешно отправлен на сервер телемедицины", promocodeRegisterModel.getProductCode()));
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    String output;
                    StringBuilder response = new StringBuilder();
                    while ((output = br.readLine()) != null) {
                        LOGGER.info(String.format("Ответ сервера телемедицины: %s", output));
                        response.append(output);
                    }
                    operationLogEntry.setOperationParameter("request", jsonBuilder);
                    operationLogEntry.setOperationParameter("response", response.toString());
                    operationLogEntry.setOperationState(OperationState.SUCCESS);
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            LOGGER.error("Ошибка при подключении к серверу телемедицины \n", e);
            operationLogEntry.setOperationParameter("request", jsonBuilder);
            operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
            return operationLogEntry;
        }
    }


    private String getSHA512String(String stringToEncript) {
        String generated = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(String.format("%s&%s", stringToEncript, salt).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1).toUpperCase());
            }
            generated = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Ошибка генерации _hash \n", e);
        }
        return generated;
    }
}
