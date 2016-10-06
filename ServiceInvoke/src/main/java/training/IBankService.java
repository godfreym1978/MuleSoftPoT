package training;

import javax.jws.WebService;

@WebService
public interface IBankService {
	MoneyResponse sendMoney(MoneyRequest request);
}