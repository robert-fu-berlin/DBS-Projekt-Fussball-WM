package dbs_fussball.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import active_record.ActiveRecord;

public class Cup extends ActiveRecord {

	private Match finalMatch, thirdPlaceMatch;
	private Match semiFinalMatch1, semiFinalMatch2;
	private Match quarterFinalMatch1, quarterFinalMatch2, quarterFinalMatch3, quarterFinalMatch4;
	private Match roundOfSixteenMatch1, roundOfSixteenMatch2, roundOfSixteenMatch3, roundOfSixteenMatch4,
				  roundOfSixteenMatch5, roundOfSixteenMatch6, roundOfSixteenMatch7, roundOfSixteenMatch8;

	private Set<Match>	matchesGroupA, matchesGroupB, matchesGroupC, matchesGroupD, matchesGroupE, matchesGroupF,
			matchesGroupG, matchesGroupH;
	private Set<Team>	teamGroupA, teamGroupB, teamGroupC, teamGroupD, teamGroupE, teamGroupF, teamGroupG, teamGroupH;
	private Set<Team> teams;

	/**
	 * Default constructor for reflection, should not be called
	 */
	public Cup() {
	}

	public Match getFinal() {
		return finalMatch;
	}

	public Match getThirdPlace() {
		return thirdPlaceMatch;
	}

	public Match getSemiFinal(int number) {
		if (number == 1)
			return semiFinalMatch1;
		else if (number == 2)
			return semiFinalMatch2;
		else
			throw new IllegalArgumentException();
	}

	public Match getQuarterFinal(int number) {
		switch (number) {
			case 1:
				return quarterFinalMatch1;
			case 2:
				return quarterFinalMatch2;
			case 3:
				return quarterFinalMatch3;
			case 4:
				return quarterFinalMatch4;
			default:
				throw new IllegalArgumentException();
		}
	}

	public Match getRoundOfSixteen(int number) {
		switch (number) {
			case 1:
				return roundOfSixteenMatch1;
			case 2:
				return roundOfSixteenMatch2;
			case 3:
				return roundOfSixteenMatch3;
			case 4:
				return roundOfSixteenMatch4;
			case 5:
				return roundOfSixteenMatch5;
			case 6:
				return roundOfSixteenMatch6;
			case 7:
				return roundOfSixteenMatch7;
			case 8:
				return roundOfSixteenMatch8;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static class CupBuilder {

		private List<Team>	teamGroupA	= new ArrayList<Team>(), teamGroupB = new ArrayList<Team>(),
				teamGroupC = new ArrayList<Team>(), teamGroupD = new ArrayList<Team>(),
				teamGroupE = new ArrayList<Team>(), teamGroupF = new ArrayList<Team>(),
				teamGroupG = new ArrayList<Team>(), teamGroupH = new ArrayList<Team>();

		public CupBuilder startDate(Date startDate) {
			return this;
		}

		public CupBuilder addCountryToGroup(FifaCountry country, char group) {
			group = Character.toUpperCase(group);
			if (!(group >= 'A') && !(group <= 'H'))
				throw new IllegalArgumentException("Group must be between A and G");

			switch (group) {
				case 'A':
					teamGroupA.add(new Team(country));
					break;
				case 'B':
					teamGroupB.add(new Team(country));
					break;
				case 'C':
					teamGroupC.add(new Team(country));
					break;
				case 'D':
					teamGroupD.add(new Team(country));
					break;
				case 'E':
					teamGroupE.add(new Team(country));
					break;
				case 'F':
					teamGroupF.add(new Team(country));
					break;
				case 'G':
					teamGroupG.add(new Team(country));
					break;
				case 'H':
					teamGroupH.add(new Team(country));
					break;
			}

			return this;
		}

		public Cup build() {
			Cup cup = new Cup();

			List<List<Team>> teamGroups = Arrays.asList(teamGroupA, teamGroupB, teamGroupC, teamGroupD, teamGroupE,
					teamGroupF, teamGroupG, teamGroupH);

			for (List<Team> set : teamGroups)
				if (set.size() != 4)
					throw new IllegalArgumentException();

			cup.teams = new HashSet<Team>();

			for (Team t : teamGroupA) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupA = new HashSet<Team>();
				cup.teamGroupA.add(t);
			}

			for (Team t : teamGroupB) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupB = new HashSet<Team>();
				cup.teamGroupB.add(t);
			}

			for (Team t : teamGroupC) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupC = new HashSet<Team>();
				cup.teamGroupC.add(t);
			}

			for (Team t : teamGroupD) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupD = new HashSet<Team>();
				cup.teamGroupD.add(t);
			}

			for (Team t : teamGroupE) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupE = new HashSet<Team>();
				cup.teamGroupE.add(t);
			}

			for (Team t : teamGroupF) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupF = new HashSet<Team>();
				cup.teamGroupF.add(t);
			}

			for (Team t : teamGroupG) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupG = new HashSet<Team>();
				cup.teamGroupG.add(t);
			}

			for (Team t : teamGroupH) {
				if (cup.teams.contains(t))
					throw new IllegalArgumentException();
				cup.teams.add(t);
				cup.teamGroupH = new HashSet<Team>();
				cup.teamGroupH.add(t);
			}

			if (cup.teams.size() != 32)
				throw new IllegalArgumentException();

			cup.finalMatch = new Match(cup);
			cup.thirdPlaceMatch = new Match(cup);
			cup.semiFinalMatch1 = new Match(cup);
			cup.semiFinalMatch2 = new Match(cup);
			cup.quarterFinalMatch1 = new Match(cup);
			cup.quarterFinalMatch2 = new Match(cup);
			cup.quarterFinalMatch3 = new Match(cup);
			cup.quarterFinalMatch4 = new Match(cup);
			cup.roundOfSixteenMatch1 = new Match(cup);
			cup.roundOfSixteenMatch2 = new Match(cup);
			cup.roundOfSixteenMatch3 = new Match(cup);
			cup.roundOfSixteenMatch4 = new Match(cup);
			cup.roundOfSixteenMatch5 = new Match(cup);
			cup.roundOfSixteenMatch6 = new Match(cup);
			cup.roundOfSixteenMatch7 = new Match(cup);
			cup.roundOfSixteenMatch8 = new Match(cup);

			return cup;
		}

	}
}
