const appointmentStatus = [
    { key: '', value: "Toate" },
    { key: 'pending', value: "In asteptare" },
    { key: 'accepted', value: "Acceptat" },
    { key: 'declined', value: "Respins" },
    { key: 'canceled', value: "Anulat" },
    { key: 'solved', value: "Rezolvat" },];

const invoiceStatus = [
    { key: '', value: "Toate" },
    { key: 'unpaid', value: "Neplatita" },
    { key: 'paid', value: "Platita" },
    { key: 'canceled', value: "Anulata" },
    { key: 'pending_user', value: "In asteptarea confirmarii" }];


export { appointmentStatus, invoiceStatus }