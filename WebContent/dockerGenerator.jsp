<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1 style="color:green;">L'image génerer avec succès</h1>
	<h4>Le fichier DockerFile:</h4>
	
	<p>
		${dockerFile}
	</p>
	
	<h4>Le résultat de docker </h4>
	
	<p>
		${dockerOutput}
	</p>
	
	
	<h4>Le résultat de docker après le démarrage </h4>
	
	<p>
		${dockerOutputRun}
	</p>
	
	
	
	
	
	
	
	
</body>
</html>