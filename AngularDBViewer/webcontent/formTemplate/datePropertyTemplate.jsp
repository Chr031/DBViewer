

<div ng-controller="dateController" ng-init="initDate(<%= request.getParameter("propertyName") %>)">

 	<p class="input-group">
		<input type="text" class="form-control" datepicker-popup="{{dateFormat}}" ng-model="datePickerValue" is-open="opened" datepicker-options="dateOptions" ng-required="true" close-text="Cancel" />
		<span class="input-group-btn">
			<button type="button" class="btn btn-default" ng-click="open($event)">
				<i class="glyphicon glyphicon-calendar"></i>
			</button>
		</span>
	</p>
	

</div>