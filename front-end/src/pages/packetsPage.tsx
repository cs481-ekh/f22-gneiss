import { AddCircle } from "@mui/icons-material";
import { Alert, Button, Paper, Snackbar, TextField } from "@mui/material";
import { ChangeEvent, useState } from "react";
import { PacketListEntry } from "../components/packets/PacketListEntry";
import { v4 as uuidv4 } from "uuid";
import { getHttpService } from "../data/httpService";

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

  const dummyPackets = [
    {
      name: "Packet 1",
      id: "id1",
      new: false,
    },
    {
      name: "Packet 2",
      id: "id2",
      new: false,
    },
    {
      name: "Packet 3",
      id: "id3",
      new: false,
    },
  ];

  const [packets, setPackets] = useState(dummyPackets);
  const [canCreatePacket, setCanCreatePacket] = useState(true);
  const [search, setSearch] = useState("");
  const [alertActive, setAlertActive] = useState(false);
  const [alertReason, setAlertReason] = useState("");
  const httpService = getHttpService();

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
