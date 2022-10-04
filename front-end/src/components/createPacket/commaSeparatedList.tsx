import { Chip, TextField } from "@mui/material";
import * as React from "react";
import { ChangeEvent, useState } from "react";

export interface CommaSeparatedListProps {
  wordSet: Set<string>;
  setWordSet: React.Dispatch<React.SetStateAction<Set<string>>>;
}

export function CommaSeparatedList(props: CommaSeparatedListProps) {
  const styles = {
    input: {
      width: "20vw",
    },
    chipBox: {
      width: "20vw",
      height: "10vw",
      overflow: "scroll",
      border: "1px solid black",
      borderRadius: "10px",
    } as const,
  };

  const [input, setInput] = useState("");

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
    let wordSet = new Set<string>();
    e.target.value.split(",").forEach((v) => {
      if (v !== "") {
        wordSet.add(v);
      }
    });
    props.setWordSet(wordSet);
  };

  const itemDelete = (label: string) => {
    const newSet = props.wordSet;
    newSet.delete(label);
    setInput(Array.from(newSet).join(","));
  };

  const listItems = Array.from(props.wordSet).map((item) => (
    <Chip
      label={item}
      key={item}
      onDelete={() => {
        itemDelete(item);
      }}
    />
  ));

  if (input === "" && props.wordSet.size > 0) {
    // Rerenders input
    setInput(Array.from(props.wordSet).join(","));
  }

  return (
    <div>
      <TextField
        value={input}
        style={styles.input}
        onChange={handleInputChange}
        id="highlightInput"
        label="Highlight Words"
        variant="outlined"
      />
      <div style={styles.chipBox}>{listItems}</div>
    </div>
  );
}
