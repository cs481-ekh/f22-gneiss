import { Button, useTheme } from "@mui/material";
import { ChangeEvent, useState } from "react";

export interface FileSelectButtonProps {
  buttonLabel: string;
}

export function FileSelectButton(props: FileSelectButtonProps) {
  const theme = useTheme();
  const styles = {
    button: {
      width: "100%",
    },
    buttonUnselected: {
      width: "100%",
      borderColor: theme.palette.secondary.main,
      color: theme.palette.secondary.main,
    },
    fileName: {
      whiteSpace: "nowrap",
      overflow: "hidden",
      textOverflow: "ellipsis",
      fontSize: "8pt",
    } as React.CSSProperties,
  };

  const [file, setFile] = useState<File>();

  const onChange = (event: ChangeEvent<HTMLInputElement>) => {
    const newFile = event.target.files?.item(0);
    if (newFile != null) {
      setFile(newFile);
    }
  };

  return (
    <div>
      <Button
        style={file == null ? styles.buttonUnselected : styles.button}
        variant={file == null ? "outlined" : "contained"}
        component="label"
      >
        {props.buttonLabel}
        <input type="file" onChange={onChange} hidden />
      </Button>
      <p style={styles.fileName}>{file?.name}</p>
    </div>
  );
}
