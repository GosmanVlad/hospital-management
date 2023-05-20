import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const employeeService = {
    getEmployeeByDepartmentId,
    getDoctors,
    getEmployeesWithPagination
};

function getEmployeeByDepartmentId(departmentId) {
    return axios.get(`${apiUrl}/employees/departments/${departmentId}`, { headers });
}

function getDoctors() {
    return axios.get(`${apiUrl}/employees`, { headers });
}

function getEmployeesWithPagination(filters, pagination) {

    let pagFilters = "";

    if (pagination) {
        pagFilters = `?page=${pagination.page - 1}&size=${pagination.size}&pagination=true`
    }

    let params = {
        role: filters?.role,
        employeeId: filters?.employeeId
    };

    console.log("Filters", filters);
    return axios.get(`${apiUrl}/employees/${pagFilters}`, { params: params }, { headers });
}