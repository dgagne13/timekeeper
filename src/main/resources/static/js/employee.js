var app = angular.module('employees-app', ['ui.bootstrap']);
app.controller('employees-ctrl', function($scope, $http) {
    $scope.baseUrl = ""; //"http://localhost:8080";
    $http.get($scope.baseUrl + "/v1/employees")
    .success(function(response){ $scope.employees = response });
    $scope.showShifts = false;
    $scope.showEmployees = true;
    /* search employees */
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

    /*view shifts */
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

    /* create a shift */
    $scope.shiftTypes = ['Regular', 'Vacation', 'Sick', 'Holiday', 'UnpaidLeave'];
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
        $scope.showCreate = false;
    };
    $scope.clearShift = function() {
        $scope.newShift = {
            employeeId: null,
            date: new Date(),
            shiftType: 'Regular',
            startTime: $scope.dateWithHours(9,0),
            endTime: $scope.dateWithHours(17,0),
            hasLunch: false,
            lunchStart: $scope.dateWithHours(12,0),
            lunchEnd: $scope.dateWithHours(12,30)
        }
        $scope.creationError = false;
        $scope.creationErrorMessage = "";
    };
    $scope.shiftChange = function() {
        var ns = $scope.newShift;

        //set date for time fields
        ns.startTime.setYear(ns.date.getFullYear());
        ns.startTime.setMonth(ns.date.getMonth());
        ns.startTime.setDay(ns.date.getDate());

        //set date for time fields
        ns.endTime.setYear(ns.date.getFullYear());
        ns.endTime.setMonth(ns.date.getMonth());
        ns.endTime.setDay(ns.date.getDate());

        //set date for time fields
        ns.lunchStartTime.setYear(ns.date.getFullYear());
        ns.lunchStartTime.setMonth(ns.date.getMonth());
        ns.lunchStartTime.setDay(ns.date.getDate());

        //set date for time fields
        ns.lunchEndTime.setYear(ns.date.getFullYear());
        ns.lunchEndTime.setMonth(ns.date.getMonth());
        ns.lunchEndTime.setDay(ns.date.getDate());

        if(ns.startTime.getTime() >= ns.endTime.getTime()) {
            $scope.creationError = true;
            $scope.creationErrorMessage = "Shift must end after it begins";
             return;
        }
        //validate lunch only for regular shift
        if(ns.shiftType === 'Regular') {
           if(ns.hasLunch) {
                // make sure times are sequential
               if(ns.startTime.getTime() >= ns.endTime.getTime()) {
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
    $scope.createShift = function(employeeId) {
       $scope.clearShift();
       $scope.newShift.employeeId = employeeId;
    };

    //prepare shift data for posting
    $scope.prepareData = function(newShift) {
        var data = {};
        var ns = $scope.newShift;
        data.shiftType = ns.shiftType;
        data.startTime = ns.startTime;
        data.endTime = ns.endTime;

        if(ns.hasLunch) {
           data.lunchStartTime = ns.startTime;
           data.lunchEndTime = ns.endTime;
        }

        return JSON.stringify(data);
    };

    $scope.postShift = function() {
        var data = $scope.prepareData($scope.newShift);
        var url = $scope.baseUrl + "/v1/shifts";
        var response = $http.post(url,data);
        res.success(function() { $scope.showCreate = false;});
        res.error(function(err) {
            $scope.creationError = true;
            $scope.creationErrorMessage = err.message;
        });
    };
});
