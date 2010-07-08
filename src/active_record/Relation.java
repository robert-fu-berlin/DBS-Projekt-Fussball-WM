package active_record;

enum Relation {
	LESS_THAN, LESS_THAN_OR_EQUALS, EQUALS, GREATER_THAN_OR_EQUALS, GREATER_THAN, UNEQUALS;

	public String toString() {
		switch (this) {
			case LESS_THAN:
				return "<";

			case LESS_THAN_OR_EQUALS:
				return "<=";

			case EQUALS:
				return "=";

			case GREATER_THAN_OR_EQUALS:
				return ">=";

			case GREATER_THAN:
				return ">";

			case UNEQUALS:
				return "!=";
		}
		assert false;
		return null;
	}
}
