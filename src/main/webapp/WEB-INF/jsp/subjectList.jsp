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
        <c:forEach var="subject" items="${subjects}">
            <tr>
                <td>
                    <div class="controls">
                        <a href="<c:url value="/schema/${user.name}/action/add/public/${subject.id}"/> ">
                            <img src="<c:url value="/resources/icons/add.png"/>" alt="${schemaSubjectActionAdd}"/></a>
                    </div>
                </td>
                <td>
                    <span class="subject">${subject.label}</span>
                </td>
                <td>
                    <c:forEach var="item" items="${subject.items}">
                        <span class="item not_started">${item.label}</span>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="about">${copyright}</div>
</body>
</html>