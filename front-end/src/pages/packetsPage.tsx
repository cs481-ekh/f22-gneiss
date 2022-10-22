import { AddCircle } from "@mui/icons-material";
import {
  Button,
  Paper,
  TextField,
} from "@mui/material";
import { useState } from "react";
import { PacketListEntry } from "../components/packets/PacketListEntry";

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
  const [canCreatePacket, setCanCreatePacket] = useState(true)

  const addPacket = () => {
    let np = packets.slice();
    np.push({
      name: "",
      id: crypto.randomUUID(),
      new: true,
    });
    setPackets(np);
    setCanCreatePacket(false)
  };

  const listPackets = packets.map((packet) => (
    <PacketListEntry name={packet.name} id={packet.id} new={packet.new} setPacketValid={setCanCreatePacket} />
  ));

  return (
    <Paper style={styles.page}>
      <header style={styles.header}>
        <h1>Packets</h1>
        <TextField variant="filled" label="Search Packets" disabled={!canCreatePacket} />
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
    </Paper>
  );
}
