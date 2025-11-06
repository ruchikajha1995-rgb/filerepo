
import axios from "axios";
export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || "http://localhost:8080",
});
export type NodeType = "FOLDER" | "FILE";
export interface NodeResponse {
  id: number; name: string; type: NodeType; position: number; parentId: number|null;
  tags: Record<string,string>;
  children: NodeResponse[];
}
