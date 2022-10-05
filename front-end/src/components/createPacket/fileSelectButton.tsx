import { Button, useTheme } from "@mui/material";
import { ChangeEvent } from "react";

export interface FileSelectButtonProps {
  buttonLabel: string;
  file: File | undefined;
  setFile: React.Dispatch<React.SetStateAction<File | undefined>>;
  validateSelection: (f: File | undefined) => boolean
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

  const onChange = (event: ChangeEvent<HTMLInputElement>) => {
    const newFile = event.target.files?.item(0)!;
    if (!props.validateSelection(newFile)) {
      return
    }
    props.setFile(newFile);
  };

  return (
    <div>
      <Button
        style={props.file == null ? styles.buttonUnselected : styles.button}
        variant={props.file == null ? "outlined" : "contained"}
        component="label"
      >
        {props.buttonLabel}
        <input type="file" data-testid="fileSelect" onChange={onChange} hidden />
      </Button>
      <p style={styles.fileName}>{props.file?.name}</p>
    </div>
  );
}
