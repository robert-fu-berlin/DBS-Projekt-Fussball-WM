package dbs_fussball.servlets;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Joiner;

public class JspHelper {

	private JspHelper() {}
	
	public static String print(Object o) {
		return o != null ? o.toString() : ""; 
	}
	
	public static String wrap(String tag, Object o) {
		if (o == null)
			return "";
		
		return "<" + tag + ">" + o.toString() + "</" + tag + ">";
	}
	
	public static String wrap(String tag, Map<String, String> params, Object o) {
		if (o == null)
			return "";
		
		return "<" + tag + Joiner.on("\" ").withKeyValueSeparator("=\"").join(params) + ">" + o.toString() + "</" + tag + ">";
	}
	
	public static String createTable(Map<String, String> entries) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<table>");
		
		for (Entry<String, String> entry : entries.entrySet()) {
			if (entry.getValue() == null)
				continue;
			
			builder.append("<tr>");
			builder.append("	<td>" + entry.getKey() + "</td>");
			builder.append("	<td>" + entry.getValue() + "</td>");
			builder.append("</tr>");
		}
		
		builder.append("</table>");
		
		return builder.toString();
	}
	
}
