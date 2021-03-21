package capprezy.ua.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {
    private String action;
    private Integer paymentId;
    private String status;
    private String publicKey;
    private String orderId;
    private Double amount;
    private String currency;

    public Payment(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        action = jsonObject.get("action").getAsString();
        paymentId = jsonObject.get("payment_id").getAsInt();
        status = jsonObject.get("status").getAsString();
        publicKey = jsonObject.get("public_key").getAsString();
        orderId = jsonObject.get("order_id").getAsString();
        amount = jsonObject.get("amount").getAsDouble();
        currency = jsonObject.get("currency").getAsString();
    }
}
