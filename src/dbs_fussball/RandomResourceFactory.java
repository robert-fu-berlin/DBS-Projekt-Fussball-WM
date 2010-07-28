package dbs_fussball;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dbs_fussball.model.FifaCountry;
import dbs_fussball.model.Person;
import dbs_fussball.model.Team;

public class RandomResourceFactory {

	private static String[]	_firstNames	= { "JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD",
			"CHARLES", "JOSEPH", "THOMAS", "CHRISTOPHER", "DANIEL", "PAUL", "MARK", "DONALD", "GEORGE", "KENNETH",
			"STEVEN",
			"EDWARD", "BRIAN", "RONALD", "ANTHONY", "KEVIN", "JASON", "MATTHEW", "GARY", "TIMOTHY", "JOSE", "LARRY",
			"JEFFREY", "FRANK", "SCOTT", "ERIC", "STEPHEN", "ANDREW", "RAYMOND", "GREGORY", "JOSHUA", "JERRY",
			"DENNIS", "WALTER", "PATRICK", "PETER", "HAROLD", "DOUGLAS", "HENRY", "CARL", "ARTHUR", "RYAN", "ROGER",
			"JOE", "JUAN", "JACK", "ALBERT", "JONATHAN", "JUSTIN", "TERRY", "GERALD", "KEITH", "SAMUEL", "WILLIE",
			"RALPH", "LAWRENCE", "NICHOLAS", "ROY", "BENJAMIN", "BRUCE", "BRANDON", "ADAM", "HARRY", "FRED", "WAYNE",
			"BILLY", "STEVE", "LOUIS", "JEREMY", "AARON", "RANDY", "HOWARD", "EUGENE", "CARLOS", "RUSSELL", "BOBBY",
			"VICTOR", "MARTIN", "ERNEST", "PHILLIP", "TODD", "JESSE", "CRAIG", "ALAN", "SHAWN", "CLARENCE", "SEAN",
			"PHILIP", "CHRIS", "JOHNNY", "EARL", "JIMMY", "ANTONIO", "DANNY", "BRYAN", "TONY", "LUIS", "MIKE",
			"STANLEY", "LEONARD", "NATHAN", "DALE", "MANUEL", "RODNEY", "CURTIS", "NORMAN", "ALLEN", "MARVIN",
			"VINCENT", "GLENN", "JEFFERY", "TRAVIS", "JEFF", "CHAD", "JACOB", "LEE", "MELVIN", "ALFRED", "KYLE",
			"FRANCIS", "BRADLEY", "JESUS", "HERBERT", "FREDERICK", "RAY", "JOEL", "EDWIN", "DON", "EDDIE", "RICKY",
			"TROY", "RANDALL", "BARRY", "ALEXANDER", "BERNARD", "MARIO", "LEROY", "FRANCISCO", "MARCUS", "MICHEAL",
			"THEODORE", "CLIFFORD", "MIGUEL", "OSCAR", "JAY", "JIM", "TOM", "CALVIN", "ALEX", "JON", "RONNIE", "BILL",
			"LLOYD", "TOMMY", "LEON", "DEREK", "WARREN", "DARRELL", "JEROME", "FLOYD", "LEO", "ALVIN", "TIM", "WESLEY",
			"GORDON", "DEAN", "GREG", "JORGE", "DUSTIN", "PEDRO", "DERRICK", "DAN", "LEWIS", "ZACHARY", "COREY",
			"HERMAN", "MAURICE", "VERNON", "ROBERTO", "CLYDE", "GLEN", "HECTOR", "SHANE", "RICARDO", "SAM", "RICK",
			"LESTER", "BRENT", "RAMON", "CHARLIE", "TYLER", "GILBERT", "GENE", "MARC", "REGINALD", "RUBEN", "BRETT",
			"ANGEL", "NATHANIEL", "RAFAEL", "LESLIE", "EDGAR", "MILTON", "RAUL", "BEN", "CHESTER", "CECIL", "DUANE",
			"FRANKLIN", "ANDRE", "ELMER", "BRAD", "GABRIEL", "RON", "MITCHELL", "ROLAND", "ARNOLD", "HARVEY", "JARED",
			"ADRIAN", "KARL", "CORY", "CLAUDE", "ERIK", "DARRYL", "JAMIE", "NEIL", "JESSIE" };

	private static String[]	_stageNames	= { "MARY", "PATRICIA", "LINDA", "BARBARA", "ELIZABETH", "JENNIFER", "MARIA",
			"SUSAN", "MARGARET", "DOROTHY", "LISA", "NANCY", "KAREN", "BETTY", "HELEN", "SANDRA", "DONNA", "CAROL",
			"RUTH", "SHARON", "MICHELLE", "LAURA", "SARAH", "KIMBERLY", "DEBORAH", "JESSICA", "SHIRLEY", "CYNTHIA",
			"ANGELA", "MELISSA", "BRENDA", "AMY", "ANNA", "REBECCA", "VIRGINIA", "KATHLEEN", "PAMELA", "MARTHA",
			"DEBRA", "AMANDA", "STEPHANIE", "CAROLYN", "CHRISTINE", "MARIE", "JANET", "CATHERINE", "FRANCES", "ANN",
			"JOYCE", "DIANE", "ALICE", "JULIE", "HEATHER", "TERESA", "DORIS", "GLORIA", "EVELYN", "JEAN", "CHERYL",
			"MILDRED", "KATHERINE", "JOAN", "ASHLEY", "JUDITH", "ROSE", "JANICE", "KELLY", "NICOLE", "JUDY",
			"CHRISTINA", "KATHY", "THERESA", "BEVERLY", "DENISE", "TAMMY", "IRENE", "JANE", "LORI", "RACHEL",
			"MARILYN", "ANDREA", "KATHRYN", "LOUISE", "SARA", "ANNE", "JACQUELINE", "WANDA", "BONNIE", "JULIA", "RUBY",
			"LOIS", "TINA", "PHYLLIS", "NORMA", "PAULA", "DIANA", "ANNIE", "LILLIAN", "EMILY", "ROBIN", "PEGGY",
			"CRYSTAL", "GLADYS", "RITA", "DAWN", "CONNIE", "FLORENCE", "TRACY", "EDNA", "TIFFANY", "CARMEN", "ROSA",
			"CINDY", "GRACE", "WENDY", "VICTORIA", "EDITH", "KIM", "SHERRY", "SYLVIA", "JOSEPHINE", "THELMA",
			"SHANNON", "SHEILA", "ETHEL", "ELLEN", "ELAINE", "MARJORIE", "CARRIE", "CHARLOTTE", "MONICA", "ESTHER",
			"PAULINE", "EMMA", "JUANITA", "ANITA", "RHONDA", "HAZEL", "AMBER", "EVA", "DEBBIE", "APRIL", "LESLIE",
			"CLARA", "LUCILLE", "JAMIE", "JOANNE", "ELEANOR", "VALERIE", "DANIELLE", "MEGAN", "ALICIA", "SUZANNE",
			"MICHELE", "GAIL", "BERTHA", "DARLENE", "VERONICA", "JILL", "ERIN", "GERALDINE", "LAUREN", "CATHY",
			"JOANN", "LORRAINE", "LYNN", "SALLY", "REGINA", "ERICA", "BEATRICE", "DOLORES", "BERNICE", "AUDREY",
			"YVONNE", "ANNETTE", "JUNE", "SAMANTHA", "MARION", "DANA", "STACY", "ANA", "RENEE", "IDA", "VIVIAN",
			"ROBERTA", "HOLLY", "BRITTANY", "MELANIE", "LORETTA", "YOLANDA", "JEANETTE", "LAURIE", "KATIE", "KRISTEN",
			"VANESSA", "ALMA", "SUE", "ELSIE", "BETH", "JEANNE", "VICKI", "CARLA", "TARA", "ROSEMARY", "EILEEN",
			"TERRI", "GERTRUDE", "LUCY", "TONYA", "ELLA", "STACEY", "WILMA", "GINA", "KRISTIN", "JESSIE", "NATALIE",
			"AGNES", "VERA", "WILLIE", "CHARLENE", "BESSIE", "DELORES", "MELINDA", "PEARL", "ARLENE", "MAUREEN",
			"COLLEEN", "ALLISON", "TAMARA", "JOY", "GEORGIA", "CONSTANCE", "LILLIE", "CLAUDIA", "JACKIE", "MARCIA",
			"TANYA", "NELLIE", "MINNIE", "MARLENE", "HEIDI", "GLENDA", "LYDIA", "VIOLA", "COURTNEY", "MARIAN",
			"STELLA", "CAROLINE", "DORA", "JO", "VICKIE", "MATTIE", "TERRY", "MAXINE", "IRMA", "MABEL", "MARSHA",
			"MYRTLE", "LENA", "CHRISTY", "DEANNA", "PATSY", "HILDA", "GWENDOLYN", "JENNIE", "NORA", "MARGIE", "NINA",
			"CASSANDRA", "LEAH", "PENNY", "KAY", "PRISCILLA", "NAOMI", "CAROLE", "BRANDY", "OLGA", "BILLIE", "DIANNE",
			"TRACEY", "LEONA", "JENNY", "FELICIA", "SONIA", "MIRIAM", "VELMA", "BECKY", "BOBBIE" };

	private static String[]	_lastNames	= { "SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER",
			"WILSON", "MOORE", "TAYLOR", "ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN", "THOMPSON",
			"GARCIA", "MARTINEZ", "ROBINSON", "CLARK", "RODRIGUEZ", "LEWIS", "LEE", "WALKER", "HALL", "ALLEN", "YOUNG",
			"HERNANDEZ", "KING", "WRIGHT", "LOPEZ", "HILL", "SCOTT", "GREEN", "ADAMS", "BAKER", "GONZALEZ", "NELSON",
			"CARTER", "MITCHELL", "PEREZ", "ROBERTS", "TURNER", "PHILLIPS", "CAMPBELL", "PARKER", "EVANS", "EDWARDS",
			"COLLINS", "STEWART", "SANCHEZ", "MORRIS", "ROGERS", "REED", "COOK", "MORGAN", "BELL", "MURPHY", "BAILEY",
			"RIVERA", "COOPER", "RICHARDSON", "COX", "HOWARD", "WARD", "TORRES", "PETERSON", "GRAY", "RAMIREZ",
			"JAMES", "WATSON", "BROOKS", "KELLY", "SANDERS", "PRICE", "BENNETT", "WOOD", "BARNES", "ROSS", "HENDERSON",
			"COLEMAN", "JENKINS", "PERRY", "POWELL", "LONG", "PATTERSON", "HUGHES", "FLORES", "WASHINGTON", "BUTLER",
			"SIMMONS", "FOSTER", "GONZALES", "BRYANT", "ALEXANDER", "RUSSELL", "GRIFFIN", "DIAZ", "HAYES", "MYERS",
			"FORD", "HAMILTON", "GRAHAM", "SULLIVAN", "WALLACE", "WOODS", "COLE", "WEST", "JORDAN", "OWENS",
			"REYNOLDS", "FISHER", "ELLIS", "HARRISON", "GIBSON", "MCDONALD", "CRUZ", "MARSHALL", "ORTIZ", "GOMEZ",
			"MURRAY", "FREEMAN", "WELLS", "WEBB", "SIMPSON", "STEVENS", "TUCKER", "PORTER", "HUNTER", "HICKS",
			"CRAWFORD", "HENRY", "BOYD", "MASON", "MORALES", "KENNEDY", "WARREN", "DIXON", "RAMOS", "REYES", "BURNS",
			"GORDON", "SHAW", "HOLMES", "RICE", "ROBERTSON", "HUNT", "BLACK", "DANIELS", "PALMER", "MILLS", "NICHOLS",
			"GRANT", "KNIGHT", "FERGUSON", "ROSE", "STONE", "HAWKINS", "DUNN", "PERKINS", "HUDSON", "SPENCER",
			"GARDNER", "STEPHENS", "PAYNE" };

	static Person randomPerson() {
		Random random = new Random();
		String firstName = toTitleCase(_firstNames[random.nextInt(_firstNames.length)]);
		String lastName = toTitleCase(_lastNames[random.nextInt(_lastNames.length)]);

		Person person = new Person(firstName, lastName);
		if (random.nextBoolean()) {
			String stageName = toTitleCase(_stageNames[random.nextInt(_stageNames.length)]);
			person.setStageName(stageName);
		}

		return person;
	}

	public static Team randomTeam(FifaCountry country) {
		Team team = new Team(country);

		for (int i = 0; i < 23; i++)
			team.addPlayer(randomPerson());

		team.addAssociate(randomPerson());

		team.setTrainer(randomPerson());
		team.setAssitantTrainer(randomPerson());
		team.setDoctor(randomPerson());

		return team;
	}

	private static String toTitleCase(String string) {
		Pattern p = Pattern.compile("(^|\\W)([a-z])");
		Matcher m = p.matcher(string);
		StringBuffer sb = new StringBuffer(string.length());
		while (m.find())
			m.appendReplacement(sb, m.group(1) + m.group(2).toUpperCase());
		m.appendTail(sb);
		return sb.toString();
	}

}
