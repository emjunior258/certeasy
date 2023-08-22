import axios from "axios";
export const SERVER = "http://localhost:8080/api";
const api = axios.create({
  baseURL: SERVER,
});

export default api;
