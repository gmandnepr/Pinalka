<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mfn" uri="http://109.206.40.61/taglibs/" %>
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
    ${schemaOwner} <br/>
    <table border="0">
        <c:forEach var="subject" items="${subjects}">
            <tr>
                <td width="125">
                    <c:choose>
                        <c:when test="${ownSchema}">
                            <div class="controls">
                                <a href="<c:url value="/schema/${user.name}/action/priority/inc/${subject.id}"/>">
                                    <img src="<c:url value="/resources/icons/up.png"/>" alt="${schemaSubjectActionUp}"/></a>
                                <a href="<c:url value="/schema/${user.name}/action/priority/dec/${subject.id}"/>">
                                    <img src="<c:url value="/resources/icons/down.png"/>" alt="${schemaSubjectActionDown}"/></a>
                                <a href="<c:url value="/schema/${user.name}/edit/${subject.id}"/>">
                                    <img src="<c:url value="/resources/icons/edit.png"/>" alt="${schemaSubjectActionEdit}"/></a>
                                <a href="<c:url value="/schema/${user.name}/action/remove/${subject.id}"/>">
                                    <img src="<c:url value="/resources/icons/del.png"/>" alt="${schemaSubjectActionDelete}"/></a>
                            </div>
                        </c:when>
                    </c:choose>
                </td>
                <td>
                    <span class="subject">${subject.label}:</span>
                </td>
                <td>
                    <c:forEach var="item" items="${subject.items}">
                        <c:if test="${ownSchema}">
                            <a href="<c:url value="/schema/${user.name}/state/${subject.id}/${item.id}/"/>">
                        </c:if>
                        <span class="item ${item.state} <c:if test="${mfn:isLate(item)}">late</c:if>">${item.label}</span>
                        <c:if test="${ownSchema}">
                            </a>
                        </c:if>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>
    <c:if test="${ownSchema}">
        <div class="controls">
            <input type="button" onclick="document.location='<c:url value="/schema/${user.name}/add/public"/>';"
                   value="${schemaSubjectActionAddPublic}">
            <%--<input type="button" onclick="document.location='<c:url value="/schema/${user.name}/add/recommended"/>';"--%>
                   <%--value="${schemaSubjectActionAddRecommended}">--%>
            <input type="button" onclick="document.location='<c:url value="/schema/${user.name}/add/own"/>';"
                   value="${schemaSubjectActionAddOwn}">
        </div>
    </c:if>
</div>
<div class="about">${copyright}</div>
</body>
</html>