<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
${text}
<form action="<c:url value="${actionUrl}"/>" method="post" modelAttribute="user" name="user">
    <table border="0">
        <tr>
            <td>${profileName}</td>
            <td><form:input path="user.name"/></td>
            <td><span class="error"><form:errors path="user.name"/></span></td>
        </tr>
        <tr>
            <td>${profileEmail}</td>
            <td><form:input path="user.email"/></td>
            <td><span class="error"><form:errors path="user.email"/></span></td>
        </tr>
        <tr>
            <td>${profilePassword}</td>
            <td><input type="password" name="password1"/></td>
            <td><span class="error"><form:errors path="user.pass"/></span></td>
        </tr>
        <tr>
            <td>${profilePasswordRepeat}</td>
            <td><input type="password" name="password2"/></td>
            <td></td>
        </tr>
        <tr>
            <td colspan="3"><span class="error"><input type="submit" value="${profileActionRegister}"/></span></td>
        </tr>
    </table>
</form>
</div>
<div class="about">${copyright}</div>
</body>
</html>