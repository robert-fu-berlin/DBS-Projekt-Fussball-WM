package dbs_fussball.model;

import java.util.HashSet;
import java.util.Set;

import active_record.ActiveRecord;
import active_record.Inverse;

public class Event extends ActiveRecord {
	public enum Type {
		BEGIN, HALF_TIME, EXTRA_TIME, PENALTIES, END,

		/**
		 * Indicates a player exchange.
		 * Event.getPrimary() must return the Person that left the field.
		 * Event.getSecondary() must return the Person that entered the field.
		 */
		EXCHANGE,

		/**
		 * Indicates a foul.
		 * Event.getPrimary() must return the Person executing the foul.
		 * Event.getSecondary() should return the Person that was fouled.
		 */
		FOUL,

		/**
		 * Indicates a goal.
		 * Event.getPrimary() must return the Person that scored the goal.
		 * Event.getSecondary() must return <code>null</code>.
		 * <p>
		 * The goal is counted toward the goals of the Team the scorer belongs
		 * to.
		 */
		GOAL,

		/**
		 * Indicates an own goal.
		 * Event.getPrimary() must return the Person that scored the goal.
		 * Event.getSecondary() must return <code>null</code>.
		 * <p>
		 * The goal is counted toward the goals of the Team the scorer is
		 * competing against.
		 */
		OWN_GOAL,

		/**
		 * Indicates a red card.
		 * Event.getPrimary() must return the Person that received the card.
		 * Event.getSecondary() must return <code>null</code>.
		 * <p>
		 * A player that received a red card is no longer considered on the
		 * field.
		 */
		RED_CARD,

		/**
		 * Indicates a yellow card.
		 * Event.getPrimary() must return the Person that received the card.
		 * Event.getSecondary() must return <code>null</code>.
		 */
		YELLOW_CARD;
	}

	private String	annotation;

	private Person	primaryPerson, secondaryPerson;

	/**
	 * When the event happened, measured in seconds after the match�s start.
	 */
	private Float	time;

	private Type	type;

	/**
	 * Factory method for a begin event
	 * @return
	 */
	public static Event begin() {
		Event e = new Event();
		e.type = Type.BEGIN;
		e.time = 0.0f;
		return e;
	}

	/**
	 * Factory method for Events of Type {@link Event.Type.END}.
	 * 
	 * @param time
	 *            The time the match ended, in seconds after start.
	 */
	public static Event end(Float time) {
		Event e = new Event();
		e.type = Type.END;
		return e;
	}

	public static Event exchange(Float time, Person oldPlayer, Person newPlayer) {
		Event e = new Event();
		e.type = Type.EXCHANGE;
		e.time = time;
		e.primaryPerson = oldPlayer;
		e.secondaryPerson = newPlayer;
		return e;
	}

	public static Event foul(Float time, Person fouler) {
		return foul(time, fouler, null);
	}

	public static Event foul(Float time, Person fouler, Person foulee) {
		Event e = new Event();
		e.type = Type.FOUL;
		e.time = time;
		e.primaryPerson = fouler;
		e.secondaryPerson = foulee;
		return e;
	}

	public static Event goal(Float time, Person scorer) {
		Event e = new Event();
		e.type = Type.GOAL;
		e.time = time;
		e.primaryPerson = scorer;
		return e;
	}

	public static Event ownGoal(Float time, Person scorer) {
		Event e = new Event();
		e.type = Type.OWN_GOAL;
		e.time = time;
		e.primaryPerson = scorer;
		return e;
	}

	public static Event redCard(Float time, Person recipient) {
		Event e = new Event();
		e.type = Type.RED_CARD;
		e.time = time;
		e.primaryPerson = recipient;
		return e;
	}

	public static Event yellowCard(Float time, Person recipient) {
		Event e = new Event();
		e.type = Type.YELLOW_CARD;
		e.time = time;
		e.primaryPerson = recipient;
		return e;
	}

	/**
	 * Public constructor for reflection. Use Event�s static methods obtain
	 * instances of this class.
	 * TODO find a way to reduce the visibility of this constructor without
	 * messing up the ActiveRecordMapper
	 */
	public Event() {

	}


	public String getAnnotation() {
		return annotation;
	}

	/**
	 * Returns the primary person of this event. The precise meaning of this
	 * is dependent on this event�s {@link Type}.
	 * 
	 * @return
	 *         This event�s secondary person or <code>null</code> if there is
	 *         none.
	 */
	public Person getPrimary() {
		return primaryPerson;
	}

	/**
	 * Returns the secondary person of this event. The precise meaning of this
	 * is dependent on this event�s {@link Type}.
	 * 
	 * @return
	 *         This event�s secondary person or <code>null</code> if there is
	 *         none.
	 */
	public Person getSecondary() {
		return secondaryPerson;
	}

	public Float getTime() {
		return time;
	}

	public Type getType() {
		return type;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
}
