import axios from "axios";

const apiUrl = process.env.VUE_APP_API_URL;

export const fileService = {
    download
};

function download(file) {
    return axios.get(`${apiUrl}/files?file=${file}`, { responseType: 'blob' }).then((res) => {
        let fileURL = window.URL.createObjectURL(new Blob([res.data]));
        let fileLink = document.createElement('a');
        const splitFileName = res.config.url.split("/");
        let fileName = splitFileName[splitFileName.length - 1];
        fileLink.href = fileURL;
        fileLink.setAttribute('download', fileName);
        document.body.appendChild(fileLink);

        fileLink.click();
    });
}