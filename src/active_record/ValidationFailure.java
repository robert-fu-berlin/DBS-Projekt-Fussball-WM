package active_record;

public class ValidationFailure {

	private final String reason;

	public ValidationFailure() {
		reason = "Unknown validation failure";
	}

	public ValidationFailure(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return reason;
	}
}
