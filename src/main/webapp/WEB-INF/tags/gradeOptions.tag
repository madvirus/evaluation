<%@ tag body-content="empty" pageEncoding="euc-kr" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="grade" required="false" type="java.lang.String" %>
<option value="S" <c:if test="${grade == 'S'}">selected</c:if>>S</option>
<option value="A" <c:if test="${grade == 'A' || empty grade}">selected</c:if>>A</option>
<option value="B" <c:if test="${grade == 'B'}">selected</c:if>>B</option>
<option value="C" <c:if test="${grade == 'C'}">selected</c:if>>C</option>
<option value="D" <c:if test="${grade == 'D'}">selected</c:if>>D</option>
