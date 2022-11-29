import { Divider, Link, Paper, TextField } from "@mui/material";
import * as React from "react";
import { ChangeEvent, useState } from "react";

export interface PacketListEntryProps {
  name: string;
  id: string;
  new: boolean;
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
      props.renameFunc(name);
    }
  };

  const handleNameEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleNameInputBlur();
    }
  };

  const validateName = (name: string) => {
    if (name.length < 3) {
      return "Must be at least 3 characters";
    }
    if (name.length > 20) {
      return "Must not exceed 20 characters";
    }
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
          onKeyDown={handleNameEnter}
        />
      )}
      {!nameInputActive && <Link href={`./packet/${props.id}`}>{name}</Link>}
      <Divider />
    </Paper>
  );
}
