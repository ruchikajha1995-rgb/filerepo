import axios from "axios";
import { NodeResponse } from "../api";

// ✅ backend base URL
const API_BASE = "http://localhost:8080/api";

export const getTree = async (): Promise<NodeResponse[]> => {
  // Fetch all nodes (tree view)
  const res = await axios.get(`${API_BASE}/nodes`);
  return res.data;
};

export const createNode = async (payload: any): Promise<NodeResponse> => {
  const res = await axios.post(`${API_BASE}/nodes`, payload);
  return res.data;
};

export const updateNode = async (id: number, payload: any): Promise<NodeResponse> => {
  const res = await axios.put(`${API_BASE}/nodes/${id}`, payload);
  return res.data;
};

export const deleteNode = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE}/nodes/${id}`);
};

export const moveNode = async (
  id: number,
  payload: { newParentId: number | null; newPosition: number }
): Promise<NodeResponse> => {
  const res = await axios.post(`${API_BASE}/nodes/${id}/move`, payload);
  return res.data;
};
