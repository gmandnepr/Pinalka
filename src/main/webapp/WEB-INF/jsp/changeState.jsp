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
    <div class="subtitle">${subject.label}: ${item.label}</div>

    <table border="0">
        <tr>
            <td>${schemaSubjectDescription}</td>
            <td>${subject.description}</td>
        </tr>
        <tr>
            <td>${schemaSubjectTeacher}</td>
            <td>${subject.teacher}</td>
        </tr>
        <tr>
            <td>${schemaItemDescription}</td>
            <td>${item.description}</td>
        </tr>
        <tr>
            <td>${schemaItemDeadline}</td>
            <td>${item.deadline}</td>
        </tr>
        <tr>
            <td>${schemaDone}</td>
            <td>${state.doneRatio}</td>
        </tr>
    </table>
    <c:forEach var="state" items="${states}">
        <a href="<c:url value="/schema/${user.name}/action/state/${subject.id}/${item.id}/${state}/"/>">
            <span class="item ${state} <c:if test="${item.state == state}">current</c:if>">${state}</span>
        </a>
    </c:forEach>
    <br/>
    <br/>
</div>
<div class="about">${copyright}</div>
</body>
</html>