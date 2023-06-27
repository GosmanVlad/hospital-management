import axios from "axios";
import moment from "moment";

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
    changeAppointmentStatus,
    generatePdf,
    exportExcel
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

function generatePdf(appointmentId) {
    return axios.get(`${apiUrl}/appointments/generate-appointment/${appointmentId}`, { headers })
}

function exportExcel(filters) {
    let params = {
        departmentId: filters?.departmentId || null,
        employeeId: filters?.employeeId || null,
        userId: filters?.userId,
        status: filters?.status || "",
    };

    return axios.get(`${apiUrl}/appointments/export-excel`, { params: params, responseType: 'arraybuffer', headers: headers }).then((res) => {
        const fileURL = window.URL.createObjectURL(new Blob([res.data]));
        const fileLink = document.createElement("a");
        const contentDisposition = res.headers["content-disposition"];
        let fileName = "Appointment" + "_" + moment(filters?.startDate).format("YYYY-MM-DD") + "_" + moment(filters?.endDate).format("YYYY-MM-DD");

        if (contentDisposition) {
            const fileNameMatch = contentDisposition.match(/filename="(.+)"/);
            if (fileNameMatch?.length === 2) fileName = fileNameMatch[1];
        }

        fileLink.href = fileURL;

        fileLink.setAttribute("download", fileName + ".xlsx");
        document.body.appendChild(fileLink);

        fileLink.click();
    })
}