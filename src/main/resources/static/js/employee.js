var app = angular.module('employees-app', ['ui.bootstrap']);
app.controller('employees-ctrl', function($scope, $http) {
    $scope.view = "employees";
    $http.get("/v1/employees")
    .success(function(response){ $scope.employees = response });

    /* search employees */
    $scope.empSearchLast = "";
    $scope.empSearchDept = "";
    $scope.searchEmployees = function() {
        var url = "/v1/employees?last=" + $scope.empSearchLast;
        if($scope.empSearchDept){
            url += "&department=" + $scope.empSearchDept;
        }

       $http.get(url)
           .success(function(response){ $scope.employees = response; });


       $scope.view = 'employees';
    };

    /*view shifts */
    $scope.shiftsForEmployee = function(id, name) {
        var url = "/v1/shifts?employee-id=" + id;
         $http.get(url)
                   .success(function(response){
                        $scope.shifts = response;
                        $scope.shiftEmployee = name;
                        $scope.shiftEmployeeId = id;
                    });
        $scope.view = "shifts";
    };
    $scope.closeShifts = function() {
        $scope.searchEmployees();
    };

    /* create a shift */
    $scope.shiftTypes = ['Regular', 'Vacation', 'Sick', 'Holiday', 'UnpaidLeave'];
    $scope.currentDate = new Date();
    $scope.showCreate = false;
    $scope.hasCreationError = false;
    $scope.creationErrorMessage = false;
    $scope.dateWithHours = function(hours, minutes) {
        var dwh = new Date();
        dwh.setHours(hours);
        dwh.setMinutes(minutes);
        dwh.setSeconds(0);
        dwh.setMilliseconds(0);
        return dwh;
    };

    $scope.cancelCreate = function() {
        $scope.clearShift();
        $scope.searchEmployees();
    };
    $scope.clearShift = function() {
        $scope.newShift = {
            employeeId: null,
            date: new Date(),
            shiftType: 'Regular',
            startTime: $scope.dateWithHours(9,0),
            endTime: $scope.dateWithHours(17,0),
            hasLunch: true,
            lunchStartTime: $scope.dateWithHours(12,0),
            lunchEndTime: $scope.dateWithHours(12,30)
        }
        $scope.shiftEmployee = "";
        $scope.creationError = false;
        $scope.creationErrorMessage = "";
    };
    $scope.shiftChange = function() {
        var ns = $scope.newShift;

        //set date for time fields
        ns.startTime.setFullYear(ns.date.getFullYear());
        ns.startTime.setMonth(ns.date.getMonth());
        ns.startTime.setDate(ns.date.getDate());

        //set date for time fields
        ns.endTime.setFullYear(ns.date.getFullYear());
        ns.endTime.setMonth(ns.date.getMonth());
        ns.endTime.setDate(ns.date.getDate());

        //set date for time fields
        ns.lunchStartTime.setFullYear(ns.date.getFullYear());
        ns.lunchStartTime.setMonth(ns.date.getMonth());
        ns.lunchStartTime.setDate(ns.date.getDate());

        //set date for time fields
        ns.lunchEndTime.setFullYear(ns.date.getFullYear());
        ns.lunchEndTime.setMonth(ns.date.getMonth());
        ns.lunchEndTime.setDate(ns.date.getDate());

        if(ns.startTime.getTime() >= ns.endTime.getTime()) {
            $scope.creationError = true;
            $scope.creationErrorMessage = "Shift must end after it begins";
             return;
        }
        //validate lunch only for regular shift
        if(ns.shiftType === 'Regular') {
           if(ns.hasLunch) {
                // make sure times are sequential
               if(ns.lunchStartTime.getTime() >= ns.lunchEndTime.getTime()) {
                   $scope.creationError = true;
                   $scope.creationErrorMessage = "Lunch must end after it begins";
                   return;
               } else if(ns.startTime.getTime() >= ns.lunchStartTime.getTime() || ns.endTime.getTime() <= ns.lunchEndTime.getTime()) {
                   $scope.creationError = true;
                   $scope.creationErrorMessage = "Lunch must be during the shift";
                   return;
               }
           } else {
                var duration = (ns.endTime.getTime() - ns.startTime.getTime()) / 3600000;
                if(duration >= 6) {
                    $scope.creationError = true;
                    $scope.creationErrorMessage = "Lunch must be taken for a 6+ hour shift";
                    return;
                }
           }
        }

        //valid, show submit button
        $scope.creationError = false;
        $scope.creationErrorMessage = "";
    };

    //set up new shift with emp id
    $scope.createShift = function(employeeId, name) {
       $scope.clearShift();
       $scope.shiftEmployee = name;
       $scope.newShift.employeeId = employeeId;
       $scope.view = "create"
    };

    //prepare shift data for posting
    $scope.prepareData = function(newShift) {
        var data = {};
        var ns = $scope.newShift;
        data.employeeId = ns.employeeId;
        data.shiftType = ns.shiftType;
        data.startTime = ns.startTime;
        data.endTime = ns.endTime;

        if(ns.shiftType == 'Regular' && ns.hasLunch) {
           data.lunchStartTime = ns.lunchStartTime;
           data.lunchEndTime = ns.lunchEndTime;
        }

        return JSON.stringify(data);
    };

    $scope.postShift = function() {
        var data = $scope.prepareData($scope.newShift);
        var url = "/v1/shifts";
        var response = $http.post(url,data);
        response.success(function() { $scope.searchEmployees();});
        response.error(function(err) {
            $scope.creationError = true;
            $scope.creationErrorMessage = err.message;
        });
    };

    $scope.deleteShift = function(shiftId) {
        var url = "/v1/shifts/" + shiftId;
        var response = $http.delete(url);
        response.success(function() {
            $scope.shiftsForEmployee($scope.shiftEmployeeId, $scope.shiftEmployee);
        });
        response.error(function(err) {
            alert("Error deleting shift: " + err.message);
        });
    }
});
