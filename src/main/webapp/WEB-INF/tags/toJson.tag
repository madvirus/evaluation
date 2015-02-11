<%@ tag body-content="empty" pageEncoding="euc-kr" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ attribute name="value" required="true" rtexprvalue="true" type="java.lang.Object" %>
<%
    ObjectMapper objectMapper = new ObjectMapper();
%>
<%= objectMapper.writeValueAsString(value) %>