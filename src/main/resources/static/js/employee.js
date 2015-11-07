function Employees(baseUrl, employeeTableId) {
    this.baseUrl = baseUrl;
    this.fetchEmployees = function(searchParams) {
        var url = this.baseUrl + "/v1/employees" + searchParams;
        $.get(
            url,
            function (data, status, xhr) {
              return data;
            }
        ).fail(function (xhr, status, error) {
              alert(error.message);
              return null;
        });
    };

    this.allEmployees = function() {
        return this.fetchEmployees();
    };
    this.searchEmployees = function(department, name) {
        var searchParams = "";
        if(department || name) {
            searchParams += "?";
        }
        if(department) {
            searchParams += ("department=" + department);
        }
        if(name) {
            searchParams += ("last=" + name)
        }
        return this.fetchEmployees(searchParams);
    };
}
var EmployeeTable(employeeTableId, baseUrl) {
    this.employees = Employees(baseUrl);
    this.employeeTableId = employeeTableId;
    this.shifts = Shifts(baseUrl);
    this.shiftsTable(baseUrl, "employee-shifts-table")
    this.Columns = {
        trackTime:"Track Time",
        name:"Name",
        position:"Title",
        department:"Department",
        vacationHours:"Vacation",
        sickHours:"Sick",
        viewTime:"View Time"
        hourlyRate:"Hourly Wage",
        employmentStatus:"Status",
        hireDate:"Hired"
    };
    this.tableHeader = function() {
        //add header
        var header = "<tr>";
        $.each(this.Columns, function (key, value) {
          header += "<th>" + value + "</th>";
        });
        header += "</tr>";
        return header;
    };
    this.tableRow = function(employee) {
        id = employee["id"];
        var contents = "";
        if(employee.employmentStatus == "Active") {
            contents += "<tr class='active'>";
        } else {
            contents += "<tr class='inactive'>";
        }
        $.each(this.Columns, function (key, value) {
           if(key == "trackTime") {
              if(employee.employmentStatus == "Active") {
                contents += "<td><span class='add-shift-button' id='add-shift-" + id + "/></td>";
              } else {
                contents += "<td></td>";
              }
            } else if(key == "viewShifts") {
                contents += "<td><span class='view-shifts-button' id='view-shifts-" + id + "/></td>";
            } else {
                var val = employee[key]
                contents += "<td>" + (val != null ? val : "")   + "</td>";
            }

        });
        contents += "</tr>"
        $('#add-shift-' + id).click(function() {
            this.shifts.displayCreateDialog(id);
        });
        $('#view-shifts-' + id).click(function() {
            this.shiftsTable.displayShifts(id);
        });
        return contents;
    };
    this.renderTable = function(employees) {
        if(this.employeeTableId){
          var contents ="No employees found.";
          if(employees.length > 0) {
            contents = "<table>";
            contents += this.tableHeader();
            employees.forEach(function(item) {
                contents += this.tableRow(item)
            });
            contents += "</table>";
          }
          console.info(contents)
          $("#" + this.employeeTableId).text("");
          $("#" + this.employeeTableId).append(contents);
        }
    };
    this.refresh = function(name, department) {
        this.employees.fetchEmployees()
    };
}

function workShift(shiftType, startTime, endTime, lunchStart, lunchEnd) {
    this.shiftType = shiftType;
    this.startTime = startTime;
    this.endTime = endTime;
    this.lunchStart = lunchStart;
    this.lunchEnd = lunchEnd;
}

function Shifts(baseUrl) {
    this.baseUrl = baseUrl;
    this.shiftTypes = ["Regular", "Vacation", "Sick","Holiday", "UnpaidLeave"];
    this.fetchShiftsForEmployee = function(employeeId) {
        if(employeeId) {
            var fetchUrl = this.baseUrl + "/v1/shifts/" + employeeId
            $.get(
                url,
                function (data, status, xhr) {
                  return data;
                }
            ).fail(function (xhr, status, error) {
                alert(error.message);
                return null;
            });
        }

    };
}

function ShiftsTable(baseUrl, shiftsTableId) {
    this.shifts = new Shifts(baseUrl);
    this.Columns = {
        shiftType:"Type",
        startTime:"Start",
        endTime:"End",
        duration:"Duration"
        lunchStart:"Lunch Start",
        lunchEnd:"Lunch End",
        id:""
    };
    this.tableHeader = function() {
        //add header
        var header = "<tr>";
        $.each(this.Columns, function (key, value) {
          header += "<th>" + value + "</th>";
        });
        header += "</tr>";
        return header;
    };
    this.tableRow = function(shift) {
        var contents = "<tr>";
        $.each(this.Columns, function (key, value) {
            var val = item[key]
            if(key == "id") {
                contents += '<td><span class="delete-shift-button" id="delete-shift-' + val +'">X</span></td>';
            } else {
                contents += "<td>" + (val != null ? val : "")   + "</td>";
            }
        });
        contents += "</tr>"
        return contents;
    };
    this.renderTable = function(shifts) {
        if(this.shiftsTableId){
          var contents ="No shifts for employee.";
          if(shifts.length > 0) {
            contents = "<table>";
            contents += this.tableHeader();
            shifts.forEach(function(item) {
                contents += this.tableRow(item)
            });
            contents += "</table>";
          }
          console.info(contents)
          $("#" + this.shiftsTableId).text("");
          $("#" + this.shiftsTableId).append(contents);
          $("#" + this.shiftsTableId).toggle(true);
        }
    };
    this.displayShifts = function(employeeId) {
        var employeeShifts = this.shifts.fetchShiftsForEmployee(employeeId);
        this.renderTable(employeeShifts);

    };
    this.close = function() {
       $("#" + this.shiftsTableId).toggle(false);
    }
}


function ShiftCreationDialog(baseUrl) {
        this.createShift = function(shift) {
            var errors = this.validateShift(shift);
            var url = this.baseUrl + "/v1/shifts"
            if(errors) {
                return errors;
            }
            $.post(
                 url,
                 shift,
                 function(response) {
                    return;
                 },
                 "application/json"
            ).fail(function (xhr, status, error) {
                  alert(error.message);
                  return null;
            });
        };

        this.validateShift = function(shift) {
            var error = {};
            if(!shift) {
                error.message = "No shift was entered."
            }
            if(!shift.shiftType || this.shitfTypes.indexOf(shift.shiftType) == -1) {
                error.message = "Shift type missing or unrecognized";
                error.shiftType = "true";
            }
            if(!shift.startTime) {
            }
        };
        this.displayCreateDialog = function(employee) {
          var contents ='<dialog id="new-shift-dialog">'
          + '<h3>Record a shift for ' + firstName + ' ' + lastName +'</h3>'
          + '<form>'
          + '<input class="hidden-input" type="text" id=""/>';
          contents += '<label for="new-shift-type"><span>Type</span>'
          + '<select name="new-shift-type" id="new-shift-type">';
          this.shiftTypes.forEach(function(item) {
            contents += '<option value="' + item +'">'+ item +'</option>';
          });
          contents += '</select>'
          + '</label>'
          + '<label for="new-shift-start"><span>Start</span><input type="dtext" name="new-shift-start-date" id="new-shift-start-date"/>'
          + this.timeSelect()
          + '</label>'
          + '<label for="new-shift-lunch-start"><span>Lunch start</span><input type="datetime" name="new-shift-lunch-start" id="new-shift-lunch-start"/></label>'
          + '<label for="new-shift-lunch-end"><span>Lunch end</span><input type="datetime" name="new-shift-lunch-end" id="new-shift-lunch-end"/></label>'
          + '<label for="new-shift-end"><span>End</span><input type="datetime" name="new-shift-end" id="new-shift-end"/></label>'
          + '<input name="new-shift-submit" type="submit" />'
          + '</form>
          + '</dialog>';

        };

        this.timeSelect = function() {
            var dscontent = "<select>";
        };
}