package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CalculateApplicationService {
    public int calculate(Purchase purchase) {
        return purchase.subtotal();
    }
}
