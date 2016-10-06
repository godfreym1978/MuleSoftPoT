package training;

public class BankService implements IBankService {

	private final int MAX = 999999;

	private final int MIN = 100000;

	@Override
	public MoneyResponse sendMoney(MoneyRequest request) {
		MoneyResponse response = new MoneyResponse();
		response.setSender(request.getSender());
		response.setReceiver(request.getReceiver());
		response.setAmount(request.getAmount());
		response.setOrigin(request.getOrigin());
		response.setDestination(request.getDestination());
		response.setConfirmationNumber((int) (Math.random() * (MAX - MIN) + MIN));
		return response;
	}

}