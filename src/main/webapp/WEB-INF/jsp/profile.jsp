<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>">
</head>
<body>
<div class="title">${title}</div>
<div class="menu">
    <c:forEach var="link" items="${menuLinks}">
        <a href="<c:url value="${link.href}"/>">${link.label}</a>
    </c:forEach>
</div>
<div class="content">
    <table border="0">
        <tr>
            <td>${profileName}</td>
            <td>${user.name}</td>
        </tr>
        <tr>
            <td>${profileRegistered} </td>
            <td>${user.registrationDate}</td>
        </tr>
    </table>
</div>
<div class="about">${copyright}</div>
</body>
</html>