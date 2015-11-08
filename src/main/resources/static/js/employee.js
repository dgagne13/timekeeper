var app = angular.module('employees-app', []);
app.controller('employees-ctrl', function($scope, $http) {
    $scope.baseUrl = "http://localhost:8080";
    $http.get($scope.baseUrl + "/v1/employees")
    .success(function(response){ $scope.employees = response });
    $scope.showShifts = false;
    $scope.showEmployees = true;
    $scope.empSearchLast = "";
    $scope.empSearchDept = "";
    $scope.searchEmployees = function() {
        var url = $scope.baseUrl + "/v1/employees?last=" + $scope.empSearchLast;
        if($scope.empSearchDept){
            url += "&department=" + $scope.empSearchDept;
        }

       $http.get(url)
           .success(function(response){ $scope.employees = response; });

    };
    $scope.shiftsForEmployee = function(id, name) {
        var url = $scope.baseUrl + "/v1/shifts?employee-id=" + id;
         $http.get(url)
                   .success(function(response){
                        $scope.shifts = response;
                        $scope.shiftEmployee = name;
                    });
        $scope.showEmployees = false;
        $scope.showShifts = true;
    };
    $scope.closeShifts = function() {
        $scope.showShifts = false;
        $scope.showEmployees = true;
    };
});