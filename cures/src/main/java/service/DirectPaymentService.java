package service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import config.CcavenueConfig;
import model.PaymentGatewayTransaction;
import util.AesCryptUtil;
import util.HibernateUtil;

@Service
@Transactional
public class DirectPaymentService {

    @Autowired
    private CcavenueConfig ccavenueConfig;

    public Map<String, String> createPayment()
            throws Exception {

        HashMap<String, String> result =
                new HashMap<>();

        Session session = null;

        Transaction tx = null;

        try {

            // =====================================
            // ORDER ID
            // =====================================

            String orderId =
                    UUID.randomUUID()
                            .toString();

            // =====================================
            // CONFIG
            // =====================================

            String workingKey =
            		ccavenueConfig.getWorkingKey();

            String merchantId =
            		ccavenueConfig.getMerchantId();

            String accessCode =
            		ccavenueConfig.getAccessCode();

            String amount =
            		ccavenueConfig.getBaseAmount();

            String currency = "INR";

            // =====================================
            // REQUEST
            // =====================================

            String redirectUrl = "https://all-cures.com:444/cures/payment/ccavenue-direct-payment-updates";
    	   String cancelUrl = "";
            long currentTimeMillis = new Date().getTime();
            int ccaRequestTid = (int) currentTimeMillis;

            String ccaRequest = "ccaRequesttid=" + ccaRequestTid + "&merchant_id=" + merchantId + "&order_id=" + orderId
                    + "&currency=" + currency + "&amount=" + amount + "&redirect_url=" + redirectUrl + "&cancel_url="
                    + cancelUrl + "&language=EN";
            System.out.println(
                    "CCA REQUEST : " +
                    ccaRequest
            );

            // =====================================
            // ENCRYPT
            // =====================================

            AesCryptUtil aesUtil =
                    new AesCryptUtil(
                            workingKey
                    );

            String encRequest =
                    aesUtil.encrypt(
                            ccaRequest
                    );
            System.out.println(
                    "Encrypted CCA REQUEST : " +
                    encRequest
            );
            // =====================================
            // SAVE TRANSACTION
            // =====================================

            PaymentGatewayTransaction transactionEntity =
                    new PaymentGatewayTransaction();

            transactionEntity.setOrderId(
                    orderId
            );

            transactionEntity.setAmount(
                    new BigDecimal(amount)
            );

            transactionEntity.setCurrency(
                    currency
            );

            transactionEntity.setOrderStatus(
                    "PENDING"
            );

            transactionEntity.setPaymentType(
                    "DIRECT_AD_PAYMENT"
            );
            transactionEntity.setCreatedDate(
                    new Date()
            );

            session =
                    HibernateUtil
                            .buildSessionFactory();

            tx =
                    session.beginTransaction();

            session.save(
                    transactionEntity
            );

            tx.commit();

            // =====================================
            // RESPONSE
            // =====================================

            result.put(
                    "encRequest",
                    encRequest
            );

            result.put(
                    "accessCode",
                    accessCode
            );

            result.put(
                    "orderId",
                    orderId
            );

            return result;

        } catch (Exception e) {

            if (tx != null) {

                tx.rollback();
            }

            e.printStackTrace();

            throw new RuntimeException(
                    e.getMessage()
            );

        } 
    }
}
