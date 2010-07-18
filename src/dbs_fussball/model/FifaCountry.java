package dbs_fussball.model;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * Contains a list of all current Fifa country codes as well as codes of former members
 * taken from this list http://www.rsssf.com/miscellaneous/fifa-codes.html
 *
 * @author Robert Böhnke
 *
 */

public enum FifaCountry {
	/*
	 * Africa
	 */

	/**
	 * Algeria
	 */
	ALGERIA("ALG"),

	/**
	 * Angola
	 */
	ANGOLA("ANG"),

	/**
	 * Benin
	 */
	BENIN("BEN"),

	/**
	 * Botswana
	 */
	BOTSWANA("BOT"),

	/**
	 * Burkina Faso
	 */
	BURKINA_FASO("BFA"),

	/**
	 * Burundi
	 */
	BURUNDI("BDI"),

	/**
	 * Cameroon
	 */
	CAMEROON("CMR"),

	/**
	 * Cape Verde Islands
	 */
	CAPE_VERDE_ISLANDS("CPV"),

	/**
	 * Central African Republic
	 */
	CENTRAL_AFRICAN_REPUBLIC("CTA"),

	/**
	 * Chad
	 */
	CHAD("CHA"),

	/**
	 * Comoros Islands
	 */
	COMOROS_ISLANDS("COM"),

	/**
	 * Congo
	 */
	CONGO("CGO"),

	/**
	 * Congo DR (Zaire)
	 */
	CONGO_DR_ZAIRE("COD"),

	/**
	 * Cote d’Ivoire
	 */
	COTE_DIVOIRE("CIV"),

	/**
	 * Djibouti
	 */
	DJIBOUTI("DJI"),

	/**
	 * Egypt
	 */
	EGYPT("EGY"),

	/**
	 * Equatorial Guinea
	 */
	EQUATORIAL_GUINEA("EQG"),

	/**
	 * Eritrea
	 */
	ERITREA("ERI"),

	/**
	 * Ethiopia
	 */
	ETHIOPIA("ETH"),

	/**
	 * Gabon
	 */
	GABON("GAB"),

	/**
	 * Gambia
	 */
	GAMBIA("GAM"),

	/**
	 * Ghana
	 */
	GHANA("GHA"),

	/**
	 * Guinea
	 */
	GUINEA("GUI"),

	/**
	 * Guinea – Bissau
	 */
	GUINEA__BISSAU("GNB"),

	/**
	 * Kenya
	 */
	KENYA("KEN"),

	/**
	 * Lesotho
	 */
	LESOTHO("LES"),

	/**
	 * Liberia
	 */
	LIBERIA("LBR"),

	/**
	 * Libya
	 */
	LIBYA("LBY"),

	/**
	 * Madagascar
	 */
	MADAGASCAR("MAD"),

	/**
	 * Malawi
	 */
	MALAWI("MWI"),

	/**
	 * Mali
	 */
	MALI("MLI"),

	/**
	 * Mauritania
	 */
	MAURITANIA("MTN"),

	/**
	 * Mauritius
	 */
	MAURITIUS("MRI"),

	/**
	 * Morocco
	 */
	MOROCCO("MAR"),

	/**
	 * Mozambique
	 */
	MOZAMBIQUE("MOZ"),

	/**
	 * Namibia
	 */
	NAMIBIA("NAM"),

	/**
	 * Niger
	 */
	NIGER("NIG"),

	/**
	 * Nigeria
	 */
	NIGERIA("NGA"),

	/**
	 * Rwanda
	 */
	RWANDA("RWA"),

	/**
	 * Sao Tome e Principe
	 */
	SAO_TOME_E_PRINCIPE("STP"),

	/**
	 * Senegal
	 */
	SENEGAL("SEN"),

	/**
	 * Seychelles
	 */
	SEYCHELLES("SEY"),

	/**
	 * Sierra Leone
	 */
	SIERRA_LEONE("SLE"),

	/**
	 * Somalia
	 */
	SOMALIA("SOM"),

	/**
	 * South Africa
	 */
	SOUTH_AFRICA("RSA"),

	/**
	 * Sudan
	 */
	SUDAN("SUD"),

	/**
	 * Swaziland
	 */
	SWAZILAND("SWZ"),

	/**
	 * Tanzania
	 */
	TANZANIA("TAN"),

	/**
	 * Togo
	 */
	TOGO("TOG"),

	/**
	 * Tunisia
	 */
	TUNISIA("TUN"),

	/**
	 * Uganda
	 */
	UGANDA("UGA"),

	/**
	 * Zambia
	 */
	ZAMBIA("ZAM"),

	/**
	 * Zimbabwe
	 */
	ZIMBABWE("ZIM"),

	/*
	 * Asia
	 */

	/**
	 * Afghanistan
	 */
	AFGHANISTAN("AFG"),

	/**
	 * Australia
	 */
	AUSTRALIA("AUS"),

	/**
	 * Bahrain
	 */
	BAHRAIN("BHR"),

	/**
	 * Bangladesh
	 */
	BANGLADESH("BAN"),

	/**
	 * Bhutan
	 */
	BHUTAN("BHU"),

	/**
	 * Brunei Darussalam
	 */
	BRUNEI_DARUSSALAM("BRU"),

	/**
	 * Cambodia
	 */
	CAMBODIA("CAM"),

	/**
	 * China PR
	 */
	CHINA_PR("CHN"),

	/**
	 * Chinese Taipei
	 */
	CHINESE_TAIPEI("TPE"),

	/**
	 * East Timor
	 */
	EAST_TIMOR("TLS"),

	/**
	 * Guam
	 */
	GUAM("GUM"),

	/**
	 * Hong Kong
	 */
	HONG_KONG("HKG"),

	/**
	 * India
	 */
	INDIA("IND"),

	/**
	 * Indonesia
	 */
	INDONESIA("IDN"),

	/**
	 * Iran
	 */
	IRAN("IRN"),

	/**
	 * Iraq
	 */
	IRAQ("IRQ"),

	/**
	 * Japan
	 */
	JAPAN("JPN"),

	/**
	 * Jordan
	 */
	JORDAN("JOR"),

	/**
	 * Korea DPR
	 */
	KOREA_DPR("PRK"),

	/**
	 * Korea Republic
	 */
	KOREA_REPUBLIC("KOR"),

	/**
	 * Kuwait
	 */
	KUWAIT("KUW"),

	/**
	 * Kyrgyzstan
	 */
	KYRGYZSTAN("KGZ"),

	/**
	 * Laos
	 */
	LAOS("LAO"),

	/**
	 * Lebanon
	 */
	LEBANON("LIB"),

	/**
	 * Macao
	 */
	MACAO("MAC"),

	/**
	 * Malaysia
	 */
	MALAYSIA("MAS"),

	/**
	 * Maldives
	 */
	MALDIVES("MDV"),

	/**
	 * Mongolia
	 */
	MONGOLIA("MGL"),

	/**
	 * Myanmar
	 */
	MYANMAR("MYA"),

	/**
	 * Nepal
	 */
	NEPAL("NEP"),

	/**
	 * Oman
	 */
	OMAN("OMA"),

	/**
	 * Pakistan
	 */
	PAKISTAN("PAK"),

	/**
	 * Palestine
	 */
	PALESTINE("PAL"),

	/**
	 * Philippines
	 */
	PHILIPPINES("PHI"),

	/**
	 * Qatar
	 */
	QATAR("QAT"),

	/**
	 * Saudi Arabia
	 */
	SAUDI_ARABIA("KSA"),

	/**
	 * Singapore
	 */
	SINGAPORE("SIN"),

	/**
	 * Sri Lanka
	 */
	SRI_LANKA("SRI"),

	/**
	 * Syria
	 */
	SYRIA("SYR"),

	/**
	 * Tajikistan
	 */
	TAJIKISTAN("TJK"),

	/**
	 * Thailand
	 */
	THAILAND("THA"),

	/**
	 * Turkmenistan
	 */
	TURKMENISTAN("TKM"),

	/**
	 * United Arab Emirates
	 */
	UNITED_ARAB_EMIRATES("UAE"),

	/**
	 * Uzbekistan
	 */
	UZBEKISTAN("UZB"),

	/**
	 * Vietnam
	 */
	VIETNAM("VIE"),

	/**
	 * Yemen
	 */
	YEMEN("YEM"),

	/*
	 * Europe
	 */

	/**
	 * Albania
	 */
	ALBANIA("ALB"),

	/**
	 * Andorra
	 */
	ANDORRA("AND"),

	/**
	 * Armenia
	 */
	ARMENIA("ARM"),

	/**
	 * Austria
	 */
	AUSTRIA("AUT"),

	/**
	 * Azerbaijan
	 */
	AZERBAIJAN("AZE"),

	/**
	 * Belarus
	 */
	BELARUS("BLR"),

	/**
	 * Belgium
	 */
	BELGIUM("BEL"),

	/**
	 * Bosnia Herzegovina
	 */
	BOSNIA_AND_HERZEGOVINA("BIH"),

	/**
	 * Bulgaria
	 */
	BULGARIA("BUL"),

	/**
	 * Croatia
	 */
	CROATIA("CRO"),

	/**
	 * Cyprus
	 */
	CYPRUS("CYP"),

	/**
	 * Czech Republic
	 */
	CZECH_REPUBLIC("CZE"),

	/**
	 * Denmark
	 */
	DENMARK("DEN"),

	/**
	 * England
	 */
	ENGLAND("ENG"),

	/**
	 * Estonia
	 */
	ESTONIA("EST"),

	/**
	 * Faeroe Islands
	 */
	FAEROE_ISLANDS("FRO"),

	/**
	 * Finland
	 */
	FINLAND("FIN"),

	/**
	 * France
	 */
	FRANCE("FRA"),

	/**
	 * Georgia
	 */
	GEORGIA("GEO"),

	/**
	 * Germany
	 */
	GERMANY("GER"),

	/**
	 * Greece
	 */
	GREECE("GRE"),

	/**
	 * Holland
	 */
	HOLLAND("NED"),

	/**
	 * Hungary
	 */
	HUNGARY("HUN"),

	/**
	 * Iceland
	 */
	ICELAND("ISL"),

	/**
	 * Israel
	 */
	ISRAEL("ISR"),

	/**
	 * Italy
	 */
	ITALY("ITA"),

	/**
	 * Kazakhstan
	 */
	KAZAKHSTAN("KAZ"),

	/**
	 * Latvia
	 */
	LATVIA("LVA"),

	/**
	 * Liechtenstein
	 */
	LIECHTENSTEIN("LIE"),

	/**
	 * Lithuania
	 */
	LITHUANIA("LTU"),

	/**
	 * Luxembourg
	 */
	LUXEMBOURG("LUX"),

	/**
	 * Macedonia FYR
	 */
	MACEDONIA_FYR("MKD"),

	/**
	 * Malta
	 */
	MALTA("MLT"),

	/**
	 * Moldova
	 */
	MOLDOVA("MDA"),

	/**
	 * Montenegro
	 */
	MONTENEGRO("MNE"),

	/**
	 * Northern Ireland
	 */
	NORTHERN_IRELAND("NIR"),

	/**
	 * Norway
	 */
	NORWAY("NOR"),

	/**
	 * Poland
	 */
	POLAND("POL"),

	/**
	 * Portugal
	 */
	PORTUGAL("POR"),

	/**
	 * Republic of Ireland
	 */
	REPUBLIC_OF_IRELAND("IRL"),

	/**
	 * Romania
	 */
	ROMANIA("ROU"),

	/**
	 * Russia
	 */
	RUSSIA("RUS"),

	/**
	 * San Marino
	 */
	SAN_MARINO("SMR"),

	/**
	 * Scotland
	 */
	SCOTLAND("SCO"),

	/**
	 * Serbia
	 */
	SERBIA("SRB"),

	/**
	 * Slovakia
	 */
	SLOVAKIA("SVK"),

	/**
	 * Slovenia
	 */
	SLOVENIA("SVN"),

	/**
	 * Spain
	 */
	SPAIN("ESP"),

	/**
	 * Sweden
	 */
	SWEDEN("SWE"),

	/**
	 * Switzerland
	 */
	SWITZERLAND("SUI"),

	/**
	 * Turkey
	 */
	TURKEY("TUR"),

	/**
	 * Ukraine
	 */
	UKRAINE("UKR"),

	/**
	 * Wales
	 */
	WALES("WAL"),

	/*
	 * North and Middle America
	 */

	/**
	 * Anguilla
	 */
	ANGUILLA("AIA"),

	/**
	 * Antigua & Barbuda
	 */
	ANTIGUA_AND_BARBUDA("ATG"),

	/**
	 * Aruba
	 */
	ARUBA("ARU"),

	/**
	 * Bahamas
	 */
	BAHAMAS("BAH"),

	/**
	 * Barbados
	 */
	BARBADOS("BRB"),

	/**
	 * Belize
	 */
	BELIZE("BLZ"),

	/**
	 * Bermuda
	 */
	BERMUDA("BER"),

	/**
	 * British Virgin Islands
	 */
	BRITISH_VIRGIN_ISLANDS("VGB"),

	/**
	 * Canada
	 */
	CANADA("CAN"),

	/**
	 * Cayman Islands
	 */
	CAYMAN_ISLANDS("CAY"),

	/**
	 * Costa Rica
	 */
	COSTA_RICA("CRC"),

	/**
	 * Cuba
	 */
	CUBA("CUB"),

	/**
	 * Dominica
	 */
	DOMINICA("DMA"),

	/**
	 * Dominican Republic
	 */
	DOMINICAN_REPUBLIC("DOM"),

	/**
	 * El Salvador
	 */
	EL_SALVADOR("SLV"),

	/**
	 * Grenada
	 */
	GRENADA("GRN"),

	/**
	 * Guatemala
	 */
	GUATEMALA("GUA"),

	/**
	 * Guyana
	 */
	GUYANA("GUY"),

	/**
	 * Haiti
	 */
	HAITI("HAI"),

	/**
	 * Honduras
	 */
	HONDURAS("HON"),

	/**
	 * Jamaica
	 */
	JAMAICA("JAM"),

	/**
	 * Mexico
	 */
	MEXICO("MEX"),

	/**
	 * Montserrat
	 */
	MONTSERRAT("MSR"),

	/**
	 * Netherland Antilles
	 */
	NETHERLAND_ANTILLES("ANT"),

	/**
	 * Nicaragua
	 */
	NICARAGUA("NCA"),

	/**
	 * Panama
	 */
	PANAMA("PAN"),

	/**
	 * Puerto Rico
	 */
	PUERTO_RICO("PUR"),

	/**
	 * St. Kitts & Nevis
	 */
	ST_KITTS_AND_NEVIS("SKN"),

	/**
	 * St. Lucia
	 */
	ST_LUCIA("LCA"),

	/**
	 * St Vincent & The Grenadines
	 */
	ST_VINCENT_AND_THE_GRENADINES("VIN"),

	/**
	 * Surinam
	 */
	SURINAM("SUR"),

	/**
	 * Trinidad & Tobago
	 */
	TRINIDAD_AND_TOBAGO("TRI"),

	/**
	 * Turks & Caicos Islands
	 */
	TURKS_AND_CAICOS_ISLANDS("TCA"),

	/**
	 * United States of America
	 */
	UNITED_STATES_OF_AMERICA("USA"),

	/**
	 * United States Virgin Islands
	 */
	UNITED_STATES_VIRGIN_ISLANDS("VIR"),

	/*
	 * Oceania
	 */

	/**
	 * American Samoa
	 */
	AMERICAN_SAMOA("ASA"),

	/**
	 * Cook Islands
	 */
	COOK_ISLANDS("COK"),

	/**
	 * Fiji
	 */
	FIJI("FIJ"),

	/**
	 * New Caledonia
	 */
	NEW_CALEDONIA("NCL"),

	/**
	 * New Zealand
	 */
	NEW_ZEALAND("NZL"),

	/**
	 * Papua New Guinea
	 */
	PAPUA_NEW_GUINEA("PNG"),

	/**
	 * Samoa
	 */
	SAMOA("SAM"),

	/**
	 * Solomon Islands
	 */
	SOLOMON_ISLANDS("SOL"),

	/**
	 * Tahiti
	 */
	TAHITI("TAH"),

	/**
	 * Tonga
	 */
	TONGA("TGA"),

	/**
	 * Vanuatu
	 */
	VANUATU("VAN"),

	/*
	 * South America
	 */

	/**
	 * Argentina
	 */
	ARGENTINA("ARG"),

	/**
	 * Bolivia
	 */
	BOLIVIA("BOL"),

	/**
	 * Brazil
	 */
	BRAZIL("BRA"),

	/**
	 * Chile
	 */
	CHILE("CHI"),

	/**
	 * Colombia
	 */
	COLOMBIA("COL"),

	/**
	 * Ecuador
	 */
	ECUADOR("ECU"),

	/**
	 * Paraguay
	 */
	PARAGUAY("PAR"),

	/**
	 * Peru
	 */
	PERU("PER"),

	/**
	 * Uruguay
	 */
	URUGUAY("URU"),

	/**
	 * Venezuela
	 */
	VENEZUELA("VEN"),

	/**
	 * Commonwealth of Independent States
	 */
	COMMONWEALTH_OF_INDEPENDENT_STATES("CIS"),

	/**
	 * Curacao
	 */
	CURACAO("CUR"),

	/**
	 * Czechoslovakia
	 */
	CZECHOSLOVAKIA("TCH"),

	/**
	 * East Germany
	 */
	EAST_GERMANY("DDR"),

	/**
	 * North Vietnam
	 */
	NORTH_VIETNAM("VNO"),

	/**
	 * North Yemen
	 */
	NORTH_YEMEN("NYE"),

	/**
	 * Saarland
	 */
	SAARLAND("SAA"),

	/**
	 * Serbia & Montenegro
	 */
	SERBIA_AND_MONTENEGRO("SCG"),

	/**
	 * South Vietnam
	 */
	SOUTH_VIETNAM("VSO"),

	/**
	 * South Yemen
	 */
	SOUTH_YEMEN("SYE"),

	/**
	 * Soviet Union
	 */
	SOVIET_UNION("URS"),

	/**
	 * West Germany
	 */
	WEST_GERMANY("FRG"),

	/**
	 * Yugoslavia
	 */
	YUGOSLAVIA("YUG"),

	/**
	 * Bohemia
	 */
	BOHEMIA("BOH"),

	/*
	 * Old Names of Countries/Independent States
	 */

	/**
	 * Burma
	 */
	BURMA("BUR"),

	/**
	 * Ceylon (Today Sri Lanka)
	 */
	CEYLON("CEY"),

	/**
	 * Congo-Kinshasa (Later Zaire)
	 */
	CONGOKINSHASA("CKN"),

	/**
	 * Congo-Brazzaville (Today Congo)
	 */
	CONGOBRAZZAVILLE("COB"),

	/**
	 * Dahomey (Today Benin)
	 */
	DAHOMEY("DAH"),

	/**
	 * Dutch Indies (Today Indonesia)
	 */
	DUTCH_INDIES("DEI"),

	/**
	 * New Hebrides (Today Vanuatu)
	 */
	NEW_HEBRIDES("HEB"),

	/**
	 * Rhodesia (Today Zimbabwe)
	 */
	RHODESIA("RHO"),

	/**
	 * Tanganyika (Today part of Tanzania)
	 */
	TANGANYIKA("TAA"),

	/**
	 * Taiwan (Today Chinese Taipei)
	 */
	TAIWAN("TAI"),

	/**
	 * United Arab Republic
	 */
	UNITED_ARAB_REPUBLIC("UAR"),

	/**
	 * Upper Volta (Today Burkina Faso)
	 */
	UPPER_VOLTA("UPV"),

	/**
	 * Western Samoa (Today Samoa)
	 */
	WESTERN_SAMOA("WSM"),

	/**
	 * Zaire (Today Congo DR)
	 */
	ZAIRE("ZAI");

	private static final SetMultimap<FifaCountry, FifaCountry> successors;

	static {
		/* Successors of indicidual national teams according to
		 * http://en.wikipedia.org/wiki/List_of_men's_national_association_football_teams#Former_national_football_teams
		 */
		successors = HashMultimap.create();
		successors.put(CZECHOSLOVAKIA, CZECH_REPUBLIC);
		successors.put(CZECHOSLOVAKIA, SLOVAKIA);

		successors.put(SAARLAND, WEST_GERMANY);

		successors.put(WEST_GERMANY, GERMANY);

		successors.put(EAST_GERMANY, GERMANY);

		successors.put(NORTH_VIETNAM, VIETNAM);

		successors.put(SOUTH_VIETNAM, VIETNAM);

		successors.put(NORTH_YEMEN, YEMEN);

		successors.put(SOUTH_YEMEN, YEMEN);

		successors.put(UNITED_ARAB_REPUBLIC, EGYPT);
		successors.put(UNITED_ARAB_REPUBLIC, SYRIA);

		successors.put(SOVIET_UNION, COMMONWEALTH_OF_INDEPENDENT_STATES);
		successors.put(SOVIET_UNION, ESTONIA);
		successors.put(SOVIET_UNION, LATVIA);
		successors.put(SOVIET_UNION, LITHUANIA);

		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, RUSSIA);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, ARMENIA);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, AZERBAIJAN);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, BELARUS);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, GEORGIA);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, KAZAKHSTAN);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, KYRGYZSTAN);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, MOLDOVA);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, TAJIKISTAN);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, TURKMENISTAN);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, UKRAINE);
		successors.put(COMMONWEALTH_OF_INDEPENDENT_STATES, UZBEKISTAN);

		successors.put(YUGOSLAVIA, SERBIA_AND_MONTENEGRO);
		successors.put(YUGOSLAVIA, BOSNIA_AND_HERZEGOVINA);
		successors.put(YUGOSLAVIA, CROATIA);
		successors.put(YUGOSLAVIA, MACEDONIA_FYR);
		successors.put(YUGOSLAVIA, SLOVENIA);

		successors.put(SERBIA_AND_MONTENEGRO, SERBIA);
		successors.put(SERBIA_AND_MONTENEGRO, MONTENEGRO);

		successors.put(TANGANYIKA, TANZANIA);
	}

	public final String code;

	FifaCountry(String code) {
		this.code = code;
	}

	public Set<FifaCountry> getSuccessors() {
		// bfs
		List<FifaCountry> result = new LinkedList<FifaCountry>(successors.get(this));
		Iterator<FifaCountry> iterator = result.listIterator();
		while(iterator.hasNext()) {
			result.addAll(successors.get(iterator.next()));
		}
		return EnumSet.copyOf(result);
	}

}
