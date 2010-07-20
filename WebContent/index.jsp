<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DBS Projekt</title>

<script type="text/javascript">
function getIndex(index, opt) {
	var ret = "person/" + index + "/" + opt;
	return ret;
}
</script>

</head>
<body>
<div>
<h1>DBS Fussball-WM-Projekt</h1>
<form name="input" action="">
	<table>
		<tr>
			<td><input type="button" value="Spieler erstellen" onclick="location.href='person/new'" /></td>
		</tr>
		<tr>
			<td>
				<input name="index" size="3" type="text" value="Index" onclick="document.input.index.value = ''"/><br />
				<input name="edit" type="button" value="Spieler verändern" onclick="location.href=getIndex(document.input.index.value, 'edit');"/><br />
				<input name="show" type="button" value="Spieler anzeigen" onclick="location.href=getIndex(document.input.index.value, '');"/>
			</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>