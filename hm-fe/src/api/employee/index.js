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
    getDoctors
};

function getEmployeeByDepartmentId(departmentId) {
    return axios.get(`${apiUrl}/employees/departments/${departmentId}`, { headers });
}

function getDoctors() {
    return axios.get(`${apiUrl}/employees`, { headers });
}