<div>
<div class="localActionBar"> 
	<button class="btn btn-default" ng-click="createNewDependantObject(<%= request.getParameter("propertyName") %>, property.accessor.actualTypeArgument)"> Add new</button> 
	<button class="btn btn-default" ng-click="openListForCollection(<%= request.getParameter("propertyName") %>, property.accessor.actualTypeArgument);"> pick </button> 
	
</div>
<table class="table" ng-controller="listFormController" >
	<tr ng-repeat="(index, value) in <%= request.getParameter("propertyName") %>.links">
		<td ng-class="{lineTableOverCstm: hover[index]}" 
			ng-mouseenter="hover[index] = true"
			ng-mouseleave="hover[index] = false"
			ng-click="openDependantObject(<%= request.getParameter("propertyName") %>, property.accessor.actualTypeArgument,value.linkedId, index)"
			>{{value.value}}
		</td>
		<td ng-class="{lineTableOverCstm: hover[index]}" 
			ng-mouseenter="hover[index] = true"
			ng-mouseleave="hover[index] = false"
			ng-click="openDependantObject(<%= request.getParameter("propertyName") %>, property.accessor.actualTypeArgument,value.linkedId, index)"
		>
			<span style=" display: inline-block; width:80px;">
				<button class="btn btn-danger" ng-show="hover[index]" ng-click="removeObjectLink(<%= request.getParameter("propertyName") %>,index);">
				 	<i class="icon-trash icon-white"></i>
				 	Remove
				</button>
			</span>
		</td> 
	</tr>
</table>



</div>