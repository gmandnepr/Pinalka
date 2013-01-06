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
    <form name="f" action="<c:url value="/static/j_spring_security_check"/>" method="POST">
        <table border="0">
            <c:if test="${isFail}">
                <tr>
                    <td colspan="2"><span class="error">${profileErrorLogin}</span></td>
                </tr>
            </c:if>
            <tr>
                <td>${profileName}</td>
                <td><input type="text" name="j_username" value=''></td>
            </tr>
            <tr>
                <td>${profilePassword}</td>
                <td><input type="password" name="j_password"/></td>
            </tr>
            <tr>
                <td colspan="2">${profileActionRemember} <input type="checkbox" name="_spring_security_remember_me"/></td>
            </tr>
            <tr>
                <td colspan='2'><input name="submit" type="submit" value="${profileActionLogin}"/></td>
            </tr>
        </table>
    </form>
</div>
<div class="about">${copyright}</div>
</body>
</html>