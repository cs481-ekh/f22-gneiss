import { Divider, Link, Paper, TextField } from "@mui/material";
import { ChangeEvent, Dispatch, useState } from "react";

export interface PacketListEntryProps {
  name: string;
  id: string;
  new: boolean;
  setPacketValid: Dispatch<React.SetStateAction<boolean>>;
  renameFunc: (name: string) => void;
}

export function PacketListEntry(props: PacketListEntryProps) {
  const styles = {
    packet: {
      padding: "16px",
    },
  };

  const [name, setName] = useState(props.name);
  const [nameInputActive, setNameInputActive] = useState(props.new);

  const handleNameInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
    validateName(e.target.value);
  };

  const handleNameInputBlur = () => {
    if (validateName(name) === "") {
      setNameInputActive(false);
      props.renameFunc(name)
    }
  };

  const validateName = (name: string) => {
    props.setPacketValid(false);
    if (name.length < 3) {
      return "Must be at least 3 characters";
    }
    if (name.length > 20) {
      return "Must not exceed 20 characters";
    }
    props.setPacketValid(true);
    return "";
  };

  return (
    <Paper elevation={3} key={props.id} style={styles.packet}>
      {nameInputActive && (
        <TextField
          autoFocus
          size="small"
          label="New packet"
          error={validateName(name) !== ""}
          helperText={validateName(name)}
          onChange={handleNameInputChange}
          onBlur={handleNameInputBlur}
        />
      )}
      {!nameInputActive && <Link href={`/packet/${props.id}`}>{name}</Link>}
      <Divider />
    </Paper>
  );
}
