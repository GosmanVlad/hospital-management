import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const employeeService = {
    getEmployeeByDepartmentId
};

function getEmployeeByDepartmentId(departmentId) {
    return axios.get(`${apiUrl}/employees/departments/${departmentId}`, { headers });
}