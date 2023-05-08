import axios from "axios";
const headers = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
};

const apiUrl = process.env.VUE_APP_API_URL;

export const appointmentService = {
    getAppointments,
    saveAppointment,
    changeAppointmentStatus
};

function getAppointments(filters, pagination) {
    let pagFilters = "";

    if (pagination) {
        pagFilters = `?page=${pagination.page - 1}&size=${pagination.size}`
    }

    let params = {
        departmentId: filters?.departmentId || null,
        employeeId: filters?.employeeId || null,
        userId: filters?.userId,
        status: filters?.status || "",
    };

    return axios.get(`${apiUrl}/appointments/${pagFilters}`, { params: params }, { headers });
}


function saveAppointment(body) {

    return axios.post(`${apiUrl}/appointments`, body, { headers });
}

function changeAppointmentStatus(appointmentId, status) {

    return axios.put(`${apiUrl}/appointments/${appointmentId}/${status}`, { headers });
}