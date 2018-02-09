

angular.module('app.controllers', [])
  
.controller('registerACarCtrl', ['$scope', '$stateParams', '$http',// The following is the constructor function for this page's controller. See https://docs.angularjs.org/guide/controller
// You can include any angular dependencies as parameters for this function
// TIP: Access Route Parameters for your page via $stateParams.parameterName
function ($scope, $stateParams, $PouchDB) {
    
var dbLocal = new PouchDB("car_sales_db");
var dbRemote = new PouchDB("http://127.0.0.1:5984/car_sales_db");
var dataDBLocal = [];
var dataDBRemote = [];
var detailsToUpdate = [];
    var carsSelected = [];


    
   
    
     $scope.addCar = function(car_to_save){
        var id = String(new Date().getTime());
        var doc = {
            "_id":id,
            "make": car_to_save.make,
            "model": car_to_save.model,
            "year": car_to_save.year,
            "price": car_to_save.price,
            "description": car_to_save.description
        };
        
        dbLocal.put(doc, function(err, result){
            if (!err){
                alert('the car has been added');
                console.log("the car has been added to the local database PouchDB");
                console.log(result);
            }
        })
    
  dbLocal.sync("http://127.0.0.1:5984/car_sales_db");


    }
     
     //----------update

  $scope.updateCar = function(id){

   var loc_id = String(id.idnn);
 
   dbLocal.get(loc_id).then(function (doc) {
  
  doc.make = id.make;

  return dbLocal.put(doc);
}).then(function () {
 
  return dbLocal.get(loc_id);
}).then(function (doc) {
  console.log(doc);
});
   dbLocal.sync("http://127.0.0.1:5984/car_sales_db");
  }
     
     //-----------Delete
  
$scope.deleteCar = function(id){
    var loc_id = String(id.idnn);

    dbLocal.get(loc_id).then(function(doc) {
  return dbLocal.remove(doc);
}).then(function (result) {
        alert('the car has been deleted');
  console.log('deleted');
}).catch(function (err) {
  console.log(err);
});
dbLocal.sync("http://127.0.0.1:5984/car_sales_db");
}

    //---------------creating a view

$scope.getByModel = function () {
    var ddoc = {
        _id: '_design/my_index4_model',
        views: {
            by_model: {
                map: function (doc) {
                    emit(doc.model);
                }.toString()
            }
        }
    };
    // save if doesnt exist already
    if (typeof ddoc._id === 'undefined') {

        dbLocal.put(ddoc).then(function () {
            console.log("view created");
        }).catch(function (err) {
            console.log(err);
        });

    } else {

        dbLocal.query('my_index4_model/by_model').then(function (res) {
            console.log(JSON.stringify(res));
            
            
            
        }).catch(function (err) {
            console.log("no cars");
        });
    }

    dbLocal.sync("http://127.0.0.1:5984/car_sales_db");
}

    //---------------count cars

$scope.countCar = function (car_make) {
    var MapReduceModel = {
  map: function (doc) {
    emit(doc.make);
  },
  reduce: '_count'
};
    
dbLocal.query(MapReduceModel, {
  key: car_make, reduce: true, group: true
}).then(function (result) {
  console.log(JSON.stringify(result));

        $scope.listOfCarsByMake = result;  
         
     
     
}).catch(function (err) {
  console.log("no results");
});

}

    //-----------load from Pouch
    
    $scope.showCarsLocal = function(){
        dbLocal.allDocs({include_docs:true, descending:true},function callback(err, response){
           console.log(response);
            for (var i = 0; i < response.rows.length; i++){
                console.log(response.rows[i].doc);
                dataDBLocal[i] = response.rows[i].doc;
                
                
            }
            
         $scope.listOfCars = dataDBLocal;  
        })
    }

      //-----------load from Couch
      
      $scope.showCarsRemote = function(){
        dbRemote.allDocs({include_docs:true, descending:true},function callback(err, response){
            for (var i = 0; i < response.rows.length; i++){
                dataDBRemote[i] = response.rows[i].doc;
             
            }
        /*the name listOfCarsRemote was renamed to listOfCars so that it can be reused for showing local data too*/
        $scope.listOfCars = dataDBRemote;  
        })
    }

}])


   
.controller('carsCtrl', ['$scope', '$stateParams', // The following is the constructor function for this page's controller. See https://docs.angularjs.org/guide/controller
// You can include any angular dependencies as parameters for this function
// TIP: Access Route Parameters for your page via $stateParams.parameterName
function ($scope, $stateParams) {


}])

/*
code refereces:
https://www.youtube.com/watch?v=XcFI-rR_T4E
https://pouchdb.com/api.html#query_database
https://pouchdb.com/2014/06/17/12-pro-tips-for-better-code-with-pouchdb.html
https://codepen.io/ionic/pen/isfAJ*/
 