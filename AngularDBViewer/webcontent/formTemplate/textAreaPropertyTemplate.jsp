<div>
	<textarea 
		class="form-control" 
		rows="5"
		ng-readonly="property.accessor.readOnly" 
		ng-model="<%= request.getParameter("propertyName") %>.value"></textarea>
</div>