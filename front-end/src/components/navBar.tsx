import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  useTheme,
} from "@mui/material";
import * as React from "react";

export type NavInfo = {
  label: string
  href: string
}

export interface INavBarProps {
  content: NavInfo[]
  barWidth: string;
}

export function NavBar(props: INavBarProps) {
  const theme = useTheme();
  const styles = {
    drawer: {
      width: props.barWidth,
    },
    linkText: {
      color: "white",
    } as const,
  };

  const mapLinks = props.content.map((v) => (
    <ListItem key={v.label} disablePadding>
      <ListItemButton component="a" href={v.href}>
        <ListItemText style={styles.linkText} primary={v.label} />
      </ListItemButton>
    </ListItem>
  ));

  return (
    <Drawer
      style={styles.drawer}
      sx={{
        "& .MuiDrawer-paper": {
          width: props.barWidth,
          boxSizing: "border-box",
          backgroundColor: theme.palette.primary.dark,
        },
      }}
      variant="permanent"
      anchor="left"
    >
      <List>{mapLinks}</List>
    </Drawer>
  );
}
