<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mi App con Docker</title>
</head>
<body>
<h1><%= "GitHub Form con Jakarta y Docker" %>
</h1>
<br/>
<form method="post" action="myServlet" autocomplete="off">
    <label for="name">¿Cuál es tu nombre?</label><br>
    <input type="text" id="name" name="name"><br>
    <label for="gitHub">¿Cuál es tu user de GitHub?</label><br>
    <input type="text" id="gitHub" name="gitHub"><br><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>