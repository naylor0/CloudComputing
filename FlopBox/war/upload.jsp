<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %> <%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
<html>
<head>
	<title>Upload Test</title>
</head>
<body>
	<form action="<%= blobstoreService.createUploadUrl("/upload") %>"
		method="post" enctype="multipart/form-data"> <input type="text" name="foo">
		<input type="file" name="myFile"><br>
		<input type="radio" name="isPublic" value="public"> Public<br>
		<input type="radio" name="isPublic" value="private" checked="checked"> Private<br>
		<input type="submit" value="Submit">
	</form>
</body>
</html>