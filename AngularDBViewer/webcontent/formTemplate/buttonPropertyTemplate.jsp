<div>
	<button class="btn btn-default" 
		
		ng-click="openList(<%= request.getParameter("propertyName") %>)">
		{{<%= request.getParameter("propertyName") %>.value}}  
	</button>
</div>