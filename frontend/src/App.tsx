import { useEffect, useState } from "react";
import { getTree, createNode, moveNode } from "./services/tree";
import { NodeResponse } from "./api";
import { TreeView, TreeItem } from "@mui/lab";
import {
  Button,
  Stack,
  TextField,
  MenuItem,
  Select,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  Box,
} from "@mui/material";
import {
  ExpandMore,
  ChevronRight,
  Folder,
  InsertDriveFile,
} from "@mui/icons-material";

export default function App() {
  const [tree, setTree] = useState<NodeResponse[]>([]);
  const [open, setOpen] = useState(false);
  const [name, setName] = useState("");
  const [type, setType] = useState<"FOLDER" | "FILE">("FOLDER");
  const [parentId, setParentId] = useState<number | null>(null);

  // ✅ Load from backend
  const load = async () => {
    try {
      const data = await getTree();
      console.log("Loaded tree data:", JSON.stringify(data, null, 2));
      setTree(data || []);
    } catch (error) {
      console.error("Error loading tree:", error);
      setTree([]);
    }
  };

  useEffect(() => {
    load();
  }, []);

  // ✅ Add Node
  const addNode = async () => {
    try {
      await createNode({ name, type, parentId, position: 999 });
      setOpen(false);
      setName("");
      setType("FOLDER");
      setParentId(null);
      await load(); // reload after add
    } catch (error) {
      console.error("Error creating node:", error);
    }
  };

  // ✅ Demo Move
  const demoMove = async () => {
    try {
      const docs = tree.find((t) => t.name === "Documents");
      const pics = tree.find((t) => t.name === "Pictures");
      if (!docs || !pics || !pics.children?.length) return;
      await moveNode(pics.children[0].id, {
        newParentId: docs.id,
        newPosition: 0,
      });
      await load();
    } catch (error) {
      console.error("Error moving node:", error);
    }
  };

  // ✅ Recursive Renderer (safe)
  const renderNode = (n: any) => {
    const children = Array.isArray(n.children) ? n.children : [];
    const icon = n.type === "FOLDER" ? <Folder /> : <InsertDriveFile />;

    return (
      <TreeItem
        key={n.id}
        nodeId={String(n.id)}
        label={
          <Stack direction="row" alignItems="center" gap={1}>
            {icon}
            <Typography>{n.name}</Typography>
          </Stack>
        }
      >
        {children.map((c) => renderNode(c))}
      </TreeItem>
    );
  };

  return (
    <Stack p={2} gap={2}>
      {/* Buttons */}
      <Stack direction="row" gap={1}>
        <Button variant="contained" onClick={() => setOpen(true)}>
          Add Node
        </Button>
        <Button variant="outlined" onClick={demoMove}>
          Demo Move
        </Button>
        <Button onClick={load}>Refresh</Button>
      </Stack>

      {/* ✅ TreeView Display */}
      <Box
        sx={{
          border: "1px solid #ccc",
          borderRadius: "4px",
          padding: 2,
          backgroundColor: "#fafafa",
        }}
      >
        <TreeView
          defaultCollapseIcon={<ExpandMore />}
          defaultExpandIcon={<ChevronRight />}
          defaultExpanded={tree.map((t) => String(t.id))}
        >
          {tree.length > 0 ? (
            tree.map((n) => renderNode(n))
          ) : (
            <Typography color="gray">No nodes found</Typography>
          )}
        </TreeView>
      </Box>

      {/* ✅ Debug Raw Data */}
      <Box
        sx={{
          border: "1px solid lightgray",
          padding: 2,
          fontFamily: "monospace",
          fontSize: 13,
          backgroundColor: "#fff",
          maxHeight: 300,
          overflow: "auto",
        }}
      >
        <Typography fontWeight="bold">Debug Data:</Typography>
        <pre>{JSON.stringify(tree, null, 2)}</pre>
      </Box>

      {/* Dialog for Add Node */}
      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle>Add Node</DialogTitle>
        <DialogContent>
          <Stack gap={2} mt={1}>
            <TextField
              label="Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <Select
              value={type}
              onChange={(e) => setType(e.target.value as any)}
            >
              <MenuItem value="FOLDER">FOLDER</MenuItem>
              <MenuItem value="FILE">FILE</MenuItem>
            </Select>
            <TextField
              label="Parent ID (optional)"
              value={parentId ?? ""}
              onChange={(e) =>
                setParentId(e.target.value ? Number(e.target.value) : null)
              }
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button onClick={addNode} variant="contained">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
}
