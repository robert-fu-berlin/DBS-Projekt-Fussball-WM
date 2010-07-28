<% String black     = "#000"; %>
<% String dark_grey = "#333"; %>
<% String white = "#fff"; %>

<% int width = 800; %>
<% int nodeWidth = (width / 4); %>
* {
	padding: 0;
	margin: 0;
}

body {
	padding: 10px;
	font-family: "Lucida Grande";
	background-color: black;
}

#container {
	margin: 0 auto;
	color: black;
	border: 1px solid #333;
	background-color: <%= white %>;
	padding: 20px;
	width: <%= width + 20 %>px;
	
	-moz-border-radius: 20px 20px 20px 20px;
	-webkit-border-radius: 20px 20px 20px 20px;
}

#container a, #container a:link, #container a:hover, #container  a:visited {
	text-decoration: none;
	color: black;
}

h1.match-score {
	font-size: 250px;
	text-shadow: 5px 5px 20px <%= dark_grey %>;
}

h2.match-teams {
	font-size: 70px;
	text-shadow: 3px 3px 8px <%= dark_grey %>;
	margin-top: -40px;
}

h1, h2 {
	text-align: center;
	text-shadow: 2px 2px 8px <%= dark_grey %>;
}

.tree-node {
	color: white;
	display: inline-block;
}

.tree-node a, .tree-node a:link, .tree-node a:hover, .tree-node a:visited {
	text-decoration: none;
	color: white !important;
}

.tree-node .type-line, .tree-node .goal-line, .tree-node .versus-line {
	display: block;
	text-align: center;
	width: <%= nodeWidth - 6 %>px;
	padding: 3px;
	-webkit-box-shadow: 3px 3px 8px <%= dark_grey %>;
	-moz-box-shadow: 3px 3px 8px <%= dark_grey %>;
}

.tree-node .type-line {
	background-color: red;
	border-radius: 10px 10px 0 0;
	display: block;
}

.tree-node .goal-line, .tree-node .versus-line {
	background-color: black;
}

.tree-node .goal-line {
	font-size: xx-large;
	text-shadow: 0px 0px 8px white;
}

.tree-node .versus-line {
	border-radius: 0 0 10px 10px;
}

.tree-node.final, .tree-node.third-place {
	margin: 10px <%= (width - nodeWidth) / 2 %>px;
}

.tree-node.semi-final {
	margin: 10px <%= (width - 2 * nodeWidth) / 4 %>px;
}

.tree-node.quarter-final {
	margin: 10px <%= (width - 4 * nodeWidth) / 8 %>px;
}

.tree-node.round-of-sixteen {
	margin: 10px <%= (width - 4 * nodeWidth) / 8 %>px;
}

.tree-node.final .type-line {
	background-color: gold;
}

.tree-node.third-place .type-line {
	background-color: silver;
}

.tree-node.semi-final .type-line {
	background-color: brown;
}

.tree-node.quarter-final .type-line {
	background-color: darkblue;
}

.tree-node.round-of-sixteen .type-line {
	background-color: blue;
}

p.error-message {
	font-weight: bold;
	color: red;
}

li {
	margin: 0 0 0 20px;
	color: inherit;
}

input.button {
	display: inline;
	width: auto !important;
}

fieldset {
	display: inline-block;
	border: 1px solid black;
	margin: 0 10px 10px 0;
	width: <%= nodeWidth %>px;
}

fieldset.events {
	display: inline-block;
	border: 1px solid black;
	margin: 0 10px 10px 0;
	width: auto;
}

fieldset label {
	margin: 5px 10px 0 10px;
	font-size: small;
}

fieldset input, fieldset select {
	width: <%= nodeWidth - 20 %>px;
	margin: 0 10px 5px 10px;	
}

fieldset legend {
	padding: 0 10px;
}