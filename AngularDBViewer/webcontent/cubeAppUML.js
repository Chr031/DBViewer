
Math.sign = Math.sign || function(x) {
  x = +x; // convert to a number
  if (x === 0 || isNaN(x)) {
    return x;
  }
  return x > 0 ? 1 : -1;
}



app.service('Geom', function(){
	/** returns a double array of size 2,2; */
	this.get2DRotationMatix = function (angle_x) {
		var cos = Math.cos(angle_x);
		var sin = Math.sin(angle_x);
		var rotationMatrix = [[cos,-sin],[sin,cos]]; 
		return rotationMatrix; 
	};

	/** 
		point is of the for {x: ,y: }
	*/
	this.apply2DTransformation = function (matrix, point) {
		var result = this.multiplyMatrix(matrix, [[point.x],[point.y]]);
		return {"x":result[0][0], "y":result[1][0]};
	};

	this.multiplyMatrix = function (m1, m2) {
		var result =[];
		for (var i=0;i<m1.length;i++) {
			result[i] = [];
			for (var j=0;j<m2[0].length;j++) {
				result[i][j] = 0;
				for (var k=0;k<m2.length;k++) {
					result[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return result;
	};

});


app.directive('drawUmlModel',['$document','$http', 'Geom','ClassSelector','$window', function( $document, $http, Geom, ClassSelector, $scope,$window) {
	// Runs during compile
	return {
		restrict: 'A', // E = Element, A = Attribute, C = Class, M = Comment
		scope: {
			drawUmlModel : '=',
		},
		link: function($scope, iElm, iAttrs, controller) {
			var ctx = iElm[0].getContext('2d');			
			var model = $scope.drawUmlModel;
			var classPosition = [];
			var modelPositionList;
			var classRectList = [];
			var classRectIndex = new rbush(9, ['.x1', '.y1', '.x2', '.y2']);

			var phy = (1+ Math.sqrt(5))/2;

			console.log(lookdeepString(iAttrs));
			console.log("Start drawing " + $scope.drawUmlModel.modelName);
			
			ctx.textBaseline = "middle";
    		//ctx.font=""+iAttrs.fontSize +'px '+iAttrs.fontFamilly;
    		ctx.font="15px Tahoma";

    		
			for(i in model.classes) {
				classPosition[model.classes[i].simpleName] = {"x":50,"y":20+30};
				
			}




			// Load the model position
			var urlGetPosition = "getModelPositionBean/"+model.modelName +"/";
			$http.get(urlGetPosition).success(function(response) {
					console.log(lookdeepString(response));
					modelPositionList = response;
					for (var i in modelPositionList.list) {
						var pos = modelPositionList.list[i];
						classPosition[pos.className].x = pos.x;
						classPosition[pos.className].y = pos.y;
					}
					drawModel(true);

				})
				.error(function(response) { 
					// TODO not accessible : fix the bug : introduce a service
					$scope.showError("get Model position bean ", response);
				});


			function saveModelPosition() {
				for (var name in classPosition) {
					var cPos = classPosition[name];
					for (var i in modelPositionList.list ) {
						var pos = modelPositionList.list[i];
						if (pos.className == name) {
							pos.x = cPos.x;
							pos.y = cPos.y;
						}
					}
				}
				var url = "setModelPositionBean"
				$http.post(url, modelPositionList);

			}

		

			
    		function drawModel(reloadIndex) {
    			ctx.clearRect(0,0,iElm[0].width, iElm[0].height);
    			if (reloadIndex) {classRectIndex.clear();}
    			for(i in model.classes) {    				
    				var name = model.classes[i].simpleName;
    				paintTableClass(classPosition[name].x,classPosition[name].y, name, reloadIndex);
    			}
    			for (i in model.links) {
    				drawLink(model.links[i]);
    			}
    			
    		}

			
			function paintTableClass(x, y, name, reloadIndex) {
				//console.log("paint class " +  name + " at " + x + "," + y);
				var txtWidth= ctx.measureText(name).width;
    			var txtHeight = 15;//iAttrs.fontSize ;
    			var w = txtWidth + 10 + txtWidth%2; // offset padding
    			var h = txtHeight + 10 + txtHeight%2; // offset padding

    			// draw the rectangle and the table name
    			
    			ctx.beginPath();
				ctx.fillStyle="#555";
				ctx.fillText(name,x-txtWidth/2, y );
				ctx.strokeStyle="#789";
    			ctx.rect(x-w/2, y-h/2,w, h);
    			ctx.stroke(); 				
    			var indexRect = {"id":name,"x1":x-w/2,"y1":y-h/2,"x2":x+w/2,"y2":y+h/2};
    			classRectList[name] = indexRect;
    			if (reloadIndex) {classRectIndex.insert(indexRect);}
			}

			function drawLink(link) {
				var color;
				if (link.type=="key") color="#9AB";
				else color="orange";
				paintArrowAndCardinality(classRectList[link.startClassName], classRectList[link.endClassName], color, link.startCardinality,link.endCardinality);
			}


			/**
			 * Big function that should be factorized 
			 */
			function paintArrowAndCardinality(startRect, endRect, color, startCardinality, endCardinality) {
				// getTheLine !!!
				var startX = (startRect.x1 + startRect.x2)/2;
				var startY = (startRect.y1 + startRect.y2)/2;
				var startH = startRect.y2 - startRect.y1;
				var startW = startRect.x2 - startRect.x1;

				var endX = (endRect.x1 + endRect.x2)/2;
				var endY = (endRect.y1 + endRect.y2)/2;
				var endW = endRect.x2 - endRect.x1;
				var endH = endRect.y2 - endRect.y1;

				
				

				// get the angle
				// get the base points
				
				var tb=8, th=10;
				var theta = 0;
				var sinL = endX - startX;
				var cosL = endY - startY;
				var startBX=startX, startBY=startY, endBX=endX, endBY=endY;
				var startTanCorner = startW/startH;
				var endTanCorner = endW/endH;
				if (sinL == 0) {
					if (cosL == 0) 
						return ;
					theta = cosL>0? 0: Math.PI;
					startBX+=0; startBY += Math.sign(cosL) * startH/2;
					endBX+=0; endBY += -Math.sign(cosL) * endH/2;
				} else {
					var tanTheta = sinL/cosL;
					theta = Math.atan(tanTheta);
					if (cosL < 0) 
						theta += Math.PI;
					//console.log(Math.sign(cosL) + " "+theta  + " "+ tanTheta);
					
					if (-startTanCorner < tanTheta && tanTheta<startTanCorner) {
						startBX+= Math.sign(cosL) * startH * ( tanTheta / 2);
						startBY+= Math.sign(cosL) * startH /2;						
					} else {
						startBX += Math.sign(sinL) * startW /2;
						startBY += Math.sign(sinL) * startW / (tanTheta *2);
					}
					// TODO could be factorized
					
					if (-endTanCorner < tanTheta && tanTheta < endTanCorner) {
						endBX -= Math.sign(cosL) * endH * ( tanTheta / 2);
						endBY -= Math.sign(cosL) * endH /2;	
					} else {
						endBX -= Math.sign(sinL) * endW /2;
						endBY -= Math.sign(sinL) * endW / (tanTheta *2);
					}

				}
				//console.log(startX + ","+ startY + " "+ startBX +","+ startBY);

				// draw the line
				ctx.beginPath();
				ctx.strokeStyle=color;
				ctx.moveTo(startBX, startBY);
				ctx.lineTo(endBX, endBY);
				ctx.stroke();

				// draw the arrow on endLine 
				var T0 = {"x":0,"y":0};
				var T1 = {"x":tb/2,"y":th};
				var T2 = {"x":-tb/2,"y":th};

				var rm = Geom.get2DRotationMatix(Math.PI-theta);

				T1 = Geom.apply2DTransformation(rm, T1);
				T2 = Geom.apply2DTransformation(rm, T2);

				//console.log(lookdeepString(T1));
				//console.log(lookdeepString(T2));


				ctx.beginPath();
				ctx.fillStyle=color;
				ctx.moveTo(endBX, endBY);
				ctx.lineTo(endBX+T1.x, endBY+T1.y);
				ctx.lineTo(endBX+T2.x, endBY+T2.y);
				ctx.closePath();
				ctx.fill();

				if (startCardinality == undefined ||endCardinality == undefined) return ;

				// draw the cardinality
				var startCardTxtWidth= ctx.measureText(startCardinality).width;
    			var endCardTxtWidth= ctx.measureText(endCardinality).width;
    			
    			var txtHeight = 15;
				startBY += txtHeight/2;
				if (-startTanCorner < tanTheta && tanTheta<startTanCorner) {
					// north or south
					if (cosL <=0) startBY -= txtHeight;
					if (sinL >=0) startBX -= startCardTxtWidth;
				} else {
					// east or west
					if (sinL <=0) startBX -= startCardTxtWidth;
					if (cosL <=0) startBY -= txtHeight;	
				}
				// add some space : don't glue the label !
				var padding = 3;
				var l = Math.sqrt(Math.pow(startBX - startX,2) + Math.pow(startBY -startY,2));
				var k = padding/l +1;
				startBX = startX + (startBX - startX) * k;
				startBY = startY + (startBY - startY) * k;
				ctx.fillStyle="#555";
				ctx.fillText(startCardinality, startBX, startBY);


				// TODO could be factorized
				
				endBY += txtHeight/2;
				if (-endTanCorner < tanTheta && tanTheta<endTanCorner) {
					// north or south
					if (cosL >=0) endBY -= txtHeight;
					if (sinL <=0) endBX -= endCardTxtWidth;
				} else {
					// east or west
					if (sinL >=0) endBX -= endCardTxtWidth;
					if (cosL >=0) endBY -= txtHeight;	
				}
				// add some space : don't glue the label !
				padding = 10;
				l = Math.sqrt(Math.pow(endBX - endX,2) + Math.pow(endBY -endY,2));
				k = padding/l +1;
				endBX = endX + (endBX - endX) * k;
				endBY = endY + (endBY - endY) * k;
				ctx.fillStyle="#555";
				ctx.fillText(endCardinality, endBX, endBY);




			}		


			// dragging
			var startX,startY;
			var cx = iElm[0].getBoundingClientRect().left;
			var cy = iElm[0].getBoundingClientRect().top;
			var draggedClassName="";

			iElm.on("dblclick", function(event) {
				for(i in model.classes) {   
					if (model.classes[i].simpleName == draggedClassName) {
						ClassSelector.chooseClass(model.classes[i]);
					}
				}

			});

			iElm.on('mousedown', function(event) {
		        // Prevent default dragging of selected content
		        event.preventDefault();
		        startX = event.clientX - cx + getPageScroll()[0];
		        startY = event.clientY - cy + getPageScroll()[1];
		        var searchResult = classRectIndex.search([startX,startY,startX,startY]);
		        console.log(startX +"," + startY );
		        
		        if (searchResult.length == 0) 
		        	return ;
		        
		        draggedClassName = searchResult[0].id;
		        console.log(searchResult[0].id);
		        console.log(lookdeepString(searchResult));
		        
		        startX -= classPosition[draggedClassName].x;
		        startY -= classPosition[draggedClassName].y;		        

		        $document.on('mousemove', mousemove);
		        $document.on('mouseup', mouseup);
		    });

		    function mousemove(event) {
		        var x = event.clientX - cx + getPageScroll()[0] - startX;
		        var y = event.clientY - cy + getPageScroll()[1] - startY;
		        classPosition[draggedClassName]={"x":x,"y":y};
		        drawModel(false);
		    }

		    function mouseup() {
		        $document.off('mousemove', mousemove);
		        $document.off('mouseup', mouseup);
		        drawModel(true);
		        saveModelPosition();
		    }
		    
		    resizeCanvas();
			// set the size of this canvas 
			function resizeCanvas() {
		    	
		    	iElm[0].height = window.innerHeight -cy -20;
		    	iElm[0].width = window.innerWidth -cx -20;
		    	ctx.textBaseline = "middle";
    			ctx.font="15px Tahoma";
		    	drawModel(false);
		    }

		    window.onresize = function() {
		    	resizeCanvas();
		    };

		}
	};
}]);
