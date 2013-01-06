<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>">
    <script type="text/javascript">

        var newId = ${itemsSize};

        function addItem() {
            try{
                var table = document.getElementById("items");
                var rowCount = table.rows.length;

                var row = table.insertRow(rowCount);
                row.id = newId;


                var cell0 = row.insertCell(0);
                var cell1 = row.insertCell(1);
                var cell2 = row.insertCell(2);
                var cell3 = row.insertCell(3);
                var cell4 = row.insertCell(4);

                cell0.innerHTML = "<input type=\"button\" value=\"${schemaItemActionRemove}\" onclick=\"removeItem("+newId+")\"/>";
                cell1.innerHTML = "<input id=\"items"+newId+".label\" name=\"items["+newId+"].label\" type=\"text\"/>";
                cell2.innerHTML = "<input id=\"items"+newId+".description\" name=\"items["+newId+"].description\" type=\"text\"/>";
                cell3.innerHTML = "<input id=\"items"+newId+".deadline\" name=\"items["+newId+"].deadline\" type=\"text\"/>";
                cell4.innerHTML = "<select id=\"items"+newId+".state\" name=\"items["+newId+"].state\"><option value=\"NOT_STARTED\" selected=\"selected\">NOT_STARTED</option><option value=\"IN_PROGRESS\">IN_PROGRESS</option><option value=\"DONE\">DONE</option><option value=\"REOPEN\">REOPEN</option></select>";

                newId++;
            }catch(e){alert(e);}
        }
        function removeItem(idToRemove) {
            var table = document.getElementById("items");
            var rowCount = table.rows.length;

            for(var i=0; i<rowCount; i++){
                var row = table.rows[i];
                if(row.id == idToRemove){
                    table.deleteRow(i);
                    break;
                }
            }
        }

    </script>
</head>
<body>
<div class="title">${title}</div>
<div class="menu">
    <c:forEach var="link" items="${menuLinks}">
        <a href="<c:url value="${link.href}"/>">${link.label}</a>
    </c:forEach>
</div>
<div class="content">
<form action="<c:url value="${actionUrl}"/>" method="post" modelAttribute="subject" name="subject">
    <form:hidden path="subject.id"/>
    <table border="0">
        <tr>
            <td>${schemaSubjectLabel}</td>
            <td><form:input path="subject.label"/></td>
            <td></td>
        </tr>
        <tr>
            <td>${schemaSubjectDescription}</td>
            <td><form:input path="subject.description"/></td>
            <td></td>
        </tr>
        <tr>
            <td>${schemaSubjectTeacher}</td>
            <td><form:input path="subject.teacher"/></td>
            <td></td>
        </tr>
        <tr>
            <td>${schemaSubjectPublic}</td>
            <td><form:checkbox path="subject.publicSubject"/></td>
            <td></td>
        </tr>
        <tr>
            <td>${schemaSubjectPriority}</td>
            <td><form:input path="subject.priority"/></td>
            <td></td>
        </tr>
    </table>

    <table id="items" border="0">
        <tr id="title">
            <td>*</td>
            <td>${schemaItemLabel}</td>
            <td>${schemaItemDescription}</td>
            <td>${schemaItemDeadline}</td>
            <td>${schemaItemState}</td>
        </tr>
        <c:forEach var="item" items="${subject.items}" varStatus="i">
            <tr id="${i.index}">
                <td><input type="button" value="${schemaItemActionRemove}" onclick="removeItem(${i.index})"/><form:hidden path="subject.items[${i.index}].id"/></td>
                <td><form:input path="subject.items[${i.index}].label"/></td>
                <td><form:input path="subject.items[${i.index}].description"/></td>
                <td><form:input path="subject.items[${i.index}].deadline"/></td>
                <td><form:select path="subject.items[${i.index}].state" items="${statesValues}"/></td>
            </tr>
        </c:forEach>
    </table>
    <input type="button" value="${schemaItemActionAdd}" onclick="addItem()"/>
    <input type="submit" value="${profileActionSave}"/>
</form>
</div>
<div class="about">${copyright}</div>
</body>
</html>