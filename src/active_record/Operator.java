package active_record;

public enum Operator {
	AND, OR;

	@Override
	public String toString() {
		switch (this) {
		case AND:
			return "and";

		case OR:
			return "or";
		}
		assert false;
		return null;
	};
}
