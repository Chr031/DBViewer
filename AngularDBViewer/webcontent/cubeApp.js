//Global helper functions

function rnd() {
	return Math.random();
}


function lookdeep(object){
    var collection= [], index= 0, next, item;
    for(item in object){
        if(object.hasOwnProperty(item)){
            next= object[item];
            if(typeof next== 'object' && next!= null){
                collection[index++]= item +
                ':{ '+ lookdeep(next).join(', ')+'}';
            }
            else collection[index++]= [item+':'+String(next)];
        }
    }
    return collection;
}

function lookdeepString(object){
    var collection= lookdeep(object);
    return "{" + collection.join(",\n") +"}";
}

function getPageScroll() {
    var xScroll, yScroll;
    if (self.pageYOffset) {
      yScroll = self.pageYOffset;
      xScroll = self.pageXOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {
      yScroll = document.documentElement.scrollTop;
      xScroll = document.documentElement.scrollLeft;
    } else if (document.body) {// all other Explorers
      yScroll = document.body.scrollTop;
      xScroll = document.body.scrollLeft;
    }
    return new Array(xScroll,yScroll)
}

function css(selector, property, value) {
    for (var i=0; i<document.styleSheets.length;i++) {//Loop through all styles
        //Try add rule
        try { document.styleSheets[i].insertRule(selector+ ' {'+property+':'+value+'}', document.styleSheets[i].cssRules.length);
        } catch(err) {try { document.styleSheets[i].addRule(selector, property+':'+value);} catch(err) {}}//IE
    }
}

function scrollHeader() {
	var base = 115;
	var scrollHeight = getPageScroll()[1];
	css(".header-wrap", "top", Math.max(-scrollHeight+0,-base+0));
	css(".menu-wrap", "top", Math.max(-scrollHeight+130,-base+130));
	css(".menu-wrap", "height", "calc(100% - " + Math.max(-scrollHeight+130,-base+130) + "px)");
	css(".historyLine", "top", Math.max(-scrollHeight+135,-base+135));
	css(".actionBar", "top", Math.max(-scrollHeight+130,-base+130));
	css(".header-background", "top", Math.max(-scrollHeight+0,-base+0));

}





/**
*  Module
*
* Description
*/
var app = angular.module('cubeApp', ['ngRoute','ui.bootstrap']);


// draw a triangle 
// need color font-size font-familly font-color text
app.directive("drawing", function(){
  	return {
    	restrict: "A",
    	link: function(scope, element, attr) {
    		//console.log(attr.drawing + " " + attr.color);
    		var ctx = element[0].getContext('2d');
    		ctx.textBaseline = "middle";
    		ctx.font=""+attr.fontSize +'px '+attr.fontFamilly;
    		var txtWidth= ctx.measureText(attr.text).width;
    		var txtHeight = attr.fontSize ;
    		//console.log(attr.fontSize +'px '+attr.fontFamilly);
    		
    		
    		var height = Number(txtHeight)+10;
    		var offset = height * (1+Math.sqrt(5))/2;
    		var width = txtWidth+offset;
    		element[0].height = height;
    		element[0].width = width;
						
			ctx.font=""+attr.fontSize +'px '+attr.fontFamilly;
			//console.log(ctx.font + " " + height + " "+ width);
    		
    		paint(attr.drawing);

    		function paint(end) {
    			
    			ctx.fillStyle=attr.color;
				ctx.moveTo(0,0);
				ctx.lineTo(txtWidth+offset/4,0);
				ctx.lineTo(txtWidth+offset/2,height/2);
				ctx.lineTo(txtWidth+offset/4, height);
				ctx.lineTo(0, height);
				ctx.closePath();
				ctx.fill();

				if (!end) {
					ctx.fillStyle=attr.color;
					ctx.moveTo(txtWidth+offset/2,0);
					ctx.lineTo(txtWidth+3*offset/4,height/2);
					ctx.lineTo(txtWidth+offset/2,height);
					ctx.lineTo(width,height);
					ctx.lineTo(width,0);
					ctx.closePath();
					ctx.fill();
				}


				ctx.fillStyle=attr.fontColor;
				ctx.textBaseline = "middle";
				ctx.fillText(attr.text,offset/8,height/2 );
    		};
			
			scope.$watch(attr.drawing, paint);

			

    	}
    }
});




app.directive('watchSize',  function(){
	// Runs during compile
	return {
		restrict: 'A', // E = Element, A = Attribute, C = Class, M = Comment
		link: function($scope, iElm, iAttrs, controller) {
			$scope.$watch(
			  function () {
			    return [iElm[0].clientWidth, iElm[0].clientHeight].join('x');
			  },
			  function (value) {
			    var size = value.split('x');
			    console.log('watch directive got resized:', size);
			    var cssList = iAttrs.watchSize.split(',');
			    console.log('bind css:', cssList);
			    for (var i in cssList) {
			    	var cssOffset = cssList[i].split(':');

			    	css(cssOffset[0], cssOffset[1], Number(size[0])+Number(cssOffset[2]));
			    }
			  }
			);	
		}


	}; 
});

// unused
app.directive('draggable', ['$document', function($document) {
  return {
    restrict: 'A',
    link: function(scope, element, attr) {
      var startX = 0, startY = 0, x = 0, y = 0;

      element.css({
       	position: 'relative',
       	cursor: 'pointer'
      });

      element.on('mousedown', function(event) {
        // Prevent default dragging of selected content
        event.preventDefault();
        startX = event.pageX - x;
        startY = event.pageY - y;
        $document.on('mousemove', mousemove);
        $document.on('mouseup', mouseup);
      });

      function mousemove(event) {
        y = event.pageY - startY;
        x = event.pageX - startX;
        element.css({
          top: y + 'px',
          left:  x + 'px'
        });
      }

      function mouseup() {
        $document.off('mousemove', mousemove);
        $document.off('mouseup', mouseup);
      }
    }
  };
}]);


/**
* Services 
*/
app.service('ModelSelector', function() {
	var activeModel={};
	this.chooseModel=function(model) {
		
		activeModel=model;
		
	};

	this.getActiveModel = function () {
		return activeModel; 
	};

});

app.service('ClassSelector', function() {
	var activeClass={};
	this.chooseClass = function (selectedClass) {

		activeClass = selectedClass;
	};

	this.getActiveClass = function () {
		return activeClass;
	};

});


/**
* Controller of the data
*
*/
app.controller('modelListController', function(ModelSelector,$scope,$http){
	$http.get('getModels').success(function (response) {
		$scope.models=response;
	});
	$scope.chooseModel = ModelSelector.chooseModel;  

	$scope.addModel = {};
	$scope.addModel.showAddModelWindow = false;

	$scope.openNewModelWindow = function () {
		$scope.addModel.showAddModelWindow = !$scope.addModel.showAddModelWindow ;
	};

	
	 
});



app.controller('historyController',  function($scope){
	$scope.historyView = [];
	$scope.activeHistoryViewIndex=0;
	
	$scope.activeHistoryData = function() {
		console.log(lookdeepString($scope.historyView[$scope.activeHistoryViewIndex]));

		return $scope.historyView[$scope.activeHistoryViewIndex];
	};

	$scope.addHistory = function(historyData) {
		$scope.historyView.length= ++ $scope.activeHistoryViewIndex;
		$scope.historyView.push(historyData);
		cleanHistory();
	};

	$scope.setFirstHistory = function(historyData) {
		$scope.historyView.length=0;
		$scope.historyView.push(historyData);
		$scope.activeHistoryViewIndex=0;
	};

	$scope.previousHistory = function() {
		$scope.historyView.length= $scope.activeHistoryViewIndex--;
		cleanHistory();
		if ($scope.historyView[$scope.activeHistoryViewIndex].refresh != undefined) {
			$scope.historyView[$scope.activeHistoryViewIndex].refresh();
		}
	};

	$scope.gotoHistory = function(historyIndex) {
		$scope.historyView.length=historyIndex+1;
		$scope.activeHistoryViewIndex = historyIndex;
		cleanHistory();

	};

	 var cleanHistory = function () {
		for (var hd in $scope.historyView) {
			delete $scope.historyView[hd].$$hashKey;			
		}
	}

});

app.controller('tableListController', function(ModelSelector,ClassSelector,$scope) {
	$scope.activeModel = ModelSelector.getActiveModel;
	$scope.chooseClass = ClassSelector.chooseClass; 

	$scope.openUMLMap = function(modelToDisplay) {
		console.log ("Display UML Model of " + lookdeepString(modelToDisplay));
		var historyData = {};
		historyData.title=modelToDisplay.modelName + " Map ";
		historyData.viewTemplate = "umlViewTemplate.html?rnd="+rnd();
		historyData.model=modelToDisplay;
		$scope.setFirstHistory(historyData);

	};

});

app.controller('UMLMapController', function($scope){
	$scope.drawLine = function (canvasId) {
		var canvas  = document.getElementById(canvasId);
		var context = canvas.getContext("2d");
			
		context.beginPath();
		context.lineWidth = 3;
		context.moveTo(10,10);
		context.lineTo(50,37);
		context.stroke();
		context.closePath();
		console.log("drawn on " + canvasId);
		
	};

	$scope.mapModel = {}

	$scope.setMapModel = function(model) {
		$scope.mapModel = model;
	}
});





/** tHE big controller that should be split into multiple ones...
*/

app.controller('displayController', function(ModelSelector,ClassSelector,$scope, $http) {
	$scope.activeClass = ClassSelector.getActiveClass;
	
	// watch the active class change and tigger the table view editor
	// Very newbee code ....
	$scope.$watch(
		function () {return $scope.activeClass();} ,
		function (newValue, oldValue) {
			if (ModelSelector.getActiveModel().modelName==undefined) return;

			var url="getObjectBeanList/"+ModelSelector.getActiveModel().modelName+"/"+newValue.name+"/";					
			console.log(url);
			$http.get(url)
				.success(function(response) {
					
					console.log(lookdeepString(response));
					var historyData = {};
					historyData.title=  newValue.simpleName+ "'s list";
					historyData.viewTemplate="tableviewtemplate.html?rnd="+rnd();
					historyData.tableData=response;
					historyData.refresh= function () {
						$http.get(url).success(function(response) {
							historyData.tableData=response;
						})
						.error(function(response) { 
							$scope.showError("refresh ", response);
						});

					};
					$scope.setFirstHistory(historyData);
					
					$scope.hover = [];
					$scope.selected = [];


				})
				.error(function(response) { 
					$scope.showError("active class change ", response);
				
				});	

		});

	$scope.openTableView = function(className, filter) {
		var url = "getObjectBeanFilteredList/"+ModelSelector.getActiveModel().modelName+"/"+className+"/";
		console.log(url);
		$http.post(url, filter) 
			.success(function(response) {
				console.log("open table view response : " + lookdeepString(response));
				var historyData = {};
				historyData.title= response.title
				historyData.viewTemplate="tableviewtemplate.html?rnd="+rnd();
				historyData.tableData=response;

				historyData.refresh= function () {
					$http.post(url, filter).success(function(response) {
						historyData.tableData=response;
					})
					.error(function(response) { 
						$scope.showError("table view refresh ", response);
					});
				};

				$scope.addHistory(historyData);

				$scope.hover = [];
				$scope.selected = [];


			})
			.error(function(response) { 
				$scope.showError("open Table View ", response);
				
			});	
	};

	$scope.buildFilter = function(objectBean, propertyName) {
		var filter = {};
		filter.baseClass = objectBean.objectClass;
		filter.id = objectBean.originalHashCode;
		filter.propertyName = propertyName;
		return filter;
	}



	
	// click event to edit a line according to a table 

	$scope.propertyTemplateMapping = [];
	$scope.propertyTemplateMapping["TEXTAREA"] = "formTemplate/textAreaPropertyTemplate.jsp?";
	$scope.propertyTemplateMapping["TEXTFIELD"] = "formTemplate/textFieldPropertyTemplate.jsp?";
	$scope.propertyTemplateMapping["DATE"] = "formTemplate/datePropertyTemplate.jsp?";
	$scope.propertyTemplateMapping["BUTTON"] = "formTemplate/buttonPropertyTemplate.jsp?";
	$scope.propertyTemplateMapping["COLLECTION"] = "formTemplate/collectionPropertyTemplate.jsp?";
	$scope.propertyTemplateMapping["ENUM"] = "formTemplate/enumPropertyTemplate.jsp?";
	$scope.propertyTemplateMapping["BOOLEAN"] = "formTemplate/booleanPropertyTemplate.jsp?";
	
	$scope.processTemplateOptionArguments = function (options) {
		var optionArgs='';
		for (option in options) {
			optionArgs+='&option='+options[option];
		}
		return optionArgs;
	};
	
	

	$scope.openFormView = function(data, objectClass, saveCallback) {
		
		var url = "getObjectBean/" + ModelSelector.getActiveModel().modelName +"/"+ objectClass + "/" + data.originalHashCode+"/";
		console.log(lookdeepString(data));
		console.log(url);
		$http.get(url)
			.success(function (response) {
				//$scope.viewTemplate="formviewtemplate.html";
				//$scope.formData = response;

				var historyData = {};
				historyData.title = response.objectBean.propertyBeans.toString.value.value;
				historyData.viewTemplate="formviewtemplate.html?rnd="+rnd();
				historyData.formData=response;
				historyData.saveCallback = saveCallback;
				$scope.addHistory(historyData);

				console.log(lookdeepString($scope.formData));
				console.log("formData : " + lookdeepString(historyData.formData));
			})
			.error(function(response) { 
				$scope.showError("openFormView", response);
				
			});	
		
	};

	$scope.editObject = function(data, classDescriptor) {
		$scope.openFormView(data, classDescriptor.objectClass, $scope.saveObject)
	}

	// click event according to a table to add a new row
	$scope.createNewObject = function(classDescriptor) {
		// a bit hacky ... may trigger bugs .
		$scope.openFormView({originalHashCode:"0"},classDescriptor.objectClass, $scope.saveObject);
	};


	$scope.createNewDependantObject= function ( propertyBean, objectClass) {
		var saveCallback = function (formData) {
			
			$scope.saveObjectWithCallBack(formData, function(response) {
				console.log("new ObjectId is : "+ response);
				//Retrieve the real bean
				var url = "getObjectBean/" + ModelSelector.getActiveModel().modelName +"/"+ formData.objectBean.objectClass + "/" + response+"/";
				$http.get(url)
					.success(function (response2) {
						var link = {};
						
						link.value=response2.objectBean.propertyBeans.toString.value.value;
						link.linkedId=response2.objectBean.originalHashCode;
						link.linkedClass=response2.objectBean.objectClass;
						propertyBean.links.push(link);
						$scope.previousHistory();
					})
					.error(function(response) { 
						$scope.showError("createNewDependantObject", response);
				
					});

				

			});

		};		

		$scope.openFormView({originalHashCode:"0"}, objectClass, saveCallback);

	};


	$scope.openDependantObject = function ( propertyBean, objectClass, objectId, linkIndex) {
		console.log(objectClass +" "+  objectId +" " +linkIndex);
		var saveCallback = function(formData) {
			$scope.saveObjectWithCallBack(formData, function(response) {
				console.log("saved objectId is : "+ response);
				//Retrieve the real bean
				var url = "getObjectBean/" + ModelSelector.getActiveModel().modelName +"/"+ formData.objectBean.objectClass + "/" + response+"/";
				$http.get(url)
					.success(function (response2) {
						var link = {};
						
						link.value=response2.objectBean.propertyBeans.toString.value.value;
						link.linkedId=response2.objectBean.originalHashCode;
						link.linkedClass=response2.objectBean.objectClass;
						
						propertyBean.links[linkIndex]=link;
						$scope.previousHistory();
					})
					.error(function(response) { 
						$scope.showError("openDependantObject", response);
				
					});			

			});
		};

		$scope.openFormView({originalHashCode:objectId}, objectClass, saveCallback);

	};


	$scope.deleteSelectedRow = function(dataList) {
		if ($scope.selected == undefined) return ;
		// there's a bug there : loop must be done from the top to the bottom ....
		for (var selectedIndex in $scope.selected) {
			console.log(selectedIndex + ' ' + $scope.selected[selectedIndex]);
			if ($scope.selected[selectedIndex]) {
				var url = "deleteObject/"+ModelSelector.getActiveModel().modelName+"/"+dataList[selectedIndex].objectClass+"/"+dataList[selectedIndex].originalHashCode+"/"
				console.log(url);
				$http.get(url)
					.success(function(response){
						dataList.splice(selectedIndex, 1);						
					})
					.error(function(response) { 
						$scope.showError("deleteObject", response);
					
					});
			}
		}

	};


	// save form object 
	$scope.saveObject = function(formData) {
		$scope.saveObjectWithCallBack(formData, function() {
			$scope.previousHistory();
		});
	};





	$scope.saveObjectWithCallBack = function (formData, callback) {
		var url = "setObjectBean/" + ModelSelector.getActiveModel().modelName +"/"+ formData.classDescriptor.objectClass 
		console.log(lookdeepString(formData));
		console.log(url);

		$http.post(url, formData.objectBean)
			.success(function (response) {
				console.log("Object saved");
				callback(response);
			})
			.error(function(response) { 
				$scope.showError("saveObjectWithCallBack", response);
				
			});
		

	};




	$scope.cancelObject = function() {
		$scope.previousHistory();
		console.log("Cancel button clicked");
	};



	// Open new pick list 
	$scope.openList=function (propertyBean) {
		console.log(lookdeepString(propertyBean));
		var url="getObjectBeanList/"+ModelSelector.getActiveModel().modelName+"/"+propertyBean.links[0].linkedClass+"/";					
		console.log(url);
		$http.get(url)
			.success(function(response){
				var historyData = {};
				historyData.title = "pick " +response.classDescriptor.objectName;
				historyData.viewTemplate="pickviewtemplate.html?&rnd="+rnd();
				historyData.tableData=response;
				historyData.callbackData = function(objectToString, objectId) {
						propertyBean.value=	objectToString;
						propertyBean.links[0].value=objectToString
						propertyBean.links[0].linkedId=objectId;
					};
				
				$scope.addHistory(historyData);

				$scope.hover = [];
				$scope.selected = [];

			})
			.error(function(response) { 
				$scope.showError("openList", response);
				
			});
	};


	$scope.pickObject = function (historyData,data) {
		historyData.callbackData(data.propertyBeans.toString.value.value, data.originalHashCode);
		$scope.previousHistory();
	};

	/**
 	 * Open a tableview for the selection of an object 
 	 */ 
	$scope.openListForCollection=function (propertyBean, linkedClassName) {
		/*if (propertyBean == undefined) {
			propertyBean = {};
			propertyBean.value="";
			propertyBean.links=[];
		}*/
		console.log(lookdeepString(propertyBean));
		console.log(linkedClassName);
		var url="getObjectBeanList/"+ModelSelector.getActiveModel().modelName+"/"+linkedClassName+"/";					
		console.log(url);
		$http.get(url)
			.success(function(response){
				var historyData = {};
				historyData.title = "pick " +response.classDescriptor.objectName;
				historyData.viewTemplate="pickviewtemplate.html";
				historyData.tableData=response;
				historyData.callbackData = function(objectToString, objectId) {
						//propertyBean.value=	objectToString;
						var link = {};
						link.value=objectToString;
						link.linkedId=objectId;
						link.linkedClass=linkedClassName;
						propertyBean.links.push(link);						
					};
				
				$scope.addHistory(historyData);

				$scope.hover = [];
				$scope.selected = [];

			})
			.error(function(response) { 
				$scope.showError("openListForCollection", response);
				
			});
	};

	//$scope.openList




	$scope.removeObjectLink=function(propertyBean, linkIndex) {

		propertyBean.links.splice(linkIndex, 1);
	};



});





app.controller('dateController', function($scope) {
	
	$scope.dateOptions = {}

	$scope.datePickerValue =  {};

	//  fix the initial date of the picker ! and instanciate a watcher ... 
	// this depends of the date format onthe server side !!!
	$scope.initDate = function(dateAsLong) {
		//$scope.dateOptions.initDate = dateAsLong;
		console.log("received date :" + dateAsLong.value);
		$scope.datePickerValue = dateAsLong.value;
		$scope.$watch(function($scope) { return $scope.datePickerValue; },
        	function(newDate, oldDate) {
        		dateAsLong.value = newDate.getDate()+"/" +(newDate.getMonth() + 1) + "/" + newDate.getFullYear(); 
            }
       	);
	}

	$scope.dateFormat='dd/MM/yyyy';
	$scope.open = function($event) {
		console.log($scope.dateOptions.initDate); 
		$event.preventDefault();
		$event.stopPropagation();

		$scope.opened = true;
		 
		
  	};
	
});



app.controller('listFormController', function($scope){
	
	$scope.hover= new Array();


});

// a service could be better !!!
app.controller('baseController', function($scope){
	$scope.error={};
	$scope.error.show = false;
	$scope.showError = function (title, content) {
		console.log("Error during " + title + " : " + lookdeepString(content));

		$scope.error.show=true;
		$scope.error.title=title;
		$scope.error.content=lookdeepString(content);
	};

	$scope.menuwrapsize={};
	$scope.menuwrapsize.value="abc";


	$scope.getSimpleClassName=function(className) {
		
		if (className == null) return null;
		var simpleClassName = className.substring(className.lastIndexOf(".")+1);
		return simpleClassName;
	};

});