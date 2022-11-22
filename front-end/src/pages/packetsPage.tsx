import { AddCircle } from "@mui/icons-material";
import { Alert, Button, Paper, Snackbar, TextField } from "@mui/material";
import { ChangeEvent, useEffect, useState } from "react";
import { PacketListEntry } from "../components/packets/PacketListEntry";
import { v4 as uuidv4 } from "uuid";
import { getHttpService } from "../data/httpService";
import { Packet } from "../data/Models";

export interface PacketsPageProps {}

export function PacketsPage(props: PacketsPageProps) {
  const styles = {
    page: {
      padding: "32px",
    },
    header: {
      display: "flex",
      flexDirection: "column",
      gap: "16px",
      padding: "32px",
    } as const,
    controls: {
      display: "flex",
      justifyContent: "flex-end",
    },
    packetList: {
      display: "flex",
      flexDirection: "column-reverse",
      gap: "8px",
    } as const,
  };

  const [packets, setPackets] = useState<Packet[]>([]);
  const [canCreatePacket, setCanCreatePacket] = useState(true);
  const [search, setSearch] = useState("");
  const [alertActive, setAlertActive] = useState(false);
  const [alertReason, setAlertReason] = useState("");
  const httpService = getHttpService();

  useEffect(() => {
    const httpService = getHttpService();
    httpService.axios.get<any>("/api/packet/retrieve").then((res) => {
      let np: Packet[] = [];
      Object.entries(res.data.allKeys).forEach((v: any) => {
        np.push({
          name: v[1].name,
          id: v[0],
          new: false,
        });
        setPackets(np);
      });
    });
  }, []);

  const startAlert = (reason: string) => {
    setAlertActive(true);
    setAlertReason(reason);
  };

  const handleAlertClose = (
    event?: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    if (reason === "clickaway") {
      return;
    }
    setAlertActive(false);
  };

  const addPacket = () => {
    setSearch("");
    let np = packets.slice();
    np.push({
      name: "",
      id: uuidv4(),
      new: true,
    });
    setPackets(np);
    setCanCreatePacket(false);
  };

  const handleSearchChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const renamePacket = (index: number, name: string) => {
    let np = packets.slice();
    np[index].name = name;
    np[index].new = false;
    setPackets(np);
    setCanCreatePacket(true);

    httpService.axios
      .post(`/api/packet/${np[index].id}`, {
        name: name,
        invoicePDFPath: "",
        approvalPDFPath: "",
        csvPDFPath: "",
        compiledPDFPath: "",
      })
      .catch(() => {
        let np = packets.slice();
        np.pop();
        setPackets(np);
        startAlert("Failed to create packet. Try again.");
      });
  };

  const listPackets = packets
    .filter((packet) =>
      packet.name.toLowerCase().includes(search.toLowerCase())
    )
    .map((packet, i) => (
      <PacketListEntry
        key={packet.id}
        name={packet.name}
        id={packet.id}
        new={packet.new}
        renameFunc={(name: string) => renamePacket(i, name)}
      />
    ));

  return (
    <div style={styles.page}>
      <header style={styles.header}>
        <h1>Packets</h1>
        <TextField
          variant="filled"
          label="Search packets"
          disabled={!canCreatePacket}
          value={search}
          onChange={handleSearchChange}
        />
        <div style={styles.controls}>
          <Button
            onClick={addPacket}
            variant="contained"
            endIcon={<AddCircle />}
            disabled={!canCreatePacket}
          >
            Create
          </Button>
        </div>
      </header>
      <Paper style={styles.packetList}>{listPackets}</Paper>
      <Snackbar open={alertActive}>
        <Alert
          className="alert"
          onClose={handleAlertClose}
          severity="error"
          sx={{ width: "100%" }}
        >
          {alertReason}
        </Alert>
      </Snackbar>
    </div>
  );
}
