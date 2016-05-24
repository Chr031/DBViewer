<div>
	<select class="form-control" 
	ng-model="<%= request.getParameter("propertyName") %>.value">
		<% 
			String options[] = request.getParameterValues("option");
			for (String option:options) {
				%><option value="<%= option %>"><%= option %></option><%
			}
		%>
			
	</select>
</div>