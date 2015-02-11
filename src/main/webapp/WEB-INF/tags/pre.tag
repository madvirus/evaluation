<%@ tag body-content="empty" pageEncoding="euc-kr" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" type="java.lang.String" %>
<%
    String outValue = value.replace("&", "&amp;");
    outValue = outValue.replace("<", "&lt;");
    outValue = outValue.replace(">", "&gt;");
    outValue = outValue.trim().replace("\n", "\n<br/>");
%>
<%= outValue %>